package com.excella.gradle.cucumber;

/**
 *
 */

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(tags = {"@current"})
public class CucumberTest {
}
