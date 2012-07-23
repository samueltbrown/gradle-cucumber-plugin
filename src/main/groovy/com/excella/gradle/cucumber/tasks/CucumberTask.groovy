package com.excella.gradle.cucumber.tasks

import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.testing.Test

/**
 * Created with IntelliJ IDEA.
 * User: samuelbrown
 * Date: 7/18/12
 * Time: 11:36 PM
 * To change this template use File | Settings | File Templates.
 */


class CucumberTask extends Test {

    def runner
    List<String> glueDirs
    List<String> tags
    List<String> formats
    boolean strict
    boolean monochrome
    boolean dryRun

    @TaskAction
    def cucumber() {
        runner.runCucumberTests getGlueDirs(), getTags(), getFormats(), getStrict(), getMonochrome(), getDryRun()
    }
}
