package com.github.mperry

import com.github.mperry.watch.Util
import fj.data.Option
import groovy.transform.TypeChecked
import me.champeau.gradle.Refresh
//import org.apache.log4j.spi.LoggerFactory
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by mperry on 18/08/2014.
 */
@TypeChecked
class Test1 {

//    Logger log = LoggerFactory.logger
    static Logger log = LoggerFactory.getLogger(Test1.class)

    @Test
    void test1() {
        log.info("test1")
        Refresh.registerThreadPerDir(new File("src"), Util.printWatchEvent())
    }

    @Test
    void test2() {
        println "hi"
        log.info("hi2")

        Thread.sleep(2000)
    }

    @Test
    void test3() {

        def base = new File("etc")
        def f = new File(base, "events.txt")
        def f2 = new File(base, "events2.txt")

        println("hi world exist: ${base.exists()} path: ${base.canonicalPath}")
//        if (!base.exists()) {
        if (false) {
            def b = base.mkdirs()
            println("${base.canonicalPath} b: $b")
//            base.createNewFile()
        }
        if (!f.exists()) {
//        if (false) {

            base.mkdirs()
//            println(f.canonicalPath)
//            f.mkdirs()
            f.createNewFile()
            f2.createNewFile()
        }

        Refresh.registerOnThread(base, Util.printWatchEvent())
        log.info("Do file changes...")



        Util.generateEvents(10, f, Option.some(500))
        Util.generateEvents(10, f2, Option.some(500))
//        Thread.sleep(5000)
        // adfasadfdsafsdgdsfgfsdasdffadasdfasfdfaaasdfsdfadsfadfasdf
    }

}



