package com.excella.gradle.cucumber.tasks

import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: samuelbrown
 * Date: 7/18/12
 * Time: 11:36 PM
 * To change this template use File | Settings | File Templates.
 */


class CucumberTask extends ConventionTask {

    def runner

    @TaskAction
    def cucumber() {
        runner.runCucumberTests
    }
}
