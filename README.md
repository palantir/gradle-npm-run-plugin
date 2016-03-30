gradle-npm-run-plugin
=====================
[![Circle CI](https://circleci.com/gh/palantir/gradle-npm-run-plugin.svg?style=shield&circle-token=012397ab761aa8f59276b2ac7babb51d80a12a7c)](https://circleci.com/gh/palantir/gradle-npm-run-plugin)

A Gradle Plugin to create lifecycle tasks that trigger `npm run` commands.

Usage
-----
1. Apply the plugin.
2. Configure your `package.json` `scripts` block.

This will allow you to have a consistent Gradle task interface between your NPM + Java projects. You should be able to
run commands like the following:

```bash
./gradlew build -x check
```

It will build your NPM package without running the tests.


Tasks
-----
The following tasks are added:

- `clean` - Runs `npm run clean`
- `test` - Runs `npm run test`
- `check` - Depends on `:test`
- `build` - Runs `npm run build` and depends on `:check`. Builds the production-ready version of the assets.
- `buildDev` - Runs `npm run buildDev` and depends on `:check`. Builds the development-mode version of the assets.


Configuration
-------------
You can configure the `npm run *` commands in your `build.gradle`. Here's an example:

```groovy
npmRun {
    clean       "other-clean"       // defaults to "clean"
    test        "other-test"        // defaults to "test"
    build       "other-build"       // defaults to "build"
    buildDev    "other-buildDev"    // defaults to "buildDev"
}
```


Contributing
------------
Before working on the code, if you plan to contribute changes, please read the [CONTRIBUTING](CONTRIBUTING.md) document.


License
-------
This project is made available under the [Apache 2.0 License][license].


[gradle-node-project]: https://github.com/srs/gradle-node-plugin
[license]: http://www.apache.org/licenses/LICENSE-2.0
