---
id: TASK-9
title: Port hamburger menu tests
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T02:20:18Z'
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
ordinal: 9000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 9 (hamburger half). HamburgerMenuPage, HamburgerMenuAssertions, HamburgerMenuTest (6). Needs InventoryPage from TASK-5. Footer half split out to TASK-16.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 mvn -q test -Dtest=HamburgerMenuTest: 6 pass
- [ ] #2 VerifyReport exits 0
<!-- AC:END -->
