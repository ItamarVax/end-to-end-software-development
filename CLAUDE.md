<!-- BEGIN DARWIN (generated — do not edit between markers) -->
# Project Context

## Conventions & Gotchas
- This repository serves the whole course. Apply the instructions of the current assignment; do not introduce later-course technologies early.
- EX1 requires vanilla Java with JDK 25, no Maven projects and no frameworks. Packages start with `patmal.workshops`.
- The Mac's default terminal Java is 17. The test runner chooses JDK 25 explicitly; IntelliJ already has a JDK 25 SDK registered.
- Keep test-only JUnit dependencies in ignored `testlib/`, outside submission artifacts.
- EX1 submission requires a public repository, implementation JARs in root `artifacts/`, and the exact tag `EX1-Vanilla` on the submitted commit.
- The original assignment guide is stored at the repository root as `EX1 - Workshop Registration Vanilla.docx`.

## Project-Specific Safety
- Do not modify lecturer-provided interfaces or tester files.
- Do not change API behavior to accommodate tests. Use the assignment and supplied interfaces as the contract.
- Never claim the lecturer's tester passes unless it has actually run successfully; use `bash scripts/acceptance-test.sh` to rebuild and verify the exact submission JARs.
- Preserve submitted tags while continuing development for later assignments.

## Approved Design Decisions
- Use an abstract PersonImpl base for shared person data; keep responsibilities focused and SOLID in mind without extra abstractions.
- Model methods coordinate relationship updates. Workshop reassignment is allowed; repeated association is a no-op and preserves other workshops.
- Students store a team reference and derive team name, instructor and teammates through that relationship.
- Collection getters return mutable shallow copies.
- Required command arguments reject null with IllegalArgumentException; this includes null member lists and null IDs inside member lists. Null query IDs count as absent: null for single objects, empty lists for collection queries.
- Reject empty teams and objects not registered by identity in the current system instance. Failed commands preserve state and ID counters.
- Do not introduce blank-string or email-format validation beyond these approved rules.
- Student knows C++ and Python: focus explanations on Java specifics and meaningful design choices; avoid elementary programming quizzes.
- User waived the formal Darwin workflow for this assignment. Work directly in the current checkout in small learning steps; get approval for new design choices before implementation.

# Code Quality Standards

## Readability
- Prefer beginner-friendly Java, clear names and small methods. Explain decisions when helping the student learn.
- Follow each assignment's interface signatures and constraints exactly.
- Keep production code independent of JUnit; place test sources in `tests/`.
- Add tests for real behavior when application code exists. Do not add always-passing placeholder tests.

## Quality Commands
- Full test suite: `bash scripts/test.sh`.
- No standalone linter, formatter or type checker is configured. Compilation with `javac --release 25` is part of the test runner.
- A test run with zero discovered tests must fail; infrastructure readiness is not evidence that application behavior works.

# Development Commands

Machine-readable copy: `.darwin/config.json` (`commands`). Change both together.

## Setup
- JDK 25 is required. `JAVA_HOME` may select it explicitly.
- Install or verify the pinned standalone JUnit runner: `bash scripts/setup-tests.sh`.
- Lecturer-supplied API and tester JARs are in `lib/`.

## Testing
- Full suite: `bash scripts/test.sh`.
- Scoped tests fall back to the full suite.
- The runner compiles Java from `model/src/`, `logic/src/`, `src/`, and `tests/` into ignored `build/test-classes/`.
- The runner intentionally fails if no tests are discovered.
- Lecturer acceptance check: `bash scripts/acceptance-test.sh`; this rebuilds and tests the submission JARs without JUnit or test classes.

## Build
- Submission JARs: `bash scripts/build.sh`.
- Output: `artifacts/workshop-model-impl.jar` and `artifacts/workshop-logic-impl.jar`.

## Other Commands
- No standalone lint, type-check, or application run command is configured. This project is an API library.

# Glossary

- Instructor: a person who teaches zero or more workshops.
- Workshop: an activity taught by an instructor, with teams registered to it.
- Team: a group of students participating in one workshop.
- Free student: a student who belongs to no team. Each student belongs to at most one team.
- WorkshopSystem: the supplied Java interface through which all registration operations are performed.
<!-- END DARWIN -->
