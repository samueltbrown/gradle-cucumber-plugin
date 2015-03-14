Feature: Ignoring failures from "cucumber" task runner

  Scenario: Successful test has successful build with ignoreFailures as false
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.2.2 for compile
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
        ignoreFailures = false
      }
      """
    When I run Gradle with "cucumber"
    Then I should see "1 Scenarios (1 passed)"
    And I should see "2 Steps (2 passed)"
    And I should see "BUILD SUCCESSFUL"

  Scenario: Successful test has successful build with ignoreFailures as true
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.2.2 for compile
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
        ignoreFailures = true
      }
      """
    When I run Gradle with "cucumber"
    Then I should see "1 Scenarios (1 passed)"
    And I should see "2 Steps (2 passed)"
    And I should see "BUILD SUCCESSFUL"

  Scenario: Unsuccessful test has failed build with ignoreFailures as false
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.2.2 for compile
    And I declare the dependency "testCompile 'junit:junit:4.11'"
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
      import static org.junit.Assert.assertFalse;

      public class MyStepDefinitions {
        @Given("^precondition A$")
        public void precondition_A() throws Throwable {
        }

        @Then("^assertion B$")
        public void assertion_B() throws Throwable {
          assertFalse(true);
        }
      }
      """
    And I add the following task
    """
      cucumber {
        ignoreFailures = false
      }
      """
    When I run Gradle with "cucumber"
    Then I should see "1 Scenarios (1 failed)"
    And I should see "2 Steps (1 failed, 1 passed)"
    And I should see "BUILD FAILED"

  Scenario: Unsuccessful test has successful build with ignoreFailures as true
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.2.2 for compile
    And I declare the dependency "testCompile 'junit:junit:4.11'"
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
      import static org.junit.Assert.assertFalse;

      public class MyStepDefinitions {
        @Given("^precondition A$")
        public void precondition_A() throws Throwable {
        }

        @Then("^assertion B$")
        public void assertion_B() throws Throwable {
          assertFalse(true);
        }
      }
      """
    And I add the following task
    """
      cucumber {
        ignoreFailures = true
      }
      """
    When I run Gradle with "cucumber"
    Then I should see "1 Scenarios (1 failed)"
    And I should see "2 Steps (1 failed, 1 passed)"
    And I should see "BUILD SUCCESSFUL"
