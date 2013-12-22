package com.excella.gradle.cucumber;

import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A JUnit rule providing helper methods to tests.
 */
public class Helper extends TemporaryFolder {

  private List<String> cucumberPluginClasspath;

  /**
   * Creates a new empty Gradle project directory.
   */
  public ProjectHelper newProjectDir() throws IOException {
    return new ProjectHelper(this, newFolder());
  }

  /**
   * Returns the development test classpath of this plugin (requires the Gradle build to have a corresponding
   * 'printMainRuntimeClasspath' tasks). The classpath is returned as list of file paths.
   */
  public List<String> getCucumberPluginClasspath() throws IOException {
    if (cucumberPluginClasspath != null) return cucumberPluginClasspath;

    ProcessRunner runner = new ProcessRunner(new ProcessBuilder(new File("./gradlew").getAbsolutePath(), "-q", "printMainRuntimeClasspath"));
    int exitCode = runner.run();
    if (exitCode != 0) {
      throw new IOException("command failed: " + exitCode);
    }

    BufferedReader reader = new BufferedReader(new StringReader(runner.getOut()));
    String line;
    cucumberPluginClasspath = new ArrayList<String>();
    while ((line = reader.readLine()) != null) {
      cucumberPluginClasspath.add(line);
    }
    return cucumberPluginClasspath;
  }
}
