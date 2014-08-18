package me.champeau.gradle

import com.github.mperry.watch.FileMonitor
import com.github.mperry.watch.Util
import fj.F
import fj.Unit
import groovy.transform.TypeChecked
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.WatchEvent
import java.nio.file.attribute.BasicFileAttributes

import static fj.Unit.unit

/**
 * Created by mperry on 18/08/2014.
 */
@TypeChecked
class Refresh {

    static Logger log = LoggerFactory.getLogger(Refresh.class)

    static void processAsync(F<Unit, Unit> f) {
        log.info("Starting async refresh...")
        new Thread({ -> process(f)}).start()
    }

    static void process(F<Unit, Unit> f) {
        def dir = new File("src/jbake")
        log.info("Watching root dir: ${dir.canonicalPath}")
        registerAll(dir, f)
    }

    static void registerAsync(File dir, F<Unit, Unit> f) {
        // TODO: need to do this on a thread pool to limit the number of threads
        new Thread({ -> register(dir, f)}).start()
    }

    static void register(File dir, F<Unit, Unit> f) {
        log.info("Registering ${dir.canonicalPath}...")
        def p = FileMonitor.streamEvents(dir, FileMonitor.ALL_EVENTS)
        def s = p._3()._1()
        s.foreach({ WatchEvent<Path> we ->
            Util.printWatchEvent(we)
            f.f(unit())
            unit()
        } as F) //as F<WatchEvent<Path>, Unit>)
    }

    static void registerAll(File dir, F<Unit, Unit> f) throws IOException {
        dir.eachFileRecurse { File file ->
            if (file.isDirectory()) {
                registerAsync(file, f)
            }

        }
    }

}
