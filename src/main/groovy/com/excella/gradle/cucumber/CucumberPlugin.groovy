package com.excella.gradle.cucumber

import com.excella.gradle.cucumber.tasks.CucumberTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.PluginInstantiationException
import org.gradle.api.tasks.SourceSet

/**
 * Cucumber plugin definition.  This class initializes the plugin, sets up the convention mapping and adds
 * the cucumber task as an available gradle task.
 *
 * @author Samuel Brown
 * @since 0.1
 * @version 0.1
 *
 *
 */
class CucumberPlugin  implements Plugin<Project> {

    static final String CLASSPATH = 'classpath'
    static final String CUCUMBER_SOURCE_SET_NAME = 'cucumber'
    static final String CUCUMBER_RUNTIME_CONFIGURATION_NAME
    static final String CUCUMBER_COMPILE_CONFIGURATION_NAME

    static {
        try {
            CUCUMBER_RUNTIME_CONFIGURATION_NAME = CUCUMBER_SOURCE_SET_NAME + 'Runtime'
            CUCUMBER_COMPILE_CONFIGURATION_NAME = CUCUMBER_SOURCE_SET_NAME + "Compile"
        }catch(NoClassDefFoundError e){
            throw new PluginInstantiationException("Cucumber plugin only works with gradle version above 2.0 due to groovy version in older gradle versions")
        }
    }

    def hasCucumberSourceSet

    @Override
    void apply(Project project) {
        project.plugins.apply(JavaPlugin)

        project.extensions.cucumberRunner = new CucumberRunner()

        project.configurations.create(CUCUMBER_RUNTIME_CONFIGURATION_NAME).setVisible(false).setTransitive(true)
                .setDescription('The Cucumber libraries to be used for this project.')
                .extendsFrom(project.configurations.getByName('testRuntime'))

        CucumberConvention cucumberConvention = project.extensions.create("cucumber", CucumberConvention, project)
        CucumberJvmOptions jvmOptions =
            cucumberConvention.extensions.create("jvmOptions", CucumberJvmOptions, project.getFileResolver())

        hasCucumberSourceSet = project.file('src/cucumber').exists()
        if (hasCucumberSourceSet) {
            SourceSet cucumberSourceSet = project.sourceSets.maybeCreate(CUCUMBER_SOURCE_SET_NAME)
            SourceSet mainSourceSet = project.sourceSets.main
            SourceSet testSourceSet = project.sourceSets.test

            Configuration cucumberCompile = project.configurations.getByName(cucumberSourceSet.compileConfigurationName)
            cucumberCompile
                .setVisible(true)
                .setTransitive(true)
                .setDescription('The Cucumber classes to be used for this project.')
                .extendsFrom(project.configurations.testCompile)
            project.dependencies.add(cucumberSourceSet.compileConfigurationName, mainSourceSet.output)
            project.dependencies.add(cucumberSourceSet.compileConfigurationName, testSourceSet.output)

            project.configurations.getByName(cucumberSourceSet.runtimeConfigurationName)
                .setVisible(true)
                .setTransitive(true)
                .setDescription('The Cucumber libraries to be used for this project.')

            // don't use defaults if the cucumber source set exists
            cucumberConvention.featureDirs = []
            cucumberConvention.glueDirs = []

            if (project.plugins.hasPlugin("eclipse")) {
                project.eclipse.classpath {
                        plusConfigurations += [ cucumberCompile ]
                        noExportConfigurations += [ cucumberCompile ]
                    }
            }

            if (project.plugins.hasPlugin("idea")) {
                cucumberSourceSet.allSource.srcDirs.flatten().each { sourceDir ->
                    if (!cucumberSourceSet.resources.srcDirs.contains(sourceDir)) {
                        project.idea.module {
                            testSourceDirs += [ sourceDir ]
                        }
                    }
                }
                project.idea.module {
                    scopes.TEST.plus += [ cucumberCompile ]
                }
            }
        }

        project.convention.plugins.cucumber = cucumberConvention

        configureCucumberTask(project, cucumberConvention, jvmOptions)

    }

    private def configureCucumberTask(
        final Project project, CucumberConvention cucumberConvention, CucumberJvmOptions jvmOptions)
    {
        project.tasks.withType(CucumberTask).whenTaskAdded { CucumberTask cucumberTask ->
            cucumberTask.conventionMapping.map('buildscriptClasspath') { project.buildscript.configurations.getByName(CLASSPATH) }
            cucumberTask.conventionMapping.map('cucumberClasspath') { getCucumberClasspath(project, cucumberConvention) }
            cucumberTask.conventionMapping.map('glueDirs') { cucumberConvention.glueDirs }
			   cucumberTask.conventionMapping.map('featureDirs') { getFeaturesDir(project, cucumberConvention) }
            cucumberTask.conventionMapping.map('tags') { cucumberConvention.tags }
            cucumberTask.conventionMapping.map('formats') { cucumberConvention.formats }
            cucumberTask.conventionMapping.map('strict') { cucumberConvention.strict }
            cucumberTask.conventionMapping.map('monochrome') { cucumberConvention.monochrome }
            cucumberTask.conventionMapping.map('dryRun') { cucumberConvention.dryRun }
            cucumberTask.conventionMapping.map('sourceSets') { getCucumberSourceSets(project, cucumberConvention) }
            cucumberTask.conventionMapping.map('jvmOptions') { jvmOptions }
            cucumberTask.conventionMapping.map('ignoreFailures') {cucumberConvention.ignoreFailures}

            cucumberTask.runner = project.cucumberRunner
            if (hasCucumberSourceSet) {
                cucumberTask.dependsOn(project.tasks.getByName('cucumberClasses'))
            }
        }

        CucumberTask cucumberTask = project.tasks.create(name: 'cucumber', dependsOn: ['assemble'], type: CucumberTask)
        cucumberTask.jvmOptions = jvmOptions
        cucumberTask.description = "Run cucumber acceptance tests."
        cucumberTask.group = "Verification"
    }

    private def getCucumberSourceSets(final Project project, final CucumberConvention cucumberConvention) {
        if (cucumberConvention.sourceSets) {
            cucumberConvention.sourceSets
        } else if (hasCucumberSourceSet) {
            [project.sourceSets.getByName(CUCUMBER_SOURCE_SET_NAME)]
        } else {
            []
        }
    }

    private def getCucumberClasspath(final Project project, final CucumberConvention cucumberConvention) {
        FileCollection cucumberClasspath = project.files()

        getSourceSets(project, cucumberConvention).each { sourceSet ->
            cucumberClasspath += sourceSet.runtimeClasspath
        }

        // Although "cucumberRuntime" might also be collected within the SourceSets, it will NOT if the 'java' plugin is
        // is not applied ==> revert to the "cucumberRuntime" configuration.
        if (cucumberClasspath.empty) {
            cucumberClasspath = project.configurations.getByName(CUCUMBER_RUNTIME_CONFIGURATION_NAME)
        }

        cucumberClasspath
    }

    private def getFeaturesDir(final Project project, final CucumberConvention cucumberConvention) {
        def sourceSets = getSourceSets(project, cucumberConvention)

        if (!cucumberConvention.featureDirs && !sourceSets) {
            return ['src/test/resources']
        }

        List<String> featureDirs = []

        cucumberConvention.featureDirs.each { featureDir ->
            project.file(featureDir).exists() && featureDirs << featureDir
        }

        sourceSets.each { sourceSet ->
            sourceSet.resources.srcDirs.each { srcDir -> srcDir.exists() && featureDirs << srcDir.path }
        }

        featureDirs
    }

    private def getSourceSets(Project project, CucumberConvention cucumberConvention) {
        List<SourceSet> sourceSets = cucumberConvention.sourceSets
        if (!sourceSets) {
            SourceSet cucumberSourceSet = project.sourceSets.findByName(CUCUMBER_SOURCE_SET_NAME)
            if (cucumberSourceSet) {
                sourceSets = [cucumberSourceSet]
            }
        }
        sourceSets
    }
}
