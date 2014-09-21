package com.excella.gradle.cucumber;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import org.apache.commons.lang.StringUtils;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.JavaExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Mediates between gradle cucumber task and cucumber-jvm execution.  Builds the argument list and then
 * calls the cucumber cli.
 * 
 * @author Samuel Brown
 * @since 0.1
 * @version 0.1
 */
public class CucumberRunner {

    static final Logger LOGGER = LoggerFactory.getLogger(CucumberRunner.class);

    public void runCucumberTests(JavaExec cucumberExec,
                                 FileCollection classpath,
                                 List<String> glueDirs,
                                 List<String> tags,
                                 List<String> formats,
                                 boolean strict,
                                 boolean monochrome,
                                 boolean dryRun,
                                 List<String> featureDirs)
    throws Throwable {
        cucumberExec.classpath(classpath);

        cucumberExec.setMain("cucumber.api.cli.Main");

        List<String> args = new ArrayList<String>();

        if (formats != null) {
            for(String format : formats){
                args.add("--format");
                if ("asyougo".equals(format)) {
                    format = AsYouGoFormatter.class.getName();
                }
                args.add(format);
            }
        }

        if (glueDirs != null) {
            for(String dir : glueDirs){
                args.add("--glue");
                args.add(dir);
            }
        }

        if (tags != null) {
            for(String tag : tags){
                args.add("--tags");
                args.add(tag);
            }
        }

        if (strict){
            args.add("--strict");
        }

        if (monochrome){
            args.add("--monochrome");
        }

        if (dryRun){
            args.add("--dry-run");
        }

        if (featureDirs != null) {
        	for(String dir : featureDirs) {
        		args.add(dir);
        	}
        }

        cucumberExec.args(args);
        
        if(LOGGER.isDebugEnabled()){
            logParameters(args);
        }

        cucumberExec.exec();
	  }

    private void logParameters(List<String> args){
        LOGGER.debug("Cucumber runner args: ");
        for (String arg:args){
            LOGGER.debug(arg + ", ");
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        LOGGER.debug("Cucumber class path entries: ");
        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
            LOGGER.debug(url.getFile());
        }
    }
}
