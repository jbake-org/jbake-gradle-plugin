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

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction

class ServerTask extends AbstractTask {
    Long port = 8820

    @TaskAction
    void serve() {
        def server = new Server()
        def connector = new SelectChannelConnector()
        connector.port = port
        server.addConnector(connector)

        def resource_handler = new ResourceHandler()
        resource_handler.directoriesListed = true
        resource_handler.welcomeFiles = ['index.html']

        resource_handler.resourceBase = project.tasks.jbake.output

        def context = new ContextHandler()
        context.handler = resource_handler
        context.contextPath = '/'

        def handlers = new HandlerList()
        handlers.handlers = [context, new DefaultHandler()]
        server.handler = handlers

        println("Serving out contents on http://localhost:${connector.port}/");
        println("(To stop server hit CTRL-C)");

        server.start()
        server.join()
    }

}
