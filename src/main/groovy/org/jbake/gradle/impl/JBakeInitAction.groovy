package org.jbake.gradle.impl

import org.gradle.workers.WorkAction
import org.jbake.app.configuration.JBakeConfigurationFactory

abstract class JBakeInitAction implements WorkAction<JBakeInitActionParameters> {

    Init init = new Init()

    @Override
    void execute() {
        def configuration = new JBakeConfigurationFactory().createDefaultJbakeConfiguration(
                parameters.output.get().asFile,
                parameters.output.get().asFile,
                true
        )
        parameters.configuration.get().each {
            configuration.setProperty(it.key, it.value)
        }
        
        init.config = configuration

        if (parameters.templateUrl.isPresent()) {
            initFromTemplateUrl()
        } else {
            initFromTemplate()
        }
    }

    void initFromTemplate() {
        try {
            def outputDir = parameters.output.get().asFile
            def type = parameters.template.get()
            init.run(outputDir, type)
            println("Base folder structure successfully created.")
        } catch (final Exception e) {
            final String msg = "Failed to initialise structure: " + e.getMessage()
            throw new IllegalStateException(msg, e)
        }
    }

    void initFromTemplateUrl() {
        try {
            def outputDir = parameters.output.get().asFile
            def templateUrl = new URL(parameters.templateUrl.get())
            init.run(outputDir, templateUrl)
            println("Base folder structure successfully created.")
        } catch (final Exception e) {
            final String msg = "Failed to initialise structure: " + e.getMessage()
            throw new IllegalStateException(msg, e)
        }
    }

}