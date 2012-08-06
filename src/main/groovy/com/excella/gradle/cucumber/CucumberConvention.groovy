package com.excella.gradle.cucumber

import org.gradle.api.Project

/**
 * Parameters used in the cucumber task.  Convention pattern used to pass these parameters to the cucumber
 * runner.
 *
 *
 * @author: Samuel Brown
 * @since: 0.1
 * @version 0.1
 *
 */
class CucumberConvention {

    /**
     *  Directories to use as source for both feature files and step definitions. Defaults to [src/test/resources]
     */
    List<String> glueDirs = ['src/test/java','src/test/resources']

    /**
     * Tags used to filter which scenarios should be run.
     */
    List<String> tags

    /**
     * Output formats for cucumber test results. Defaults to 'pretty'
     */
    List<String> formats = ['pretty']

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
    }

    def cucumber(Closure closure) {
        closure.setDelegate this
        closure.call()
    }

}
