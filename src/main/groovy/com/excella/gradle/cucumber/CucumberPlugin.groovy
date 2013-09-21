package com.excella.gradle.cucumber

import com.excella.gradle.cucumber.tasks.CucumberTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

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
    static final String CUCUMBER_RUNTIME_CONFIGURATION_NAME = 'cucumberRuntime'


    @Override
    void apply(Project project) {
        project.plugins.apply(JavaPlugin)

        project.extensions.cucumberRunner = new CucumberRunner()

        project.configurations.create(CUCUMBER_RUNTIME_CONFIGURATION_NAME).setVisible(false).setTransitive(true)
                .setDescription('The Cucumber libraries to be used for this project.')
                .extendsFrom(project.configurations.getByName('testRuntime'))

        CucumberConvention cucumberConvention = new CucumberConvention(project)
        project.convention.plugins.cucumber = cucumberConvention

        configureCucumberTask(project, cucumberConvention)

    }

    private def configureCucumberTask(final Project project, CucumberConvention cucumberConvention) {
        project.tasks.withType(CucumberTask).whenTaskAdded { CucumberTask cucumberTask ->
            cucumberTask.conventionMapping.map('buildscriptClasspath') { project.buildscript.configurations.getByName(CLASSPATH).asFileTree }
            cucumberTask.conventionMapping.map('cucumberClasspath') { project.configurations.getByName(CUCUMBER_RUNTIME_CONFIGURATION_NAME).asFileTree }
            cucumberTask.conventionMapping.map('glueDirs') { cucumberConvention.glueDirs }
			cucumberTask.conventionMapping.map('featureDirs') { cucumberConvention.featureDirs }
            cucumberTask.conventionMapping.map('tags') { cucumberConvention.tags }
            cucumberTask.conventionMapping.map('formats') { cucumberConvention.formats }
            cucumberTask.conventionMapping.map('strict') { cucumberConvention.strict }
            cucumberTask.conventionMapping.map('monochrome') { cucumberConvention.monochrome }
            cucumberTask.conventionMapping.map('dryRun') { cucumberConvention.dryRun }
            cucumberTask.runner = project.cucumberRunner
        }

        CucumberTask cucumberTask = project.tasks.create(name: 'cucumber', dependsOn: ['assemble'], type: CucumberTask)
        cucumberTask.description = "Run cucumber acceptance tests."
        cucumberTask.group = "Verification"
    }
}
