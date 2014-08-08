package me.champeau.gradle

import groovy.transform.TypeChecked
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction

import static me.champeau.gradle.Resources.outputDir

/**
 * Created by mperry on 18/06/2014.
 */
@TypeChecked
class ServerTask extends AbstractTask {

    @TaskAction
    void server() {
        org.jbake.launcher.Main.main(["-s", "${outputDir(project)}"] as String[])
    }

}
