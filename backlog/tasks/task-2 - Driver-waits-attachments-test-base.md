---
id: TASK-2
title: Driver, waits, attachments, test base
status: To Do
assignee: []
created_date: '2026-11-27T00:47:28Z'
labels:
  - framework
milestone: m-0
dependencies:
  - TASK-1
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: high
type: task
ordinal: 2000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 2. DriverFactory, Waits, Attachments, DriverExtension (fresh driver per test, final screenshot), BaseTest, BasePage, Users.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 AttachmentsTest.relativePathIsPosix and pathOutsideRepoIsRejected pass
- [ ] #2 WaitsTest.absentWaitsForRemoval and timeoutMessage pass
- [ ] #3 SmokeTest.opensSauceDemo passes and writes results/attachments/chrome/SmokeTest/opensSauceDemo/final.png
- [ ] #4 No Thread.sleep or implicit waits in the codebase
<!-- AC:END -->
