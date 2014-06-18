package me.champeau.gradle

import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction

import static me.champeau.gradle.Resources.outputDir

//import static me.champeau.gradle.Resources.*

/**
 * Created by mperry on 18/06/2014.
 */
class ServerTask extends AbstractTask {

    @TaskAction
    void server() {
        org.jbake.launcher.Main.main(["-s", "${outputDir(project)}"] as String[])
    }

}
