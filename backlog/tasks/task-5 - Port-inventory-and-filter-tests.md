---
id: TASK-5
title: Port inventory and filter tests
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
labels:
  - port
  - functional
milestone: m-1
dependencies:
  - TASK-4
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: medium
type: task
ordinal: 5000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 5. InventoryPage, InventoryAssertions, SortOption, InventoryTest (13) and FilterTest (3).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 mvn -q test -Dtest='InventoryTest,FilterTest': 16 pass
- [ ] #2 VerifyReport exits 0
<!-- AC:END -->
