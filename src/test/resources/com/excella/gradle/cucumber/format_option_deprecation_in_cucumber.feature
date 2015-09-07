Feature: format is deprecated in cucumber 1.2.+

  Scenario: The Cucumber should run successfully without a warning about --format option change using v0.8 and cucumber 1.1.6
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for compile
    And I configure v0.8 of the Cucumber plugin from jcenter
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
    And I shouldn't see "WARNING: Cucumber-JVM's --format option is deprecated. Please use --plugin instead."

  Scenario: The Cucumber should run successfully without a warning about --format option change using latest build and cucumber 1.1.6
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for compile
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
    And I shouldn't see "WARNING: Cucumber-JVM's --format option is deprecated. Please use --plugin instead."

  Scenario: The Cucumber should run successfully but with a warning about --format option change using v0.8 and cucumber 1.2.2
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.2.2 for compile
    And I configure v0.8 of the Cucumber plugin from jcenter
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
    And I should see "WARNING: Cucumber-JVM's --format option is deprecated. Please use --plugin instead."

  Scenario: The Cucumber should run successfully without a warning about --format option change using latest build and cucumber 1.2.+
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
    When I run Gradle with "cucumber"
    Then it should succeed
    And I shouldn't see "WARNING: Cucumber-JVM's --format option is deprecated. Please use --plugin instead."
