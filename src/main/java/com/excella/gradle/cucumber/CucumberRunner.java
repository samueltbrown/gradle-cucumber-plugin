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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                if(cucumberVersionAbove1_2(classpath)) args.add("--plugin");
                else args.add("--format");
                if ("asyougo".equals(format)) {
                    // deprecated because Cucumber now runs in a different process,
                    // which does not include this plugin in its classpath
                    LOGGER.warn("'asyougo' formatter deprecated, reverting to 'pretty'");
                    format = "pretty";
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

    private boolean cucumberVersionAbove1_2(FileCollection classpath){
        Pattern cucumber = Pattern.compile("cucumber-(?!html)([^-]*?)-((\\d+)\\.(\\d+)(\\.(\\d+))*)\\.jar");
        for(File f : classpath.getFiles()){
            Matcher matcher = cucumber.matcher(f.getName());
            if(matcher.matches()) {
                LOGGER.debug("{} : {}", f.getName(),matcher.group(2));
                if(Integer.parseInt(matcher.group(3)) >= 1 && Integer.parseInt(matcher.group(4)) >= 2){
                    return true;
                }
            }
        }




        return false;
    }
}
