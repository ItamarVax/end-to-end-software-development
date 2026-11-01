# End-to-End Software Development

EX1: Workshop Registration, implemented in vanilla Java with JDK 25. No Maven or application frameworks.

## IntelliJ

Open the repository folder. Select a JDK 25 SDK named `25` if prompted. The project has two modules: `model` and `logic`; `logic` depends on `model`. JUnit is test-only.

## Unit tests

```sh
bash scripts/setup-tests.sh
bash scripts/test.sh
```

Setup downloads the pinned JUnit 6.0.0 standalone runner from Maven Central and verifies its SHA-256 hash. `testlib/` is ignored; run setup after cloning. The test command compiles the source and runs all tests under `tests/`.

## Build submission JARs

```sh
bash scripts/build.sh
```

This compiles the model and logic modules separately, without JUnit, and produces:

- `artifacts/workshop-model-impl.jar`
- `artifacts/workshop-logic-impl.jar`

The JARs contain production classes only. Keep the supplied `model-1.0.jar`, `logic-1.0.jar`, and `workshop-tester.jar` under `lib/`; they are not bundled into the implementation JARs.

## Lecturer acceptance test

```sh
bash scripts/acceptance-test.sh
```

This rebuilds the implementation JARs and runs the supplied tester using only those JARs and the supplied dependencies, without test classes or JUnit. Success ends with `RESULT: ALL OK` and exit status 0.

All scripts use `JAVA_HOME` if set; otherwise they check Homebrew JDK 25, then macOS `java_home`. Select JDK 25 explicitly if your terminal defaults to another Java version.

## Design and input policy

- Person data is shared by an abstract base class. Students derive team information through their team reference.
- Model methods coordinate relationship updates. Instructor reassignment is allowed; repeated association is a no-op.
- Collection getters return mutable shallow copies. Order is unspecified.
- Unknown or null lookup IDs return null for single objects and empty lists for collection queries.
- Required command arguments reject null. Duplicate IDs, invalid team members, empty teams, and foreign-system objects are rejected with IllegalArgumentException before changes are applied.
- Failed operations do not consume numeric IDs. Blank strings and email formats have no additional validation.

## EX1 submission

The repository must be public and contain implementation JARs in root `artifacts/`. Tag the submitted commit exactly `EX1-Vanilla`, then submit a text file containing only the GitHub repository URL. Do not bundle JUnit into submission JARs.
