package me.champeau.gradle

import groovy.transform.TypeChecked
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction

import static me.champeau.gradle.Resources.deleteDirOrThrow
import static me.champeau.gradle.Resources.sourceDir

/**
 * Created by mperry on 18/06/2014.
 */
@TypeChecked
class RemoveTask extends AbstractTask {

    @TaskAction
    void clean() {
        deleteDirOrThrow(sourceDir(project))
    }

}
