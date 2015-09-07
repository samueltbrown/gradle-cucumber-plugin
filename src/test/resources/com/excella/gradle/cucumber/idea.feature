Feature: Integration with Idea plugin

  Scenario: The Cucumber should run successfully when the Idea plugin is used in the same project
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.8 for compile
    And I configure v0.8 of the Cucumber plugin from jcenter
    And I apply the "idea" builtin plugin
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
    When I run Gradle with "cucumber"
    Then it should succeed
    When I run Gradle with "idea"
    Then it should succeed

  Scenario: The Cucumber should run successfully when the Idea plugin is used in the same project using latest build
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.2.2 for compile
    And I apply the "idea" builtin plugin
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
    When I run Gradle with "cucumber"
    Then it should succeed
    When I run Gradle with "idea"
    Then it should succeed
