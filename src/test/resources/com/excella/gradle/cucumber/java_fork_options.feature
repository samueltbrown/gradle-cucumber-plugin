Feature: The Cucumber DSL should allow to set Java fork options

  Scenario: Java VM max heap size should be passed to the Cucumber execution
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.8 for compile
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
    And I add the following task
    """
      cucumber {
        jvmOptions {
          maxHeapSize = 1
        }
      }
      """
    When I run Gradle with "cucumber"
    Then I should see "Too small initial heap"


  Scenario: Environment variables should be passed to the Cucumber execution
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.8 for compile
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
          String envVarFromGradle = System.getenv().get("ENV_VAR_FROM_GRADLE");
          if (!"VALUE_FROM_GRADLE".equals(envVarFromGradle)) {
            throw new RuntimeException(
              "expected env var 'ENV_VAR_FROM_GRADLE=VALUE_FROM_GRADLE', got '=" + envVarFromGradle + "'");
          }
        }
      }
      """
    And I add the following task
    """
      cucumber {
        jvmOptions {
          environment 'ENV_VAR_FROM_GRADLE', 'VALUE_FROM_GRADLE'
        }
      }
      """
    Then I successfully run Gradle with "cucumber"
