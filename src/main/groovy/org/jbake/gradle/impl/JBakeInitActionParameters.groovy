package org.jbake.gradle.impl

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters

interface JBakeInitActionParameters extends WorkParameters {
    Property<String> getTemplate()
    Property<String> getTemplateUrl()
    RegularFileProperty getOutput()
    MapProperty<String, Object> getConfiguration()
}