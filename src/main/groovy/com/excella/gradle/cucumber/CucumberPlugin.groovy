package com.excella.gradle.cucumber

import com.excella.gradle.cucumber.tasks.CucumberTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 
 * 
 * @author Sam Brown
 * @since 7/18/12
 * @version 1.0
 *
 * Copyright 2012, Excella Consulting
 * 
 */
class CucumberPlugin  implements Plugin<Project> {
    Project project

    @Override
    void apply(Project project) {
        this.project = project
        project.apply plugin: 'java'

        project.extensions.cucumberRunner = new CucumberRunner()

        project.convention.plugins.cobertura = new CucumberConvention(project);
        if (!project.configurations.asMap['cucumber']) {
            project.configurations.add('cucumber') {
                extendsFrom project.configurations['testCompile']
            }
            project.dependencies {
                cucumber "info.cukes:cucumber-junit:${project.cucumberJvmVersion}"
            }
        }

        project.tasks.withType(CucumberTask).whenTaskAdded {
            it.runner = project.cucumberRunner
        }
        CucumberTask cucumberTask = project.tasks.add(name: 'cucumber', dependsOn: [ 'clean','build','cleanTest','test'], type: CucumberTask)
        cucumberTask.description = "Run Cucumber Acceptance Tests"
        coverageReport.group = "Test"

        project.dependencies.add('testRuntime',  "info.cukes:cucumber-junit:${project.cucumberJvmVersion}")
    }
}
