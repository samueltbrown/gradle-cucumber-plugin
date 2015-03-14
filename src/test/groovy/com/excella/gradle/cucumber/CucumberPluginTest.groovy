package com.excella.gradle.cucumber

import com.excella.gradle.cucumber.tasks.CucumberTask
import static org.junit.Assert.*
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class CucumberPluginTest {

    @Test
    public void cucumberTaskAddedToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.samueltbrown.cucumber'
        assertTrue(project.tasks.cucumber instanceof CucumberTask)
    }
}
