---
id: TASK-2
title: Driver, waits, attachments, test base
status: Done
assignee: []
created_date: '2026-11-27T00:47:28Z'
updated_date: '2026-11-27T02:10:40Z'
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
- [x] #1 AttachmentsTest.relativePathIsPosix and pathOutsideRepoIsRejected pass
- [x] #2 WaitsTest.absentWaitsForRemoval and timeoutMessage pass
- [x] #3 SmokeTest.opensSauceDemo passes and writes results/attachments/chrome/SmokeTest/opensSauceDemo/final.png
- [x] #4 No Thread.sleep or implicit waits in the codebase
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Commit 3548958. Full suite: 10 tests (Config 5, Attachments 2, Waits 2, Smoke 1), 0 failures. [[ATTACHMENT|results/attachments/chrome/SmokeTest/opensSauceDemo/final.png]] appears in the testcase system-out. Chrome password-manager/leak-detection disabled (secret_sauce triggers breach dialog).
<!-- SECTION:NOTES:END -->
