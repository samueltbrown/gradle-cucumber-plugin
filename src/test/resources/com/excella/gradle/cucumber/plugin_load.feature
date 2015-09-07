Feature: The Cucumber plugin should load and enrich the Gradle build

  Scenario: The Cucumber plugin should declare a task
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for runtime
    When I list tasks
    Then I should see a "cucumber" task

  Scenario: The Cucumber plugin should auto-wire src/cucumber as a source set
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for compile
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
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for compile
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
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for compile
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

  Scenario: The cucumber compile classpath should include main/test classes and resources
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for compile
    And I create a "src/cucumber" directory
    And I add the following task
      """
      task printCompileClasspath << {
        sourceSets.cucumber.compileClasspath.files.each { println it.path }
      }
      """
    When I successfully run Gradle with "-q printCompileClasspath"
    Then I should see a "(.*/)?build/classes/main" line
    And I should see a "(.*/)?build/resources/main" line
    And I should see a "(.*/)?build/classes/test" line
    And I should see a "(.*/)?build/resources/test" line

  Scenario: The cucumber runtime classpath should include main/test/cucumber classes and resources
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for compile
    And I create a "src/cucumber" directory
    And I add the following task
      """
      task printRuntimeClasspath << {
        sourceSets.cucumber.runtimeClasspath.files.each { println it.path }
      }
      """
    When I successfully run Gradle with "-q printRuntimeClasspath"
    Then I should see a "(.*/)?build/classes/main" line
    And I should see a "(.*/)?build/resources/main" line
    And I should see a "(.*/)?build/classes/test" line
    And I should see a "(.*/)?build/resources/test" line
    And I should see a "(.*/)?build/classes/cucumber" line
    And I should see a "(.*/)?build/resources/cucumber" line

  Scenario: The "cucumberClasses" task should depend on the "testClasses" task
    Given I have a new Gradle project (wrapper v2.1) using Cucumber v1.1.6 for compile
    And I create a "src/cucumber" directory
    And I add the following task
      """
      def printAllTaskDependencies(Task task) {
        task.taskDependencies.getDependencies(task).each {
          println it.name
          printAllTaskDependencies(it)
        }
      }

      task printCucumberClassesDependsOn << {
        def cucumberClasses = project.tasks['cucumberClasses']
        printAllTaskDependencies(cucumberClasses)
      }
      """
    When I successfully run Gradle with "-q printCucumberClassesDependsOn"
    Then I should see a "testClasses" line
