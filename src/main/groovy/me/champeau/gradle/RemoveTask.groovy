package me.champeau.gradle

import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by mperry on 18/06/2014.
 */
class RemoveTask extends AbstractTask {

    @TaskAction
    void clean() {
        def f = Resources.sourceDir(project)
        def b = f.deleteDir()
        if (!b) {
            throw new Exception("File $f not deleted")
        }
    }


}
