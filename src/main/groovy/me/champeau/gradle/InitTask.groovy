package me.champeau.gradle

import org.apache.commons.configuration.CompositeConfiguration
import org.apache.commons.configuration.MapConfiguration
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jbake.app.Oven
import org.jbake.launcher.Main

/**
 * Created by mperry on 13/06/2014.
 */
class InitTask extends AbstractTask {

    @TaskAction
    void init() {
        Main.main(["-i"] as String[])
    }

}