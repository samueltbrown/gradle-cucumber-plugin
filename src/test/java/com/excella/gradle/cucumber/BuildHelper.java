package com.excella.gradle.cucumber;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class BuildHelper {

  private final ProjectHelper projectHelper;

  private List<String> buildscriptDependencies = new ArrayList<String>();
  private List<String> buildscriptRepositories = new ArrayList<String>();
  private List<String> sourceSets = new ArrayList<String>();
  private List<String> configurations = new ArrayList<String>();
  private List<String> dependencies = new ArrayList<String>();
  private List<String> repositories = new ArrayList<String>();
  private List<String> applies = new ArrayList<String>();
  private String wrapperVersion;
  private List<String> tasks = new ArrayList<String>();


  public BuildHelper(ProjectHelper projectHelper, String wrapperVersion) {
    this.projectHelper = projectHelper;
    this.wrapperVersion = wrapperVersion;
    task(
      "task createWrapper(type: Wrapper) {\n" +
      "  gradleVersion = '" + wrapperVersion + "'\n" +
      "}");
  }

  public BuildHelper buildscriptDependency(String dependency) {
    buildscriptDependencies.add(dependency);
    return this;
  }

  public BuildHelper buildscriptRepositories(String repository) {
    buildscriptRepositories.add(repository);
    return this;
  }

  public BuildHelper sourceSet(String sourceSet) {
    sourceSets.add(sourceSet);
    return this;
  }

  public BuildHelper configuration(String configuration) {
    configurations.add(configuration);
    return this;
  }

  public BuildHelper dependency(String dependency) {
    dependencies.add(dependency);
    return this;
  }

  public BuildHelper repository(String repository) {
    repositories.add(repository);
    return this;
  }

  public BuildHelper apply(String apply) {
    return apply(apply,-1);
  }

  public BuildHelper apply(String apply, int index) {
    if(index != -1)
      applies.add(index,apply);
    else applies.add(apply);
    return this;
  }

  public BuildHelper task(String taskCode) {
    tasks.add(taskCode);
    return this;
  }

  public void applyBuiltinPlugin(String pluginId) {
    apply("apply plugin: '" + pluginId + "'");
  }

  private boolean isGradle2_1OrAfter() {
    return Float.parseFloat(wrapperVersion.replaceFirst("(\\d+\\.\\d+).*", "$1")) >= 2.1;
  }

  public void addCucumberPlugin(
    String pluginRepo,
    String pluginVersion,
    String cucumberVersion,
    String cucumberSourceSetName,
    boolean runtimePlugin)
  throws IOException {
    final boolean isStandardCucumberSourceSet = "cucumber".equals(cucumberSourceSetName);
    boolean gradle2_1OrAfter = isGradle2_1OrAfter();

    if (!"jcenter".equals(pluginRepo) || !gradle2_1OrAfter) {
      if (!"dev".equals(pluginRepo)) {
        buildscriptRepositories.add(pluginRepo + "()");
      }
      if (!"mavenCentral".equals(pluginRepo)) {
        buildscriptRepositories("mavenCentral()");
      }
    }

    if ("dev".equals(pluginVersion)) {
      for (String path : projectHelper.getCucumberPluginClasspath()) {
        buildscriptDependency("classpath files('" + path + "')");
      }
    } else {
      if (!gradle2_1OrAfter) {
        buildscriptDependency("classpath 'com.github.samueltbrown:gradle-cucumber-plugin:" + pluginVersion + "'");
      }
    }

    if ("jcenter".equals(pluginRepo)) {
      if (gradle2_1OrAfter) {
        apply("plugins {\n" +
          "  id 'com.github.samueltbrown.cucumber'" +
          "  version '" + pluginVersion + "'\n" +
          "}",0);

      } else {
        apply("apply plugin: 'com.github.samueltbrown.cucumber'");
      }
    } else if ("mavenCentral".equals(pluginRepo)) {
      apply("apply plugin: 'cucumber'");
    } else {
      apply("apply plugin: " + CucumberPlugin.class.getName());
    }

    if (!isStandardCucumberSourceSet) {
      // it has to be declared explicitly
      sourceSet(
        cucumberSourceSetName + " {\n" +
        "  compileClasspath = sourceSets.main.output + sourceSets.test.output + configurations.intTestCompile\n" +
        "  runtimeClasspath = output + compileClasspath + configurations.intTestRuntime\n" +
        "}\n");
    }

    repository("mavenCentral()");
    if (runtimePlugin) {
      dependency(cucumberSourceSetName + "Runtime 'info.cukes:cucumber-java:" + cucumberVersion + "'");
    } else {
      dependency(cucumberSourceSetName + "Compile 'info.cukes:cucumber-core:" + cucumberVersion + "'");
      dependency(cucumberSourceSetName + "Compile 'info.cukes:cucumber-jvm:" + cucumberVersion + "'");
      dependency(cucumberSourceSetName + "Compile 'info.cukes:cucumber-java:" + cucumberVersion + "'");
    }

    if (!isStandardCucumberSourceSet) {
      configuration(cucumberSourceSetName + "Compile.extendsFrom(configurations.testCompile)");
      configuration(cucumberSourceSetName + "Runtime.extendsFrom(configurations." + cucumberSourceSetName + "Compile)");
      dependency(cucumberSourceSetName + "Compile sourceSets.test.output.dirs");
      dependency(cucumberSourceSetName + "Runtime sourceSets." + cucumberSourceSetName + ".output.dirs");
    }
  }

  private void copyWrapperFiles() throws IOException {
    File projectDir = projectHelper.getProjectDir();
    FileUtils.copyDirectoryToDirectory(new File("gradle"), projectDir);
    FileUtils.copyFileToDirectory(new File("gradlew"), projectDir);
    new File(projectDir, "gradlew").setExecutable(true);
    FileUtils.copyFileToDirectory(new File("gradlew.bat"), projectDir);
  }

  public File build() throws IOException {
    copyWrapperFiles();

    StringBuilder script = new StringBuilder();

    script.append("buildscript {\n");

    script.append("  repositories {\n");
    for (String buildscriptRepository : buildscriptRepositories) {
      script.append("    ").append(buildscriptRepository).append("\n");
    }
    script.append("  }\n");

    script.append("  dependencies {\n");
    for (String buildscriptDependency : buildscriptDependencies) {
      script.append("    ").append(buildscriptDependency).append("\n");
    }
    script.append("  }\n");

    script.append("}\n");

    script.append("\n");
    for (String apply : applies) {
      script.append(apply).append("\n");
    }
    script.append("\n");

    if (!sourceSets.isEmpty()) {
      script.append("sourceSets {\n");
      for (String sourceSet : sourceSets) {
        script.append("  ").append(sourceSet).append("\n");
      }
      script.append("}\n\n");
    }

    script.append("configurations {\n");
    for (String configuration : configurations) {
      script.append("  ").append(configuration).append("\n");
    }
    script.append("}\n\n");

    script.append("repositories {\n");
    for (String repository : repositories) {
      script.append("  ").append(repository).append("\n");
    }
    script.append("}\n\n");

    script.append("dependencies {\n");
    for (String dependency : dependencies) {
      script.append("  ").append(dependency).append("\n");
    }
    script.append("}\n\n");

    for (String task : tasks) {
      script.append("\n" + task + "\n");
    }

    File scriptFile = projectHelper.newFile("build.gradle", script.toString());

    System.out.println("=======================================================================");
    System.out.println(script);
    System.out.println("=======================================================================");

    new ProcessRunner(processBuilder("--no-daemon", "--rerun-tasks", "createWrapper", "--stacktrace")).runStrict();

    return scriptFile;
  }

  public ProcessBuilder processBuilder(String... args) {
    return processBuilder(true, args);
  }

  public ProcessBuilder processBuilder(boolean useWrapperScript, String... args) {
    List<String> command = new ArrayList<String>();
    String gradlewScript;
    if(System.getProperty("os.name").startsWith("Windows")){
      gradlewScript = "./gradlew.bat";
    } else {
      gradlewScript = "./gradlew";
    }

    if (useWrapperScript) {
      command.add(new File(projectHelper.getProjectDir(), gradlewScript).getAbsolutePath());
    } else {
      command.add("gradle");
    }

    command.addAll(Arrays.asList(args));
    return new ProcessBuilder(command).directory(projectHelper.getProjectDir());
  }
}
