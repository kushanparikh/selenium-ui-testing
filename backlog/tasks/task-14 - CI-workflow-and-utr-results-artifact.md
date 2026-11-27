---
id: TASK-14
title: CI workflow and utr-results artifact
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
labels:
  - ci
  - utr
milestone: m-3
dependencies:
  - TASK-3
  - TASK-13
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: high
type: task
ordinal: 14000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 14. GitHub Actions matrix (windows chrome/firefox, macos safari), nightly 06:30 UTC, merged utr-results artifact (<browser>/TEST-*.xml, 90 days). Requires owner approval to create the repo and push.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 Owner approved repo creation and push
- [ ] #2 Windows and Safari baselines generated via update_snapshots dispatch and committed
- [ ] #3 Downloaded utr-results passes VerifyReport for all 3 browsers, 85 tests each
- [ ] #4 Sample artifact committed under samples/utr-results/
<!-- AC:END -->
