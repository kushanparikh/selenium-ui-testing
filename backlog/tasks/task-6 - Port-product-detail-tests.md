---
id: TASK-6
title: Port product detail tests
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
ordinal: 6000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 6. ProductDetailPage, ProductDetailAssertions, ProductDetailTest (5).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 mvn -q test -Dtest=ProductDetailTest: 5 pass
- [x] #2 VerifyReport exits 0
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Done in parallel wave A (subagent). 5/5 pass x3. Strict single-match product name click (Playwright strict mode); URL checks wait because Selenium click doesn't wait for navigation.
<!-- SECTION:NOTES:END -->
