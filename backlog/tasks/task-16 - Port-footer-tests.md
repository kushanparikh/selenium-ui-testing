---
id: TASK-16
title: Port footer tests
status: Done
assignee: []
created_date: '2026-11-27T02:20:10Z'
updated_date: '2026-11-27T02:30:43Z'
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
Done in parallel wave A (subagent). 4/4 pass. DECIDED (owner, option a): saucedemo renamed the Twitter link to X (data-test=social-x, href x.com); the Java test accepts either. The TS test still fails on this (hidden by continue-on-error) - known divergence, to note in LEARNING_NOTES/TODO (TASK-15).
<!-- SECTION:NOTES:END -->
