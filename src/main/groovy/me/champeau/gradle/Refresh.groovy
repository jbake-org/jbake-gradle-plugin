package me.champeau.gradle

import com.github.mperry.watch.FileMonitor
import com.github.mperry.watch.Util
import fj.F
import fj.P1
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import static fj.Unit.unit

/**
 * Created by mperry on 18/08/2014.
 */
@TypeChecked
class Refresh {

    static Logger log = LoggerFactory.getLogger(Refresh.class)

    static ExecutorService exectorService = Executors.newWorkStealingPool(3)
    static File DEFAULT_DIR = new File("src/jbake")
//    Executors.newFixedThreadPool(4)

    static void processAsync(F<WatchEvent<Path>, Unit> f) {
        log.info("Starting async refresh...")
        new Thread({ -> process(f)}).start()
    }

    static void process(F<WatchEvent<Path>, Unit> f) {
        registerThreadPerDir(DEFAULT_DIR, f)
    }

    static void registerAsync(File dir, F<WatchEvent<Path>, Unit> f) {
        new Thread({ -> register(dir, f)}).start()
    }

    static void registerWithPool(File dir, F<WatchEvent<Path>, Unit> f) {
        exectorService.submit({ -> register(dir, f)} as Runnable)
    }


    static void register(File dir, F<WatchEvent<Path>, Unit> f) {
        log.info("Registering ${dir.canonicalPath}...")

        def p = FileMonitor.streamEvents(dir, FileMonitor.ALL_EVENTS)
        def s = p._3()._1()
        processStream(p._3(), f)
    }

    static void processStreamAsync(P1<Stream<WatchEvent<Path>>> p, F<WatchEvent<Path>, Unit> f) {
        new Thread({ -> processStream(p, f)}).start()
    }

    static void processStream(P1<Stream<WatchEvent<Path>>> p, F<WatchEvent<Path>, Unit> f) {
        p._1().foreach({ WatchEvent<Path> we ->
            Util.printWatchEvent(we)
            f.f(we)
            unit()
        } as F) //as F<WatchEvent<Path>, Unit>)
    }

    static void register(File dir, F<WatchEvent<Path>, Unit> f, WatchService ws, WatchKey wk) {
        log.info("Registering ${dir.canonicalPath}...")

        def p = FileMonitor.streamEvents(dir, FileMonitor.ALL_EVENTS)
        def s = p._3()._1()
        s.foreach({ WatchEvent<Path> we ->
            Util.printWatchEvent(we)
            f.f(we)
            unit()
        } as F) //as F<WatchEvent<Path>, Unit>)
    }

    static F<Unit, Unit> EMPTY = { WatchEvent<Path> u -> unit() } as F

    static void registerThreadPerDir(File dir, F<WatchEvent<Path>, Unit> f) throws IOException {
        log.info("Registering with root dir: ${dir.canonicalPath}")
        def i = 0
        def ws = FileSystems.getDefault().newWatchService()

        dir.eachFileRecurse { File file ->
            if (file.isDirectory()) {
                i++
                log.info("Submitting $i: ${dir.canonicalPath}")
                registerAsync(file, f)
//                registerWithPool(file, f)
            }

        }

    }

    static void registerOnThread(F<WatchEvent<Path>, Unit> f) throws IOException {
        registerOnThread(DEFAULT_DIR, f)
    }


    static void registerOnThread(File dir, F<WatchEvent<Path>, Unit> f) throws IOException {
        log.info("Registering with root dir: ${dir.canonicalPath}")
        def i = 0
        def ws = FileSystems.getDefault().newWatchService()
        WatchKey key = FileMonitor.register(dir, FileMonitor.ALL_EVENTS, ws)._2()
        dir.eachFileRecurse { File file ->
            if (file.isDirectory()) {
                i++
                log.info("Submitting $i: ${file.canonicalPath}")
//                registerAsync(file, f)
//                registerWithPool(file, f)
                def p2 = FileMonitor.register(file, FileMonitor.ALL_EVENTS, ws)
                key = p2._2()

            }

        }
        def ps = FileMonitor.streamEvents(ws, key)
        processStreamAsync(ps, f)
    }


}
