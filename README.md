# Gradle Cucumber Plugin

![Cucumber Logo] (https://cucumber.io/images/cucumber-logo.svg)

The gradle cucumber plugin provides the ability to run [cucumber](http://cukes.info) acceptance tests directly
from a gradle build.  The plugin utilizes the cucumber cli provided by the [cucumber-jvm](https://github.com/cucumber/cucumber-jvm) project
and should support any of the languages utilized in cucumber-jvm.

(Currently only tested with Java, Groovy, and JRuby more coming soon!)

## Contributors

 * [Samuel Brown] (https://github.com/samueltbrown)
 * [Matthew Lucas] (https://github.com/lucas1000001)
 * [Philippe Vosges] (https://github.com/viphe)
 * [Paul Bellchambers] (https://github.com/pbellchambers)
 * [Simon Wiehe] (https://github.com/klunk)

## Using the plugin in your gradle build script

### From v0.7 on

To use in Gradle 2.1 and later…

```groovy
      plugins {
        id "com.github.samueltbrown.cucumber" version "0.9"
      }
```

To use in earlier versions of Gradle…

```groovy
      buildscript {
        repositories {
          jcenter()
        }
        dependencies {
          classpath "com.github.samueltbrown:gradle-cucumber-plugin:0.9"
        }
      }
      
      apply plugin: "com.github.samueltbrown.cucumber"
```

### Before v0.7

You can apply the plugin using the following ```buildscript```:

      apply plugin: 'cucumber'

      buildscript {
          repositories {
              mavenCentral()
          }

          dependencies {
              classpath 'com.github.samueltbrown:gradle-cucumber-plugin:0.6'
          }
      }

Older versions can be downloaded directly from GitHub like so;

      buildscript {
          apply from: 'https://github.com/samueltbrown/gradle-cucumber-plugin/raw/master/repo/gradle-cucumber-plugin/gradle-cucumber-plugin/0.3/cucumberinit.gradle'
      }

### Running the Tests only

Once the plugin has been applied, the project dependencies need to be updated with the cucumber-jvm jar file needed for
your language.  Below 'groovy' is the chosen language.

      dependencies {

        ...

      	cucumberRuntime 'info.cukes:cucumber-groovy:1.2.2'

      }

### Building and Running the Tests

If you have a ```src/cucumber``` source set (similar to ```src/test```), the plugin will automatically detect it and
setup Java tasks and configurations for you. The "cucumber" code unit depends on "test", the same way "test" depends on
"main". Also, choose your library dependencies:

      dependencies {

      	cucumberCompile 'info.cukes:cucumber-groovy:1.2.2'

      }

Write your feature files under ```src/cucumber/resources```.

## Available Tasks

Currently the plugin only supports one task to run your cucumber tests:

      > gradle cucumber

## Cucumber Task Configuration

The cucumber task has several configurable properties:

* `formats`: A list of output formats. (Defaults to <b>pretty</b>)
* `glueDirs`: A list of directories where stepdefs and supporting code are located (Defaults to <b>src/test/java</b>)
* `featureDirs`: A list of directories where feature files are located.(Defaults to <b>src/test/resources</b>)
* `tags`: a list of the tags of the scenarios to run (`['@a,~@b', '@c']` reads as `(@a OR NOT @b) AND @c`)
* `monochrome`: A boolean value indicating if console output should be one color. (Defaults to <b>false</b>)
* `strict`: A boolean value indicating whether scenarios should be evaluated strictly. (Defaults to <b>false</b>)
* `dryRun`: A boolean value indicating whether scenarios should be run as a dry run. (Defaults to <b>false</b>)
* `jvmOptions {}`: a DSL block configuring the spawned process. Options are the same as for [JavaExec](http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.JavaExec.html).
* `ignoreFailures`: A boolean value indicating whether failures from the cucumber runner should be ignored or not. Defaults to <b>false</b>

### Example task configuration

    cucumber {
        formats = ['pretty','json:build/cucumber.json','junit:build/cucumber.xml']
        glueDirs = ['src/test/resources/env',
                    'src/test/resources/support',
                    'src/test/resources/step_definitions']
        featureDirs = ['src/test/resources/features']
        tags = ['@billing', '@important']
        monochrome = false
        strict = false
        dryRun = false
        ignoreFailures = false
        
        jvmOptions {
          maxHeapSize = '512m'
          environment 'ENV', 'staging'
        }
    }

## Prerequisites 

You must use Cucumber version <b>1.1.6</b> or higher.

## Release Notes

### v0.9

  * Minimum Gradle version is now `2.0`
  * Uses the `--plugin` rather than `--format` arg for the cucumber runner when using cucumber-jvm `1.2.+`
  * Introduces `ignoreFailures` convention for test running.

### v0.8

  * Cucumber is now executed as a separate process using an underlying JavaExec task.

### v0.7.2

  * Fixed support of Java 1.6/1.7

### v0.7.1

  * Dependency on specific version of Cucumber now has a `provided` scope.

### v0.7

  * Minimum Cucumber version is now `1.1.6`.
  * Fixed support of multiple tags.
  * Fixed execution on Windows

### v0.6

  * Windows support           
  * Idea integration: Cucumber source dirs automatically added to modules with test scope

## Coming Soon

* Automatic dependency retrieval and default glue dirs for each jvm language
* Simplified task configuration
* Command-line arguments to override task configuration


## Contributing

As you would expect, clone, push to GitHub and create a pull request for us to review and merge.

Make sure you are using jdk 1.6 when running tests ([jenv](http://jenv.io/) is our friend here).

### Pushing to Maven Central

```sh
  ./gradlew \
    -Psigning.secretKeyRingFile=path/to/ring.gpg \
    -Psigning.keyId=GPG_KEYID \
    -Psigning.password=$GPG_PASS \
    -PsonatypeUsername=$SONATYPE_USER \
    -PsonatypePassword=$SONATYPE_PASS \
    clean uploadArchives
```

It is possible to save some or all of those properties to ```~/.gradle/gradle.properties```.

