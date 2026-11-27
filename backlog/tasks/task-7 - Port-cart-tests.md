---
id: TASK-7
title: Port cart tests
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T02:20:11Z'
labels:
  - port
  - functional
milestone: m-1
dependencies:
  - TASK-5
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: medium
type: task
ordinal: 7000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 7. CartPage, CartAssertions, CartTest (8). Review Focus 1: badge-absent must wait, not throw.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 mvn -q test -Dtest=CartTest: 8 pass
- [ ] #2 CartTest passes 3 consecutive runs
- [ ] #3 verifyCartBadgeNotVisible called immediately after last removal passes
<!-- AC:END -->
