package me.champeau.gradle

import fj.F
import fj.P1
import fj.Unit
import fj.data.IO
import fj.data.IOFunctions
import fj.data.Option
import fj.data.Validation
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

import java.nio.file.Files
import java.util.regex.Pattern

import static fj.data.IOFunctions.bind
import static fj.data.Option.none
import static fj.data.Option.some

/**
 * Created by MarkPerry on 21/06/2014.
 */
class Search {

	static String resourceMatcher(String packagePath) {
		"$packagePath/".replaceAll("\\.", "/")
	}

	static IO<Set<String>> findResources(String fullPackageName) {
		{ ->
			def cb = new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(fullPackageName)).setScanners(new ResourcesScanner())
			new Reflections(cb).getResources(Pattern.compile(".*"));
		} as IO
	}

	static IO<fj.data.List<Validation<String, Long>>> writeAll(String packagePath, File base) {
		IOFunctions.join(IOFunctions.map(findResources(packagePath), { Set<String> set ->
			fj.data.List<String> list = fj.data.List.list((String[]) set.toArray());
			IOFunctions.traverse(list, { String s ->
				writeResource(s, base, packagePath)
			} as F)
		} as F))
	}

	static IO<Long> writeFile(InputStream is, File f) {
		{ -> Files.copy(is, f.toPath()) } as IO
	}

	static IO<Long> writeStream(InputStream is,
//								String sub,
								File f) {
//		def io = IOFunctions.<Long>unit { ->
////			def f = new File(base, sub)
//			f.getParentFile().mkdirs()
//			f
//		} //as IO<Long>
//		bind(io) { File f ->
//			writeFile(is, f)
//		}
				bind(IOFunctions.<Unit>unit({ ->
					f.getParentFile().mkdirs()
					Unit.unit()

				} as P1), { Unit u ->
					writeFile(is, f)
				} as F)
	}

	static IO<Validation<String, Long>> writeResource(String resourcePath, File base, String packagePath) {
		def resourcePrefix = resourceMatcher(packagePath)
		def b = resourcePath.startsWith(resourcePrefix)
		if (!b) {
			IOFunctions.unit(Validation.fail("Resource $resourcePath does not match the prefix: $resourcePrefix"))
		} else {
			def sub = resourcePath.substring(resourcePrefix.length())
			def io = { ->
				def is = ClasspathHelper.getResourceAsStream("/$resourcePath")
				is
			} as IO
			def io2 = bind(io, { InputStream is ->
				writeStream(is, new File(base, sub))
			} as F)
			IOFunctions.map(io2, { Long item ->
				Validation.success(item)
			} as F)


		}
	}


}

