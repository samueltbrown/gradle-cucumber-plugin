package com.excella.gradle.cucumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Mediates between plugin and Cucumber classes
 * 
 * Copyright 2012 Excella Consulting
 */
public class CucumberRunner {

    public void runCucumberTests(List<String> glueDirs, List<String> tags, List<String> formats,
                                 boolean strict, boolean monochrome, boolean dryRun) throws Throwable {
        List<String> args = new ArrayList<String>();

        if (glueDirs != null) {
            args.add("--glue");
            for(String dir : glueDirs){
                args.add(dir);
            }
        }

        if (tags != null) {
            args.add("--tags");
            for(String tag : tags){
                args.add(tag);
            }
        }

        if (formats != null) {
            for(String format : formats){
                args.add("--format");
                args.add(format);
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

        cucumber.cli.Main.main(args.toArray(new String[args.size()]));
    }

}