Feature: The cucumber plugin and old versions of gradle

  Scenario Outline: The cucumber plugin will no longer work with versions of gradle below 2.0
    Given I have a new Gradle project (wrapper v<gradle version>) using Cucumber v<cucumber version> for compile
    And I configure v<plugin version> of the Cucumber plugin from <repository>
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
    When I run Gradle with "cucumber"
    Then I should see "Cucumber plugin only works with gradle version above 2.0 due to groovy version in older gradle versions"

  # "dev" repository is for the current code off of this project
  # (v0.6 tests fail on jdk 1.6, because they were compiled for jdk 1.7 - bug fixed since then -)
    Examples:
      | repository   | plugin version | gradle version | cucumber version |
      | dev          | dev            |  1.12          | 1.2.2            |
      | dev          | dev            |  1.12          | 1.2.+            |
