package com.github.mperry

import com.github.mperry.watch.Util
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
        Refresh.registerAll(new File("src"), Util.printWatchEvent())
    }

    @Test
    void test2() {
        println "hi"
        log.info("hi2")

        Thread.sleep(2000)
    }

}
