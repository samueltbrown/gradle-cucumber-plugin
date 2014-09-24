package com.excella.gradle.cucumber

import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.process.internal.JvmOptions

/**
 * DSL extension.
 */
class CucumberJvmOptions extends JvmOptions {

    CucumberJvmOptions() {
        super(new IdentityFileResolver())
    }
}
