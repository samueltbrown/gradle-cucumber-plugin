Feature: The Cucumber plugin and Gradle v1.8

  Scenario: The Cucumber plugin should run without warning with Gradle v1.8
    Given I have a new Gradle project (wrapper v1.8) using Cucumber v1.1.5 for compile
    And I write "src/cucumber/resources/com/my/the.feature" as follows
      """
      Feature: Feature Name

        Scenario: Scenario Uno
          Given precondition A
          Then assertion B
      """
    And I write "src/cucumber/java/com/my/MyStepDefinitions.java" as follows
      """
      package com.my;

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
    When I successfully run Gradle with "cucumber"
    Then I shouldn't see "deprecated"
