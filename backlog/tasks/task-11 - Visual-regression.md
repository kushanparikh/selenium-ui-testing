---
id: TASK-11
title: Visual regression
status: Done
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T02:47:14Z'
labels:
  - visual
milestone: m-2
dependencies:
  - TASK-5
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
- [x] #1 VisualComparatorTest passes (identical, size mismatch, stricter allowance, small shift ignored, 10 black pixels counted)
- [x] #2 Missing baseline writes actual, fails, and message is repo-relative
- [x] #3 mvn -q test -Pvisual: 3 pass against committed *-chrome-mac.png baselines
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Wave B subagent. Comparator 8 unit tests + 3 visual tests; headed and headless both match the chrome-mac baselines (CDP pins 1280x720 @ DPR 1).
<!-- SECTION:NOTES:END -->
