package me.champeau.gradle

import fj.F
import fj.Unit
import fj.data.IO
import fj.data.IOFunctions
import fj.data.Option
import fj.data.Validation
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

import java.nio.file.Files
import java.util.regex.Pattern

import static fj.Unit.unit
import static fj.data.IOFunctions.bind
import static fj.data.Option.none
import static fj.data.Option.some

/**
 * Created by mperry on 13/06/2014.
 *
 * Writes all resources from the package named <code>packageName</code> into <code>root</code>.  Currently just
 * uses the fidbake template.  The JBake project uses a local zip to do this.
 */
class InitTask extends AbstractTask {

	String resourcesPackage = "org.jbake.template.resources"
	String template = "fidbake"
//	String resourceMatch = "${fullPackage()}/".replaceAll("\\.", "/")
	File root = Resources.sourceDir(project)


	@TaskAction
    void init() {
        def list = Search.writeAll(fullPackage(resourcesPackage, template), root).run()
//		println "result: $list"
		list.filter({ Validation v ->
			v.isFail()
		} as F).each { Validation<String, Long> v ->
			def s = v.isFail() ? "fail(${v.fail()})" : "success(${v.success()})"
//			println "v: $s"
			def s2 = "${v.fail()}"
			println s2
		}
    }

	static String fullPackage(String basePackage, String subPackage) {
		"$basePackage.$subPackage"
	}


//	IO<Unit> writeResource(String resourcePath, String resourcePrefix, File base) {
//		def sub = resourcePath.substring(resourcePrefix.length())
//		def is = ClasspathHelper.getResourceAsStream("/$resourcePath")
//		writeStream(is, sub, base)
//
//	}
//
//    void writeResources(Set<String> props, File base) {
//        props.each { String s ->
//            writeResource(s, base)
//        }
//    }

}
