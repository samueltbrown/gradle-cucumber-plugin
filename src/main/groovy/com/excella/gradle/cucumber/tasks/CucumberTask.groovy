package com.excella.gradle.cucumber.tasks

import com.excella.gradle.cucumber.CucumberJvmOptions
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Defines the cucumber task that can be used in a gradle build file.  This class creates its own
 * classloader for use in running cucumber.  This task will call the @see com.excella.gradle.cucumber.CucumberRunner
 * to execute the main cucumber-jvm cli.
 *
 *
 * @author: Samuel Brown
 * @since: 0.1
 * @version 0.1
 *
 */


class CucumberTask extends DefaultTask  {
    static final Logger LOGGER = LoggerFactory.getLogger(CucumberTask.class)

    def runner
    List<String> glueDirs
	List<String> featureDirs
    List<String> tags
    List<String> formats
    boolean strict
    boolean monochrome
    boolean dryRun
    boolean ignoreFailures
    FileCollection buildscriptClasspath
    FileCollection cucumberClasspath
    List<SourceSet> sourceSets
    CucumberJvmOptions jvmOptions

    @TaskAction
    def cucumber() {
        LOGGER.info "Configuring Cucumber for ${getProject()}"

        final execTask = getProject().task(type: JavaExec, 'cucumberExec')
        jvmOptions.copyTo(execTask)

        try {
            runner.runCucumberTests(
                  execTask,
                  getCucumberClasspath(),
                  getOrDetectGlueDirs(),
                  getTags(),
                  getFormats(),
                  getStrict(),
                  getMonochrome(),
                  getDryRun(),
                  getFeatureDirs())
        }catch(Exception exception){
            if(!getIgnoreFailures()){
                throw exception
            }
        }
    }

    private List<String> getOrDetectGlueDirs() {
        List<String> dirs = getGlueDirs() ?: []
        List<SourceSet> glueSourceSets = getSourceSets()

        if (!dirs && !glueSourceSets) {
            dirs = ['src/test/java'] // default

        } else if (glueSourceSets) {
            glueSourceSets.each { sourceSet ->
                // add output resources dir for non-Java-class implementations
                dirs << sourceSet.output.resourcesDir.path

                if (sourceSet.output.classesDir.exists()) {
                    // add all subdirs of the classes dir for compiled implementations
                    def packages = new TreeSet()
                    def classesDirPathLength = sourceSet.output.classesDir.path.length() + 1

                    sourceSet.output.classesDir.traverse { File file ->
                        if (file.isFile()) {
                            String relativePath = file.path.substring(classesDirPathLength)
                            def packageDir = relativePath.
                                replace(File.separator, '/'). // make sure we are dealing with slashes
                                replaceFirst('/?[^/]*$', ''). // remove the file name --> keep the parent dir path
                                replace('/', '.') // turn into a package name
                            packages << "classpath:${packageDir}".toString()
                        }
                    }
                    dirs.addAll(packages)
                }
            }
        }
        dirs.unique()
    }
}
