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
 *
 * Writes all resources from the package named <code>packageName</code> into <code>root</code>.  Currently just
 * uses the fidbake template.  The JBake project uses a local zip to do this.
 */
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
        new Reflections(cb).getResources(Pattern.compile(".*"));
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
        if (b) {
            def sub = s.substring(match.length())
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
