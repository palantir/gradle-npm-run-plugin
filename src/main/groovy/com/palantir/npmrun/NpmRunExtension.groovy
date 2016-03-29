package com.palantir.npmrun

class NpmRunExtension {
    String clean = "clean"
    String test = "test"
    String build = "build"
    String buildDev = "buildDev"

    void clean(String clean) {
        this.clean = clean
    }

    void test(String test) {
        this.test = test
    }

    void build(String build) {
        this.build = build
    }

    void buildDev(String buildDev) {
        this.buildDev = buildDev
    }
}
