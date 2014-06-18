package me.champeau.gradle

import org.gradle.api.Project

/**
 * Created by mperry on 18/06/2014.
 */
class Resources {

    static File sourceDir(Project p) {
        sourceDir(p.projectDir)
    }

    static File outputDir(Project p) {
        outputDir(p.buildDir)
    }

    static File sourceDir(File f) {
        new File("$f/src/jbake")
    }

    static File outputDir(File f) {
        new File("$f/jbake")
    }

    static Boolean deleteDirOrThrow(File f) {
        def b = f.deleteDir()
        if (!b) {
            throw new Exception("File $f not deleted")
        }
    }

}
