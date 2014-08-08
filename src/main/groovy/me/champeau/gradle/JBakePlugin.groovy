/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.champeau.gradle

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.gradle.api.Plugin
import org.gradle.api.Project

import static fj.P.p

@TypeChecked
class JBakePlugin implements Plugin<Project> {

    @TypeChecked(TypeCheckingMode.SKIP)
    void apply(Project project) {
        def tasks = [
                p("jbake", JBakeTask), p("jbakeInit", InitTask), p("jbakeServer", ServerTask),
                p("jbakeRemove", RemoveTask), p("jbakeClean", CleanTask)
        ]
        tasks.each {
            project.task(it._1(), type: it._2())
        }
    }

}
