package com.excella.gradle.cucumber.tasks

import org.apache.tools.ant.AntClassLoader
import org.gradle.api.DefaultTask
import org.gradle.api.UncheckedIOException
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory
import org.slf4j.Logger

/**
 * Defines the cucumber task that can be used in a gradle build file.  This class creates its own
 * classloader for use in task execution and then returns the classpath back to normal when finished
 * for the rest of the gradle task calls.  This task will call the @see com.excella.gradle.cucumber.CucumberRunner
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
    FileCollection buildscriptClasspath
    FileCollection cucumberClasspath
    List<SourceSet> sourceSets

    @TaskAction
    def cucumber() {
        LOGGER.info "Configuring Cucumber for ${getProject()}"

        ClassLoader originalClassLoader = getClass().classLoader
        URLClassLoader cucumberClassloader = createCucumberClassLoader()

        try {
            Thread.currentThread().contextClassLoader = cucumberClassloader
            executeCucumberRunner()
        }
        finally {
            Thread.currentThread().contextClassLoader = originalClassLoader
        }

    }

    /**
     * Creates Cucumber ClassLoader which consists of the Gradle runtime, Cucumber runtime and plugin classpath. The ClassLoader
     * is using a parent last strategy to make sure that the provided Gradle libraries get loaded only if they can't be
     * found in the application classpath. Borrowed from Ben Muschko and the gradle tomcat plugin:
     * https://github.com/bmuschko/gradle-tomcat-plugin
     *
     * @return Cucumber ClassLoader
     */
    private URLClassLoader createCucumberClassLoader() {
        ClassLoader rootClassLoader = new AntClassLoader(getClass().classLoader, false)
        URLClassLoader pluginClassloader = new URLClassLoader(toURLArray(getBuildscriptClasspath().files), rootClassLoader)
        new URLClassLoader(toURLArray(getCucumberClasspath().files), pluginClassloader)
    }

    private URL[] toURLArray(Collection<File> files) {
        List<URL> urls = new ArrayList<URL>(files.size())

        for(File file : files) {
            try {
                urls.add(file.toURI().toURL())
            }
            catch(MalformedURLException e) {
                throw new UncheckedIOException(e)
            }
        }

        urls.toArray(new URL[urls.size()]);
    }

    private List<String> getOrDetectGlueDirs() {
        List<String> dirs = getGlueDirs() ?: []
        List<SourceSet> glueSourceSets = getSourceSets()
        if (!dirs && glueSourceSets) {
            glueSourceSets.each { sourceSet ->
                // add output resources dir for non-Java-class implementations
                dirs << sourceSet.output.resourcesDir.path

                // add all subdirs of the classes dir for compiled implementations
                def packages = new TreeSet()
                def classesDirPathLength = sourceSet.output.classesDir.path.length() + 1
                sourceSet.output.classesDir.traverse { File file ->
                    if (file.isFile()) {
                        String relativePath = file.path.substring(classesDirPathLength)
                        def packageDir = relativePath.
                            replaceAll(File.separator, '/'). // make sure we are dealing with slashes
                            replaceFirst('/?[^/]*$', ''). // remove the file name --> keep the parent dir path
                            replaceAll('/', '.') // turn into a package name
                        packages << "classpath:${packageDir}".toString()
                    }
                }
                dirs.addAll(packages)
            }
        }
        dirs.unique()
    }

    private void executeCucumberRunner(){
        runner.runCucumberTests getOrDetectGlueDirs(), getTags(), getFormats(), getStrict(), getMonochrome(), getDryRun(), getFeatureDirs()
    }
}
