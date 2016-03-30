package com.palantir.npmrun

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

class NestedNpmRunIntegrationSpec extends IntegrationSpec {

    def setup() {
        copyResources("fixtures/package.json", "package.json")
    }

    def "execute nested npm run commands with global node"() {
        given:
        buildFile << """
            apply plugin: "com.palantir.npm-run"

            npmRun {
                build "nested-build"
            }
        """.stripIndent()

        when:
        ExecutionResult result = runTasksSuccessfully("build")

        then:
        result.success
        fileExists("nested.test")
        fileExists("nested.build")
    }

    def "execute nested npm run commands with local node"() {
        given:
        buildFile << """
            apply plugin: "com.palantir.npm-run"

            node {
                version = "0.10.33"
                npmVersion = "2.1.6"
                download = true
                workDir = file('build/node')
            }

            npmRun {
                build "nested-build"
            }
        """.stripIndent()

        when:
        ExecutionResult result = runTasksSuccessfully("build")

        then:
        result.success
        fileExists("nested.test")
        fileExists("nested.build")
    }
}
