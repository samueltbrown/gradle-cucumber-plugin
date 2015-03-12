package com.excella.gradle.cucumber

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

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
     *  Directories to use as source for step definitions. Defaults to [src/test/java]
     */
    List<String> glueDirs = []
	
	/**
	 * Directories to look for feature files. Defaults to [src/test/resources]
	 */
	List<String> featureDirs = []

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
     * Whether to cause a build failure on any test failures.
     */
    boolean ignoreFailures = false

    /**
     * Version of cucumber-jvm to use to execute cucumber tests
     *
     * @deprecated simply use <code>cucumberCompile</code> dependencies
     */
    String cucumberJvmVersion = '1.1.6'

    /**
     * If specified, the resource directories of sourceSets will be used as feature directories and all other source
     * directories will be contributed to the glue dirs. The runtime classpaths of sourceSets will be appended to the
     * Cucumber classpath.
     */
    List<SourceSet> sourceSets

    private Project project

    CucumberConvention(Project project) {
        this.project = project
    }

    def cucumber(Closure closure) {
        closure.setDelegate this
        closure.call()
    }
}
