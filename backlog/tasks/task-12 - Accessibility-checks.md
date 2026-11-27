---
id: TASK-12
title: Accessibility checks
status: In Progress
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T02:47:14Z'
labels:
  - a11y
milestone: m-2
dependencies:
  - TASK-5
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: medium
type: task
ordinal: 12000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 12. AccessibilityChecker (axe-core selenium, WCAG 2.0/2.1 A+AA), KnownAccessibilityIssues, AccessibilityTest (3).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 mvn -q test -Pa11y: 3 pass
- [ ] #2 Inventory report attachment lists select-name under Known Issues
- [x] #3 VerifyReport exits 0
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Wave B subagent. 3/3 pass. AC#2 NOT MET: saucedemo now gives the sort select aria-label='Sort products', so axe reports 0 violations and select-name (the only known issue) no longer appears. Awaiting owner decision: keep the stale allow-list entry or remove it.
<!-- SECTION:NOTES:END -->
