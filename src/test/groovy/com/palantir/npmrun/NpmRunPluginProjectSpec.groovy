/*
 * Copyright 2016 Palantir Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.npmrun

import com.moowork.gradle.node.NodePlugin
import nebula.test.PluginProjectSpec
import org.gradle.api.Task

class NpmRunPluginProjectSpec extends PluginProjectSpec {
    @Override
    String getPluginName() {
        return "com.palantir.npm-run"
    }

    def "applies the node plugin"() {
        when:
        project.apply plugin: pluginName

        then:
        project.plugins.getPlugin(NodePlugin.class) != null
    }

    def "extension is created"() {
        when:
        project.apply plugin: pluginName

        then:
        project.extensions.findByName(NpmRunPlugin.EXTENSION_NAME) != null
    }

    def "tasks built with dependsOn and mustRunAfter"() {
        when:
        project.apply plugin: pluginName
        project.evaluate()

        Task task = project.tasks.getByName(taskName)

        then:
        dependsOnList.every { task.dependsOn.collect { it.toString() }.contains(it) }
        mustRunAfterList.every { task.mustRunAfter.values.contains(it) }

        where:
        taskName    | dependsOnList                                 | mustRunAfterList
        "npmClean"  | ["npmInstall", "npm_run_clean"]               | ["npmInstall"]
        "test"      | ["npmInstall", "npm_run_test"]                | ["npmInstall", "npmClean"]
        "check"     | ["test"]                                      | []
        "build"     | ["npmInstall", "check", "npm_run_build"]      | ["npmInstall", "npmClean", "check"]
        "buildDev"  | ["npmInstall", "check", "npm_run_buildDev"]   | ["npmInstall", "npmClean", "check"]
    }
}
