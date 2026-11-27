---
id: TASK-1
title: Toolchain, Maven skeleton, Config
status: To Do
assignee: []
created_date: '2026-11-27T00:47:28Z'
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
- [ ] #1 java -version shows 21 and mvn -v shows 3.9+
- [ ] #2 ConfigTest (5 tests) passes with mvn -q test -Dgroups=unit
- [ ] #3 results/junit/chrome/TEST-*ConfigTest.xml is written
- [ ] #4 Committed: chore: maven skeleton, config and toolchain
<!-- AC:END -->
