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

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

class NpmRunPluginIntegrationSpec extends IntegrationSpec {

    def setup() {
        copyResources("fixtures/package.json", "package.json")
    }

    def "tasks call underlying package.json scripts block"() {
        setup:
        buildFile << """
            apply plugin: "com.palantir.npm-run"
        """.stripIndent()

        when:
        ExecutionResult result = runTasksSuccessfully(runTaskName)

        then:
        result.success
        filesMustExist.every { fileExists(it) }

        where:
        runTaskName | filesMustExist
        "clean"     | ["did.clean"]
        "test"      | ["did.test"]
        "check"     | ["did.test"]
        "build"     | ["did.test", "did.build"]
        "buildDev"  | ["did.test", "did.buildDev"]
    }

    def "override default npm run commands"() {
        setup:
        buildFile << """
            apply plugin: "com.palantir.npm-run"

            npmRun {
                clean       "other-clean"
                test        "other-test"
                build       "other-build"
                buildDev    "other-buildDev"
            }
        """.stripIndent()

        when:
        ExecutionResult result = runTasksSuccessfully(runTaskName)

        then:
        result.success
        filesMustExist.every { fileExists(it) }

        where:
        runTaskName | filesMustExist
        "clean"     | ["other.clean"]
        "test"      | ["other.test"]
        "check"     | ["other.test"]
        "build"     | ["other.test", "other.build"]
        "buildDev"  | ["other.test", "other.buildDev"]
    }

    def "no duplicates"() {
        setup:
        buildFile << """
            apply plugin: "com.palantir.npm-run"
        """.stripIndent()

        when:
        ExecutionResult result = runTasksSuccessfully("clean", "check", "build", "buildDev")

        then:
        result.success
        ["did.clean", "did.test", "did.build", "did.buildDev"].every { fileExists(it) }
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
}
