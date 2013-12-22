package com.excella.gradle.cucumber;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the execution of a command line and the capture of the output.
 */
public class ProcessRunner {

  private final ProcessBuilder processBuilder;
  private final String encoding = "ASCII";
  private final StringBuilder out = new StringBuilder();
  private final StringBuilder err = new StringBuilder();
  private int exitCode = Integer.MIN_VALUE;


  public ProcessRunner(ProcessBuilder processBuilder) {
    this.processBuilder = processBuilder;
  }

  public String getOut() {
    return out.toString();
  }

  public String getErr() {
    return err.toString();
  }

  public int getExitCode() {
    return exitCode;
  }

  public int run() throws IOException {
    for (String s : processBuilder.command()) {
      System.out.print(s + " ");
    }
    System.out.println();

    Process process = processBuilder.start();

    InputStream outputStream = null, errorStream = null;
    try {
      outputStream = process.getInputStream();
      errorStream = process.getErrorStream();

      byte[] tmp = new byte[1024];

      while (true) {
        int outputBytes = readAvailablOnce(outputStream, out, tmp);
        int errorBytes = readAvailablOnce(errorStream, err, tmp);
        if (outputBytes == 0 && errorBytes == 0) {
          try {
            process.exitValue();
            break;
          } catch (IllegalThreadStateException e) {
            // keep on looping
          }
        }
      }
      readAvailableAll(outputStream, out, tmp);
      readAvailableAll(errorStream, err, tmp);

    } finally {
      closeQuietly(outputStream);
      closeQuietly(errorStream);
    }

    return exitCode = process.exitValue();
  }

  public void runStrict() throws IOException {
    int exitCode = run();
    if (exitCode != 0) {
      String message = err.toString().trim();
      throw new IOException("command failed: " + exitCode + (message.isEmpty() ? "" : "\n  " + message));
    }
  }

  private static void closeQuietly(InputStream in) {
    if (in != null) {
      try {
        in.close();
      } catch (IOException e) {
        // ignored
      }
    }
  }

  private int readAvailablOnce(InputStream inputStream, StringBuilder sb, byte[] buffer) throws IOException {
    int bytesRead = 0;
    if (inputStream.available() > 0) {
      bytesRead = inputStream.read(buffer);
      sb.append(new String(buffer, 0, bytesRead, encoding));
    }
    return bytesRead;
  }

  private void readAvailableAll(InputStream inputStream, StringBuilder sb, byte[] buffer) throws IOException {
    if (inputStream.available() > 0) {
      int bytesRead = 0;
      while ((bytesRead = inputStream.read(buffer)) >= 0) {
        sb.append(new String(buffer, 0, bytesRead, encoding));
      }
    }
  }
}
