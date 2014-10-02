Feature: Cucumber DSL and its closure

  Background:
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.8 for compile

  @current
  Scenario: The context of the "cucumber" DSL should be a Cucumber task
    Given I add the following task
    """
      cucumber {
          println "'it' is ${it}"
      }
      """
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
    When I successfully run Gradle with ""
    Then I should see "'it' is task ':cucumber'"
