---
id: TASK-13
title: Firefox and Safari support
status: Done
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T04:10:23Z'
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
- [x] #1 Firefox: 85 tests pass
- [x] #2 VerifyReport exits 0 for firefox and safari
- [x] #3 Browser-specific fixes recorded for LEARNING_NOTES
- [x] #4 Safari: 82 pass, 3 skipped on Safari only with documented reasons (owner decision b)
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Chrome 85/85 (110 with self-tests), Firefox 85/85, Safari 82 + 3 skipped. Fixes: viewport now 1280x720 on every browser (was 1280x577 Chrome headless / 1280x635 Firefox); pixelmatch anti-aliasing detection ported (Chrome visual tests were flaky on text edges); Firefox + Safari mac baselines (Safari = viewport only). Safari skips: tab order (Tab skips buttons by default) and 2 cart tests (saucedemo doesn't persist the cart under WebDriver-driven Safari; works in normal Safari - checked manually). Local Safari needs display awake + unlocked (caffeinate -d); first run failed 48 tests after display sleep.
<!-- SECTION:NOTES:END -->
