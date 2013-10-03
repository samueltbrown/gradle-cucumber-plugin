package com.excella.gradle.cucumber;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ProjectHelper {

  private final Helper helper;
  private final File projectDir;


  public ProjectHelper(Helper helper, File projectDir) {
    this.projectDir = projectDir;
    this.helper = helper;
  }

  public File getProjectDir() {
    return projectDir;
  }

  public List<String> getCucumberPluginClasspath() throws IOException {
    return helper.getCucumberPluginClasspath();
  }

  public File newDir(String path) throws IOException {
    File toCreate = new File(projectDir, path);
    if (!toCreate.mkdirs()) {
      throw new IOException("cannot create " + path);
    }
    return toCreate;
  }

  /**
   * Creates a new File under the project directory.
   *
   * @param path relative to the project dir
   */
  public File newFile(String path, String utf8Content) throws IOException {
    File file = new File(projectDir, path);

    File fileParent = file.getParentFile();
    if (!fileParent.exists() && !fileParent.mkdirs()) {
      throw new IOException("failed to create " + fileParent.getAbsolutePath());
    }

    Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
    try {
      out.write(utf8Content);
      out.flush();
    } finally {
      out.close();
    }
    return file;
  }

  /**
   * Creates a Gradle build script under the current temporary project folder.
   */
  public BuildHelper createBuildScript(String wrapperVersion) throws IOException {
    return new BuildHelper(this, wrapperVersion);
  }
}
