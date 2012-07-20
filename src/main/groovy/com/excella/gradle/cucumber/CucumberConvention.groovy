package com.excella.gradle.cucumber

import org.gradle.api.Project

/**
 *
 * User: samuelbrown
 * Date: 7/18/12
 * Time: 10:27 PM
 *
 */
class CucumberConvention {

    /**
     *  Directories to use as source for both feature files and step definitions. Defaults to [src/test/resources]
     */
    List<String> glueDirs

    /**
     * Tags used to filter which scenarios should be run.
     */
    List<String> tags

    /**
     * Output formats for cucumber test results. Defaults to 'pretty'
     */
    List<String> formats

    /**
     * Execute a test dry run without actually executing tests. Defaults to false
     */
    boolean dryRun = false

    /**
     * Strict mode, fail if there are pending or skipped tests. Defaults to false
     */
    boolean strict = false

    /**
     * Format output in single color.  Defaults to false
     */
    boolean monochrome = false

    /**
     * Version of cucumber-jvm to use to execute cucumber tests
     */
    String cucumberJvmVersion = '1.0.11'

    private Project project

    CucumberConvention(Project project) {
        this.project = project
        glueDirs = ['src/test/resources']
        formats = ['pretty']

    }

    def cucumber(Closure closure) {
        closure.setDelegate this
        closure.call()
    }

}
