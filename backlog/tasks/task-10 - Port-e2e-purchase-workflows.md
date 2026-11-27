---
id: TASK-10
title: Port e2e purchase workflows
status: Done
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T03:10:34Z'
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
  - TASK-16
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
- [x] #1 mvn -q test -Dtest=CompletePurchaseTest: 10 pass
- [x] #2 Full functional run: 79 tests, 0 failures
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Commit 91582aa. 10/10 e2e pass; functional run = 79 tests. Full suite 107 = 85 ported (79 functional + 3 visual + 3 a11y, same as Playwright) + 22 framework self-tests. One-shot TS cart counts became waiting verifyCartItemCount; waitForTimeout calls dropped. SmokeTest now tagged unit.
<!-- SECTION:NOTES:END -->
