---
id: TASK-10
title: Port e2e purchase workflows
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
labels:
  - port
  - e2e
milestone: m-1
dependencies:
  - TASK-5
  - TASK-6
  - TASK-7
  - TASK-8
  - TASK-9
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: medium
type: task
ordinal: 10000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 10. CompletePurchaseTest (10 tests from tests/e2e/completePurchase.spec.ts).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 mvn -q test -Dtest=CompletePurchaseTest: 10 pass
- [ ] #2 Full functional run: 79 tests, 0 failures
<!-- AC:END -->
