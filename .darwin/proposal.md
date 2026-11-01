# Change: EX1 student registration foundation
Why: establish a small working slice of the workshop registry while teaching Java interfaces, objects, constructors, collections, validation, and tests.
Approach: configure two plain-Java IntelliJ modules with JDK 25: model for model implementations, logic for the WorkshopSystem implementation. Use the unchanged supplied API JARs. Implement a concrete student and the student registration, lookup, and listing operations through one public WorkshopSystem implementation with a public no-argument constructor. Store registered students by ID within each system instance. Proceed serially through guided learning checkpoints; explain and review each small step with the student. Other required WorkshopSystem methods remain explicitly unsupported scaffolding for later milestones, not silently successful implementations.
Rejected: implementing the whole assignment now (skips the requested learning steps); Maven or application frameworks (prohibited for EX1).
Out of scope: instructor/workshop/team registration and relationships, derived system queries, numeric ID generation, submission JAR packaging, final EX1 tag, GitHub publication, and a claim that the full lecturer tester passes. No additional null/blank input rules are introduced without an assignment requirement or an explicit later decision.
Test suite: present

## Behavior changes
NEW CAPABILITY student-registration: paths [model/src/, logic/src/, tests/] — register and retrieve free students in an in-memory system
ADDED STUDENT-REGISTRATION-1: WHEN a fresh system is queried for all students THEN it returns an empty non-null list | Verify: unit test
ADDED STUDENT-REGISTRATION-2: WHEN a student is registered with an unused ID THEN the returned student exposes the supplied ID, name and email and lookup by that ID returns that same object | Verify: unit test
ADDED STUDENT-REGISTRATION-3: WHEN a newly registered student has no team THEN its team name and instructor are null and its teammates list is empty and non-null | Verify: unit test
ADDED STUDENT-REGISTRATION-4: WHEN registration repeats an existing student ID THEN IllegalArgumentException is thrown and the original registered object, details and student count remain unchanged | Verify: unit test
ADDED STUDENT-REGISTRATION-5: WHEN a single student lookup uses an unknown ID THEN it returns null and does not alter registered students | Verify: unit test
ADDED STUDENT-REGISTRATION-6: WHEN registered students are listed THEN every registered object appears exactly once in a non-null list and modifying that returned list does not change the registry | Verify: unit test
ADDED STUDENT-REGISTRATION-7: WHEN separate system instances register students THEN their registrations are independent | Verify: unit test

## Learning checkpoints
1. Explain source roots, external API JARs, module dependencies, and JDK selection before setting up the project.
2. Explain how fields and a constructor let a concrete student satisfy its six interface methods; initially every student is free.
3. Explain the ID-to-object registry and write creation, lookup and listing a small step at a time.
4. Explain duplicate rejection before state changes, defensive list copies, and instance isolation using focused tests.

## Planning facts and constraints
- Actual API packages are patmal.workshops.api.model and patmal.workshops.api.logic; the guide reverses these components.
- All three lecturer JARs are available in lib/. The standalone JUnit dependency is test-only in ignored testlib/.
- Existing test command: bash scripts/test.sh. It currently finds zero tests and exits nonzero; Task 0 must introduce genuine behavior tests, not a placeholder smoke test.
- No Git commits exist yet. Before creating any feature worktree, the initial repository setup and supplied dependency files must be reviewed and committed so the worktree can contain them.
- The local folder is still named assignment-1 even though the remote is for the course; renaming it is outside this milestone.
- The implementation plan will include correcting stale setup documentation and regenerating the managed CLAUDE.md block from its canonical rules.
- This proposal is pending student review. No application implementation has started.
