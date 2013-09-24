package com.excella.gradle.cucumber;

import gherkin.formatter.PrettyFormatter;
import gherkin.formatter.model.*;

import java.io.IOException;
import java.io.Writer;

/**
 * A PrettyFormatter, which attempts to flush the output at various stages of the execution. Otherwise, it usually gets
 * buffered and you only see it when everything is finished.
 */
public class AsYouGoFormatter extends PrettyFormatter {

  private final Writer writer;


  public AsYouGoFormatter(Appendable out) {
    super(out, false, true);

    writer = out instanceof Writer ? (Writer) out : null;
  }

  private void maybeFlush() {
    if (writer != null) {
      try {
        writer.flush();
      } catch (IOException e) {
        // ignored
      }
    }
  }

  @Override
  public void background(Background background) {
    super.background(background);
    maybeFlush();
  }

  @Override
  public void scenario(Scenario scenario) {
    super.scenario(scenario);
    maybeFlush();
  }

  @Override
  public void scenarioOutline(ScenarioOutline scenarioOutline) {
    super.scenarioOutline(scenarioOutline);
    maybeFlush();
  }

  @Override
  public void eof() {
    super.eof();
    maybeFlush();
  }
}
