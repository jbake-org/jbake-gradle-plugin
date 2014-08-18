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
import org.apache.commons.configuration.CompositeConfiguration
import org.apache.commons.configuration.MapConfiguration
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jbake.app.Oven

import static me.champeau.gradle.Resources.outputDir
import static me.champeau.gradle.Resources.sourceDir

@TypeChecked
class JBakeTask extends AbstractTask {
    @InputDirectory File input = sourceDir(project)
    @OutputDirectory File output = outputDir(project)
    @Input Map<String, Object> configuration = [:]
    boolean clearCache = false

    @TaskAction
    void bake() {
        bake(input, output, clearCache, configuration)
    }

    static void bake(File into, File out, Boolean clear, Map<String, Object> conf) {
        new Oven(into, out, clear).with {
            config = new CompositeConfiguration([new MapConfiguration(conf), config])
            setupPaths()
            bake()
        }
    }

}
