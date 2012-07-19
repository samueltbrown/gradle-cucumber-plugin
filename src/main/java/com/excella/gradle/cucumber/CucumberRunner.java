package com.excella.gradle.cucumber;

import cucumber.cli.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Mediates between plugin and Cucumber classes
 * 
 * Copyright 2012 Excella Consulting
 */
public class CucumberRunner {

    public void instrument() throws Throwable {
        List<String> args = new ArrayList<String>();

        Main.main(args.toArray(new String[args.size()]));
    }

    public void runCucumberTests(List<String> glueDirs, List<String> tags) throws Throwable {
        List<String> args = new ArrayList<String>();

        cucumber.cli.Main.main(args.toArray(new String[args.size()]));
    }
}