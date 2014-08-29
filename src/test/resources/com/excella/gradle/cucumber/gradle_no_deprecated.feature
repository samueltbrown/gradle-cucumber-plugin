Feature: The Cucumber plugin and Gradle deprecations

  # Unless we automate the retrieval of Gradle latest version, this will have to be updated by hand
  Scenario Outline: The Cucumber plugin should run without warning with Gradle v<gradle version>
    Given I have a new Gradle project (wrapper v<gradle version>) using Cucumber v<cucumber version> for compile
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
    When I successfully run Gradle with "-v"
    Then I should see a "Gradle <gradle version>" line
    When I successfully run Gradle with "cucumber"
    Then I shouldn't see "deprecated"

    Examples:
      | gradle version | cucumber version |
      | 1.12           | 1.1.6            |
      | 1.12           | 1.1.8            |
      | 2.0            | 1.1.6            |
      | 2.0            | 1.1.8            |