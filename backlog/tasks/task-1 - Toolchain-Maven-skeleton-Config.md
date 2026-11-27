---
id: TASK-1
title: Toolchain, Maven skeleton, Config
status: Done
assignee: []
created_date: '2026-11-27T00:47:28Z'
updated_date: '2026-11-27T01:59:33Z'
labels:
  - setup
  - framework
milestone: m-0
dependencies: []
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: high
type: task
ordinal: 1000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 1. Install JDK 21 + Maven, write pom.xml (Surefire report contract, profiles stable/functional/visual/a11y/ci), Browser enum and Config.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 java -version shows 21 and mvn -v shows 3.9+
- [x] #2 ConfigTest (5 tests) passes with mvn -q test -Dgroups=unit
- [x] #3 results/junit/chrome/TEST-*ConfigTest.xml is written
- [x] #4 Committed: chore: maven skeleton, config and toolchain
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Commit: chore: maven skeleton, config and toolchain. JDK = Homebrew openjdk@21 (no sudo); run mvn with JAVA_HOME=/opt/homebrew/opt/openjdk@21 (Homebrew Maven defaults to JDK 27). Versions: Selenium 4.54.0, JUnit 5.14.4, AssertJ 3.27.7, axe-core selenium 4.14.1, Allure 2.35.6, Surefire 3.6.1.
<!-- SECTION:NOTES:END -->
