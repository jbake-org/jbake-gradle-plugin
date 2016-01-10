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

import me.champeau.gradle.tasks.JBakeBuild
import me.champeau.gradle.tasks.JBakeClean
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

class JBakePlugin implements Plugin<Project> {


    public static final String JBAKE = "jbake"
    Project project
    JBakeExtension extension

    void apply(Project project) {
        this.project = project
        project.apply(plugin: 'base')

        project.repositories {
            jcenter()
        }

        Configuration configuration = project.configurations.maybeCreate(JBAKE)
        extension = project.extensions.create(JBAKE, JBakeExtension)

        addDependenciesAfterEvaluate()

        project.task('jbakeBuild', type: JBakeBuild, group: 'Documentation', description: 'Bake a jbake project'){

            classpath = configuration
            conventionMapping.input = { project.file("$project.projectDir/$project.jbake.srcDirName") }
            conventionMapping.output = { project.file("$project.buildDir/$project.jbake.destDirName") }
            conventionMapping.clearCache = { project.jbake.clearCache }
            conventionMapping.configuration = { project.jbake.configuration }
        }

        project.task('jbakeClean', type: JBakeClean, group: 'Documentation', description: 'Delete the build directory')

    }

    def addDependenciesAfterEvaluate() {
        project.afterEvaluate {
            addDependencies()
        }
    }

    def addDependencies() {
        project.dependencies {
            jbake("org.jbake:jbake-core:${extension.version}")

            if ( new Version(extension.version) > new Version("2.3.0") ){
                jbake("org.asciidoctor:asciidoctorj:${extension.asciidoctorjVersion}")
            }
            else {
                jbake("org.asciidoctor:asciidoctor-java-integration:${extension.asciidoctorJavaIntegrationVersion}")
            }

            jbake("org.freemarker:freemarker:${extension.freemarkerVersion}")
            jbake("org.pegdown:pegdown:${extension.pegdownVersion}")
        }
    }

}
