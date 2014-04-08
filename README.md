# Gradle Cucumber Plugin

![Cucumber Logo] (https://a248.e.akamai.net/camo.github.com/d716f18fbcff34df385b5fcbdbfa21588e09aed4/687474703a2f2f63756b65732e696e666f2f696d616765732f637563756d6265725f6c6f676f2e706e67)

The gradle cucumber plugin provides the ability to run [cucumber](http://cukes.info) acceptance tests directly
from a gradle build.  The plugin utilizes the cucumber cli provided by the [cucumber-jvm](https://github.com/cucumber/cucumber-jvm) project
and should support any of the languages utilized in cucumber-jvm.

(Currently only tested with Java, Groovy, and JRuby more coming soon!)

## Contributors

 * [Samuel Brown] (https://github.com/samueltbrown)
 * [Matthew Lucas] (https://github.com/lucas1000001)
 * [Philippe Vosges] (https://github.com/viphe)

## Using the plugin in your gradle build script

You can apply the plugin using the following ```buildscript```:

      apply plugin: 'cucumber'

      buildscript {
          repositories {
              mavenCentral()
          }

          dependencies {
              classpath 'com.github.samueltbrown:gradle-cucumber-plugin:0.5'
          }
      }

Older versions can be downloaded directly from GitHub like so;

      buildscript {
          apply from: 'https://github.com/samueltbrown/gradle-cucumber-plugin/raw/master/repo/gradle-cucumber-plugin/gradle-cucumber-plugin/0.3/cucumberinit.gradle'
      }

Currently the version is set at <b>0.4.1</b> in the link but this can be updated to the latest version as it becomes available.

### Running the Tests only

Once the plugin has been applied, the project dependencies need to be updated with the cucumber-jvm jar file needed for
your language.  Below 'groovy' is the chosen language.

      dependencies {

        ...

      	cucumberRuntime 'info.cukes:cucumber-groovy:1.1.5'

      }

### Building and Running the Tests

If you have a ```src/cucumber``` source set (similar to ```src/test```), the plugin will automatically detect it and
setup Java tasks and configurations for you. The "cucumber" code unit depends on "test", the same way "test" depends on
"main". Also, choose your library dependencies:

      dependencies {

      	cucumberCompile 'info.cukes:cucumber-groovy:1.1.5'

      }

Write your feature files under ```src/cucumber/resources```.

## Available Tasks

Currently the plugin only supports one task to run your cucumber tests:

      >gradle cucumber

## Cucumber Task Configuration

The cucumber task has several configurable properties:

* `formats`: A list of output formats. (Defaults to <b>pretty</b>)
* `glueDirs`: A list of directories where stepdefs and supporting code are located (Defaults to <b>src/test/java</b>)
* `featureDirs`: A list of directories where feature files are located.(Defaults to <b>src/test/resources</b>)
* `monochrome`: A boolean value indicating if console output should be one color. (Defaults to <b>false</b>)
* `strict`: A boolean value indicating whether scenarios should be evaluated strictly. (Defaults to <b>false</b>)
* `dryRun`: A boolean value indicating whether scenarios should be run as a dry run. (Defaults to <b>false</b>)

### Example task configuration

    cucumber {
        formats = ['pretty','json:build/cucumber.json','junit:build/cucumber.xml']
        glueDirs = ['src/test/resources/env',
                    'src/test/resources/support',
                    'src/test/resources/step_definitions']
        featureDirs = ['src/test/resources/features']
        tags = ['billing', 'important']
        monochrome = false
        strict = false
        dryRun = false
    }

#### "asyougo" Formatter

The "asyougo" formatter is a hacked "pretty" formatter, which displays scenario lines as they are evaluated.

    cucumber {
        formats = ['asyougo']
    }

## Prerequisites 

You must use cucumber version <b>1.1.5</b> or higher.

## Coming Soon

* Use of tags
* Automatic dependency retrieval and default glue dirs for each jvm language
* Simplified task configuration
* Command-line arguments to override task configuration


## Contributing

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

