package me.champeau.gradle

import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction

import groovy.transform.TypeChecked

import static me.champeau.gradle.Resources.deleteDirOrThrow

/**
 * Created by mperry on 18/06/2014.
 */
@TypeChecked
class CleanTask extends AbstractTask {

    @TaskAction
    void clean() {
        deleteDirOrThrow(project.buildDir)
    }

}
