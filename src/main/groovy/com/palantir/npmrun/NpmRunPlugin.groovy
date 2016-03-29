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

                dependsOn "npmInstall"
                dependsOn "npm_run_${extension.clean}"

                mustRunAfter "npmInstall"
            }

            project.task("test") {
                group = GROUP_NAME
                description = "Runs 'npm test'"

                dependsOn "npmInstall"
                dependsOn "npm_run_${extension.test}"

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

                dependsOn "npmInstall"
                dependsOn "check"
                dependsOn "npm_run_${extension.build}"

                mustRunAfter "npmInstall"
                mustRunAfter "clean"
                mustRunAfter "check"
            }

            project.task("buildDev") {
                group = GROUP_NAME
                description = "Runs 'npm run buildDev' and depends on ':check'"

                dependsOn "npmInstall"
                dependsOn "check"
                dependsOn "npm_run_${extension.buildDev}"

                mustRunAfter "npmInstall"
                mustRunAfter "clean"
                mustRunAfter "check"
            }
        }
    }
}
