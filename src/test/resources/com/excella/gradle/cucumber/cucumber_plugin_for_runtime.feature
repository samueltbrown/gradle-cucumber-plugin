Feature: The "cucumber" task should execute tests

  Background:
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for runtime

  Scenario: Java step definitions and Cucumber features in the test source set
    Given I write "src/test/resources/features/com/company/my.feature" as follows
      """
      Feature: Feature Name

        Scenario: Scenario Uno
          Given precondition A
          Then assertion B
      """
    And I write "src/test/java/com/company/MyStepDefinitions.java" as follows
      """
      package com.company;
      import cucumber.api.java.en.*;

      public class MyStepDefinitions {
        @Given("^precondition A$")
        public void precondition_A() throws Throwable {
        }

        @Then("^assertion B$")
        public void assertion_B() throws Throwable {
        }
      }
      """
    And I declare the dependency "testCompile 'info.cukes:cucumber-java:1.1.6'"
    And I declare the dependency "cucumberRuntime sourceSets.test.output"
    And I add the following task
      """
      cucumber {
          formats = ['pretty']
          glueDirs = ['classpath:com.company']
          featureDirs = ['src/test/resources/features']
          monochrome = false
          strict = false
          dryRun = false
      }
      """
    And I add the following task
      """
      task otherTask {
          doLast {
              println 'otherTask running'
          }
      }
      otherTask.mustRunAfter "cucumber"
      """
    When I successfully run Gradle with "testClasses cucumber otherTask"
    Then I should see a "1 Scenarios \(1 passed\)" line
    And I should see a "2 Steps \(2 passed\)" line
    And I should see a "otherTask running" line
