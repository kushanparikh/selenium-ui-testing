---
id: TASK-12
title: Accessibility checks
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
labels:
  - a11y
milestone: m-2
dependencies:
  - TASK-5
  - TASK-7
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
- [ ] #1 mvn -q test -Pa11y: 3 pass
- [ ] #2 Inventory report attachment lists select-name under Known Issues
- [ ] #3 VerifyReport exits 0
<!-- AC:END -->
