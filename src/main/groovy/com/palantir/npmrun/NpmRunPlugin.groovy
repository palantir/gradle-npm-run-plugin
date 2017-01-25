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
import org.gradle.api.Plugin
import org.gradle.api.Project

class NpmRunPlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = "npmRun"
    public static final String GROUP_NAME = "NPM Run"

    @Override
    void apply(Project project) {

        project.plugins.apply(NodePlugin.class)

        NpmRunExtension extension = project.extensions.create(EXTENSION_NAME, NpmRunExtension)

        project.afterEvaluate {
            project.task("clean") {
                group = GROUP_NAME
                description = "Runs 'npm run clean'"

                dependsOn "npmSetup"
                dependsOn "npmInstall"
                dependsOn "npm_run_${extension.clean}"

                mustRunAfter "npmSetup"
                mustRunAfter "npmInstall"
            }

            project.task("test") {
                group = GROUP_NAME
                description = "Runs 'npm run test'"

                dependsOn "npmSetup"
                dependsOn "npmInstall"
                dependsOn "npm_run_${extension.test}"

                mustRunAfter "npmSetup"
                mustRunAfter "npmInstall"
                mustRunAfter "clean"
            }

            project.task("check") {
                group = GROUP_NAME
                description = "Depends on ':test'"

                dependsOn "test"
            }

            project.task("build") {
                group = GROUP_NAME
                description = "Runs 'npm run build' and depends on ':check'"

                dependsOn "npmSetup"
                dependsOn "npmInstall"
                dependsOn "check"
                dependsOn "npm_run_${extension.build}"

                mustRunAfter "npmSetup"
                mustRunAfter "npmInstall"
                mustRunAfter "clean"
                mustRunAfter "check"
            }

            project.task("buildDev") {
                group = GROUP_NAME
                description = "Runs 'npm run buildDev' and depends on ':check'"

                dependsOn "npmSetup"
                dependsOn "npmInstall"
                dependsOn "check"
                dependsOn "npm_run_${extension.buildDev}"

                mustRunAfter "npmSetup"
                mustRunAfter "npmInstall"
                mustRunAfter "clean"
                mustRunAfter "check"
            }
        }
    }
}
