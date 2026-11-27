---
id: TASK-4
title: Port login tests
status: To Do
assignee: []
created_date: '2026-11-27T00:47:28Z'
labels:
  - port
  - functional
milestone: m-1
dependencies:
  - TASK-3
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: high
type: task
ordinal: 4000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 4. LoginPage, LoginAssertions, LoginTest (13 tests from tests/login.spec.ts, titles verbatim).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 mvn -q test -Dtest=LoginTest: 13 pass
- [ ] #2 VerifyReport exits 0 on results/junit/chrome
<!-- AC:END -->
