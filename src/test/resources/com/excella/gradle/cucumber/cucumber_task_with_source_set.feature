Feature: The "cucumber" task should should execute acceptance tests declared under "src/cucumber"

  Background:
    Given I have a new Gradle project (wrapper v1.7) using Cucumber v1.1.5 for compile
    And I create a "src/cucumber" directory

  Scenario Outline: A feature file with empty step definitions
    Given I write "<feature path>" as follows
      """
      Feature: Feature Name

        Scenario: Scenario Uno
          Given precondition A
          Then assertion B
      """
    And I write "<class path>" as follows
      """
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
    Then I should see a "1 Scenarios (1 passed)" line
    And I should see a "2 Steps (2 passed)" line

    Examples:
      | feature path                             | class path                                      |
      | src/cucumber/resources/my.feature        | src/cucumber/java/MyStepDefinitions.java        |
      | src/cucumber/resources/com/my/my.feature | src/cucumber/java/com/my/MyStepDefinitions.java |


  Scenario: Step definitions depending on test and regular classes
    Given I write "src/cucumber/resources/com/my/my.feature" as follows
      """
      Feature: Feature Name

        Scenario: Scenario Uno
          Given precondition A
          Then assertion B
      """
    And I write a main empty class "com.my.main.A"
    And I write a test empty class "com.my.test.B"
    And I write "src/cucumber/java/com/my/MyStepDefinitions.java" as follows
      """
      import cucumber.api.java.en.*;

      public class MyStepDefinitions {
        @Given("^precondition A$")
        public void precondition_A() throws Throwable {
          new com.my.main.A();
        }

        @Then("^assertion B$")
        public void assertion_B() throws Throwable {
          new com.my.test.B();
        }
      }
      """
    When I successfully run Gradle with "cucumber"
    Then I should see a "1 Scenarios (1 passed)" line
    And I should see a "2 Steps (2 passed)" line
