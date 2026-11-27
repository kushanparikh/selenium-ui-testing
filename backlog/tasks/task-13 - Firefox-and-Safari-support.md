---
id: TASK-13
title: Firefox and Safari support
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
labels:
  - cross-browser
milestone: m-3
dependencies:
  - TASK-10
  - TASK-11
  - TASK-12
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: medium
type: task
ordinal: 13000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 13. Run the full suite on Firefox and Safari, fix browser-specific issues, add mac baselines.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 Firefox: 85 tests pass
- [ ] #2 Safari (after safaridriver --enable): 85 tests pass
- [ ] #3 VerifyReport exits 0 for firefox and safari
- [ ] #4 Browser-specific fixes recorded for LEARNING_NOTES
<!-- AC:END -->
