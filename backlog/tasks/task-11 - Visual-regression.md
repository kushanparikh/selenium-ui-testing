---
id: TASK-11
title: Visual regression
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
labels:
  - visual
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
ordinal: 11000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 11. VisualComparator (pixelmatch YIQ, threshold 0.2, allowed=min(100, 1%)), FullPageScreenshot, VisualRegressionTest (3), chrome-mac baselines.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 VisualComparatorTest passes (identical, size mismatch, stricter allowance, small shift ignored, 10 black pixels counted)
- [ ] #2 Missing baseline writes actual, fails, and message is repo-relative
- [ ] #3 mvn -q test -Pvisual: 3 pass against committed *-chrome-mac.png baselines
<!-- AC:END -->
