---
id: TASK-5
title: Port inventory and filter tests
status: Done
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T02:28:35Z'
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
- [x] #1 mvn -q test -Dtest='InventoryTest,FilterTest': 16 pass
- [x] #2 VerifyReport exits 0
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Done in parallel wave A (subagent). 16/16 pass x3. verifyAllProductImagesDisplayed is vacuous exactly like the TS (locator [data-test=inventory-item-img] matches nothing on saucedemo) - kept for parity, documented. SortOption enum = TS FilterType.
<!-- SECTION:NOTES:END -->
