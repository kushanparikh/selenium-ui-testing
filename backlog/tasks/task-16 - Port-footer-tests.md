---
id: TASK-16
title: Port footer tests
status: To Do
assignee: []
created_date: '2026-11-27T02:20:10Z'
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
ordinal: 16000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 9 (footer half, split from TASK-9). FooterPage, FooterAssertions, FooterTest (4). Needs only LoginPage.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 mvn -q test -Dtest=FooterTest: 4 pass
- [ ] #2 VerifyReport exits 0
<!-- AC:END -->
