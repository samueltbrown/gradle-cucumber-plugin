package com.excella.gradle.cucumber;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import org.gradle.api.GradleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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

    public void runCucumberTests(ClassLoader cucumberClassLoader, List<String> glueDirs, List<String> tags, List<String> formats,
                                 boolean strict, boolean monochrome, boolean dryRun, List<String> featureDirs) throws Throwable {
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
            args.add("--tags");
            for(String tag : tags){
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
        
        if(LOGGER.isDebugEnabled()){
            logParameters(args);
        }

		mainRun(cucumberClassLoader, args);
	}

	private void mainRun(ClassLoader cucumberClassloader, List<String> args) throws Exception {

		// annoyingly, even though we create stuff here with the provided
		// class loader, AND provide it to the cucumber runtime, the
		// runtime doesn't pass it on to the backends that it creates that
		// need a classloader such as the java and groovy
		// backends - they end up grabbing it from the thread context
		// anyway
		ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(cucumberClassloader);

			// this is basically what is inside cucumber.api.cli.Main.main
			// but that calls System.exit whereas we want to fail the build
			RuntimeOptions runtimeOptions = new RuntimeOptions(args);
			ResourceLoader resourceLoader = new MultiLoader(cucumberClassloader);
			ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, cucumberClassloader);
			Runtime runtime = new Runtime(resourceLoader, classFinder, cucumberClassloader, runtimeOptions);
			runtime.run();
			if(runtime.exitStatus() != 0x0) {
				throw new GradleException("One or more cucumber tests failed, see reports for details");
			}

		}
		finally {
			Thread.currentThread().setContextClassLoader(originalClassLoader);
		}

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
