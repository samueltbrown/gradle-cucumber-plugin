Feature: The Cucumber plugin should load and enrich the Gradle build

  Scenario: The Cucumber plugin should declare a task
    Given I have a new Gradle project (wrapper v1.7) using Cucumber v1.1.5 for runtime
    When I list tasks
    Then I should see a "cucumber" task

  Scenario: The Cucumber plugin should wire src/cucumber as a source set
    Given I have a new Gradle project (wrapper v1.7) using Cucumber v1.1.5 for compile
    And I create a "src/cucumber" directory
    And I add the following task
      """
      task printSourceSets << {
        sourceSets.each { println it.name }
      }
      """
    When I run Gradle with "-q printSourceSets"
    Then it should succeed
    And I should see a "cucumber" line

  Scenario: The cucumber compile classpath should extend from the test compile classpath
    Given I have a new Gradle project (wrapper v1.7) using Cucumber v1.1.5 for compile
    And I create a "src/cucumber" directory
    And I add the following task
      """
      task printCompileExtendsFrom << {
        configurations.cucumberCompile.extendsFrom.each { println it.name }
      }
      """
    When I successfully run Gradle with "-q printCompileExtendsFrom"
    Then I should see a "testCompile" line

  Scenario: The cucumber runtime classpath should extend from the cucumber and test runtime classpaths
    Given I have a new Gradle project (wrapper v1.7) using Cucumber v1.1.5 for compile
    And I create a "src/cucumber" directory
    And I add the following task
      """
      task printRuntimeExtendsFrom << {
        configurations.cucumberRuntime.extendsFrom.each { println it.name }
      }
      """
    When I successfully run Gradle with "-q printRuntimeExtendsFrom"
    Then I should see a "cucumberCompile" line
    And I should see a "testRuntime" line

  @current
  Scenario: The cucumber compile classpath should include main and test classes and resources
    Given I have a new Gradle project (wrapper v1.7) using Cucumber v1.1.5 for compile
    And I create a "src/cucumber" directory
    And I add the following task
      """
      task printCompileClasspath << {
        sourceSets.cucumber.compileClasspath.files.each { println it.path }
      }
      """
    When I successfully run Gradle with "-q classes printCompileClasspath"
    Then I should see a "(.*/)?build/classes/main" line
    And I should see a "(.*/)?build/resources/main" line
    And I should see a "(.*/)?build/classes/test" line
    And I should see a "(.*/)?build/resources/test" line
