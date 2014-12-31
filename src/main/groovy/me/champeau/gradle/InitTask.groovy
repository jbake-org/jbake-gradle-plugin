package me.champeau.gradle

import fj.F
import fj.data.Validation
import groovy.transform.TypeChecked
import org.gradle.api.DefaultTask
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by mperry on 13/06/2014.
 *
 * Writes all resources from the package named <code>packageName</code> into <code>root</code>.  Currently just
 * uses the fidbake template.  The JBake project uses a local zip to do this.
 */
@TypeChecked
class InitTask extends DefaultTask {

	String resourcesPackage = "org.jbake.template.resources"
	String template = "fidbake"
	File root = Resources.sourceDir(project)

	@TaskAction
    void init() {
        def list = Search.writeAll(fullPackage(resourcesPackage, template), root).run()
		list.filter({ Validation v -> v.isFail() } as F).each { Validation<String, Long> v ->
			def s2 = "${v.fail()}"
			println s2
		}
    }

	static String fullPackage(String basePackage, String subPackage) {
		"$basePackage.$subPackage"
	}

}
