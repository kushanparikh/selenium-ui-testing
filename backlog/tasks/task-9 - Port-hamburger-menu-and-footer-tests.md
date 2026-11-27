---
id: TASK-9
title: Port hamburger menu and footer tests
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
ordinal: 9000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 9. HamburgerMenuPage, FooterPage, their assertions, HamburgerMenuTest (6), FooterTest (4).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 mvn -q test -Dtest='HamburgerMenuTest,FooterTest': 10 pass
- [ ] #2 VerifyReport exits 0
<!-- AC:END -->
