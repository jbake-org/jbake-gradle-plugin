package me.champeau.gradle

import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

import java.nio.file.Files
import java.util.regex.Pattern

/**
 * Created by mperry on 13/06/2014.
 */
//@TypeChecked
class InitTask extends AbstractTask {

    @TaskAction
    void init() {
        writeAll()
    }

    String packageName = "org.jbake.template.resources.fidbake"
    String match = "$packageName/".replaceAll("\\.", "/")
    File root = Resources.sourceDir(project)

    Set<String> findResources() {
        def cb = new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(packageName)).setScanners(new ResourcesScanner())
        def r2 = new Reflections(cb)
        r2.getResources(Pattern.compile(".*"));
    }

    void writeAll() {
        writeResources(findResources())
    }

    void writeFile(InputStream is, File f) {
        Files.copy(is, f.toPath())
    }

    void writeStream(InputStream is, String sub) {
        def f = new File(root, sub)
        f.getParentFile().mkdirs()
        writeFile(is, f)
    }

    boolean writeResource(String s) {
        def b = s.startsWith(match)
        println "match: $b matcher: $match"
        if (b) {
            def sub = s.substring(match.length())
            println "resource: $s sub: $sub"
            def is = ClasspathHelper.getResourceAsStream("/$s")
            writeStream(is, sub)
        }
        b
    }

    void writeResources(Set<String> props) {
        props.each { String s ->
            writeResource(s)
        }
    }

}
