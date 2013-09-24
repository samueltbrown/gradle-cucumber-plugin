package com.excella.gradle.cucumber

import com.excella.gradle.cucumber.tasks.CucumberTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPlugin
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
    static final String CUCUMBER_RUNTIME_CONFIGURATION_NAME = CUCUMBER_SOURCE_SET_NAME + 'Runtime'
    static final String CUCUMBER_COMPILE_CONFIGURATION_NAME = CUCUMBER_SOURCE_SET_NAME + "Compile"

    def hasCucumberSourceSet

    @Override
    void apply(Project project) {
        project.plugins.apply(JavaPlugin)

        project.extensions.cucumberRunner = new CucumberRunner()

        project.configurations.create(CUCUMBER_RUNTIME_CONFIGURATION_NAME).setVisible(false).setTransitive(true)
                .setDescription('The Cucumber libraries to be used for this project.')
                .extendsFrom(project.configurations.getByName('testRuntime'))

        CucumberConvention cucumberConvention = new CucumberConvention(project)

        hasCucumberSourceSet = project.file('src/cucumber').exists()
        if (hasCucumberSourceSet) {
            project.sourceSets.maybeCreate(CUCUMBER_SOURCE_SET_NAME)
            project.configurations.getByName(CUCUMBER_COMPILE_CONFIGURATION_NAME).setVisible(true).setTransitive(true)
                .setDescription('The Cucumber classes to be used for this project.')
                .extendsFrom(project.configurations.getByName('testRuntime'))
            project.configurations.getByName(CUCUMBER_RUNTIME_CONFIGURATION_NAME).setVisible(true).setTransitive(true)
                .setDescription('The Cucumber libraries to be used for this project.')
                .extendsFrom(project.configurations.getByName(CUCUMBER_COMPILE_CONFIGURATION_NAME))

            // don't use defaults if the cucumber source set exists
            cucumberConvention.featureDirs = []
            cucumberConvention.glueDirs = []
        }

        project.convention.plugins.cucumber = cucumberConvention

        configureCucumberTask(project, cucumberConvention)

    }

    private def configureCucumberTask(final Project project, CucumberConvention cucumberConvention) {
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

            cucumberTask.runner = project.cucumberRunner

            if (hasCucumberSourceSet) {
                cucumberTask.dependsOn(project.getTasksByName('cucumberClasses', false))
            }
        }

        CucumberTask cucumberTask = project.tasks.create(name: 'cucumber', dependsOn: ['assemble'], type: CucumberTask)
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
        if (cucumberConvention.featureDirs) {
            return cucumberConvention.featureDirs
        }

        List<String> featureDirs = []
        getSourceSets(project, cucumberConvention).each { sourceSet ->
            sourceSet.resources.srcDirs.each { srcDir -> featureDirs << srcDir.path }
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
