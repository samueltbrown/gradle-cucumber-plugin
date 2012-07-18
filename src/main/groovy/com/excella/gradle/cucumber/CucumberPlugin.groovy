package com.excella.gradle.cucumber

import java.lang.reflect.Constructor;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.AsmBackedClassGenerator;
import org.gradle.api.tasks.testing.Test;


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
    }
}
