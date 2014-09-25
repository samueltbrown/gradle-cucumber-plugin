package com.excella.gradle.cucumber

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.process.JavaForkOptions
import org.gradle.process.ProcessForkOptions
import org.gradle.process.internal.DefaultJavaForkOptions

/**
 * DSL extension.
 */
class CucumberJvmOptions implements JavaForkOptions, ProcessForkOptions {

    def JavaForkOptions forkOptions


    CucumberJvmOptions() {
        this(new IdentityFileResolver())
    }

    CucumberJvmOptions(FileResolver fileResolver) {
        forkOptions= new DefaultJavaForkOptions(fileResolver)
    }

    @Override
    Map<String, Object> getSystemProperties() {
        return forkOptions.getSystemProperties()
    }

    @Override
    void setSystemProperties(Map<String, ?> stringMap) {
        forkOptions.setSystemProperties(stringMap)
    }

    @Override
    JavaForkOptions systemProperties(Map<String, ?> stringMap) {
        forkOptions.systemProperties(stringMap)
        return this
    }

    @Override
    JavaForkOptions systemProperty(String s, Object o) {
        forkOptions.systemProperty(s, o)
        return this
    }

    @Override
    String getDefaultCharacterEncoding() {
        return forkOptions.getDefaultCharacterEncoding()
    }

    @Override
    void setDefaultCharacterEncoding(String s) {
        forkOptions.setDefaultCharacterEncoding(s)
    }

    @Override
    String getMinHeapSize() {
        return forkOptions.getMinHeapSize()
    }

    @Override
    void setMinHeapSize(String s) {
        forkOptions.setMinHeapSize(s)
    }

    @Override
    String getMaxHeapSize() {
        return forkOptions.getMaxHeapSize()
    }

    @Override
    void setMaxHeapSize(String s) {
        forkOptions.setMaxHeapSize(s)
    }

    @Override
    List<String> getJvmArgs() {
        return forkOptions.getJvmArgs()
    }

    @Override
    void setJvmArgs(Iterable<?> objects) {
        forkOptions.setJvmArgs(objects)
    }

    @Override
    JavaForkOptions jvmArgs(Iterable<?> objects) {
        forkOptions.jvmArgs(objects)
        return this
    }

    @Override
    JavaForkOptions jvmArgs(Object... objects) {
        forkOptions.jvmArgs(objects)
        return this
    }

    @Override
    FileCollection getBootstrapClasspath() {
        return forkOptions.getBootstrapClasspath()
    }

    @Override
    void setBootstrapClasspath(FileCollection files) {
        forkOptions.setBootstrapClasspath(files)
    }

    @Override
    JavaForkOptions bootstrapClasspath(Object... objects) {
        forkOptions.bootstrapClasspath(objects)
        return this
    }

    @Override
    boolean getEnableAssertions() {
        return forkOptions.getEnableAssertions()
    }

    @Override
    void setEnableAssertions(boolean b) {
        forkOptions.setEnableAssertions(b)
    }

    @Override
    boolean getDebug() {
        return forkOptions.getDebug()
    }

    @Override
    void setDebug(boolean b) {
        forkOptions.setDebug(b)
    }

    @Override
    List<String> getAllJvmArgs() {
        return forkOptions.getAllJvmArgs()
    }

    @Override
    void setAllJvmArgs(Iterable<?> objects) {
        forkOptions.setAllJvmArgs(objects)
    }

    @Override
    JavaForkOptions copyTo(JavaForkOptions javaForkOptions) {
        forkOptions.copyTo(javaForkOptions)
        return this
    }

    @Override
    String getExecutable() {
        return forkOptions.getExecutable()
    }

    @Override
    void setExecutable(Object o) {
        forkOptions.setExecutable(o)
    }

    @Override
    ProcessForkOptions executable(Object o) {
        forkOptions.executable(o)
        return this
    }

    @Override
    File getWorkingDir() {
        return forkOptions.getWorkingDir()
    }

    @Override
    void setWorkingDir(Object o) {
        forkOptions.setWorkingDir(o)
    }

    @Override
    ProcessForkOptions workingDir(Object o) {
        forkOptions.workingDir(o)
        return this
    }

    @Override
    Map<String, Object> getEnvironment() {
        return forkOptions.getEnvironment()
    }

    @Override
    void setEnvironment(Map<String, ?> stringMap) {
        forkOptions.setEnvironment(stringMap)
    }

    @Override
    ProcessForkOptions environment(Map<String, ?> stringMap) {
        forkOptions.environment(stringMap)
        return this
    }

    @Override
    ProcessForkOptions environment(String s, Object o) {
        forkOptions.environment(s, o)
        return this
    }

    @Override
    ProcessForkOptions copyTo(ProcessForkOptions processForkOptions) {
        forkOptions.copyTo(processForkOptions)
        return this
    }

    CucumberJvmOptions copyTo(CucumberJvmOptions cucumberJvmOptions) {
        forkOptions.copyTo(cucumberJvmOptions.forkOptions)
        return this
    }
}
