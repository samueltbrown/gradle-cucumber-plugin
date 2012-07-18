package com.excella.gradle.cucumber;

import java.util.ArrayList;
import java.util.List;

import cucumber.cli.Main;

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

    public void runCucumberTests() throws Throwable {
        List<String> args = new ArrayList<String>();

        cucumber.cli.Main.main(args.toArray(new String[args.size()]));
    }
}