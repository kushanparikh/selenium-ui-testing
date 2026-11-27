---
id: TASK-12
title: Accessibility checks
status: Done
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T03:00:26Z'
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
- [x] #2 VerifyReport exits 0
- [x] #3 Inventory report lists select-name under Stale Known Issues (site fixed it) and a WARN is logged; test still passes
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Wave B subagent + owner decision (c). 3/3 pass. saucedemo added aria-label='Sort products', so select-name is no longer reported; it stays allow-listed and is flagged as 'Stale Known Issues' in the report plus a WARN log.
<!-- SECTION:NOTES:END -->
