package me.champeau.gradle

import groovy.transform.TypeChecked
import org.gradle.api.DefaultTask
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.nio.file.Path
import java.nio.file.WatchEvent

import static me.champeau.gradle.Resources.outputDir
import static me.champeau.gradle.Resources.sourceDir

/**
 * Created by mperry on 18/06/2014.
 */
@TypeChecked
class ServerTask extends DefaultTask {

    @InputDirectory File input = sourceDir(project)
    @OutputDirectory File output = outputDir(project)
    @Input Map<String, Object> configuration = [:]
    boolean clearCache = false

    @TaskAction
    void server() {
        doRefresh()
        org.jbake.launcher.Main.main(["-s", "${outputDir(project)}"] as String[])
    }

    void doRefresh() {
        Refresh.registerOnThread({ WatchEvent<Path> we ->
            new JBakeTask().bake()
//            JBakeTask.bake(input, output, clearCache, configuration)
        })

    }
}
