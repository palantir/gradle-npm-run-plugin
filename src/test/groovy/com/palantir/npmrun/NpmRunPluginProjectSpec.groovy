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
        taskName    | dependsOnList                                             | mustRunAfterList
        "clean"     | ["npmSetup", "npmInstall", "npm_run_clean"]               | ["npmSetup", "npmInstall"]
        "test"      | ["npmSetup", "npmInstall", "npm_run_test"]                | ["npmSetup", "npmInstall", "clean"]
        "check"     | ["test"]                                                  | []
        "build"     | ["npmSetup", "npmInstall", "check", "npm_run_build"]      | ["npmSetup", "npmInstall", "clean", "check"]
        "buildDev"  | ["npmSetup", "npmInstall", "check", "npm_run_buildDev"]   | ["npmSetup", "npmInstall", "clean", "check"]
    }
}
