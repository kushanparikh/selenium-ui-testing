---
id: TASK-16
title: Port footer tests
status: Done
assignee: []
created_date: '2026-11-27T02:20:10Z'
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
ordinal: 16000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 9 (footer half, split from TASK-9). FooterPage, FooterAssertions, FooterTest (4). Needs only LoginPage.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 mvn -q test -Dtest=FooterTest: 4 pass
- [x] #2 VerifyReport exits 0
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Done in parallel wave A (subagent). 4/4 pass. OPEN DECISION: saucedemo renamed the Twitter link to X (data-test=social-x, href x.com). Java currently accepts either; the TS test fails on this in CI (hidden by continue-on-error).
<!-- SECTION:NOTES:END -->
