---
id: TASK-17
title: 'Execution time: parallel Chrome/Firefox and a Safari critical subset'
status: To Do
assignee: []
created_date: '2026-11-27T03:56:06Z'
labels:
  - performance
  - ci
milestone: m-5
dependencies:
  - TASK-14
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
priority: medium
type: enhancement
ordinal: 17000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
All tests currently run one at a time (junit.jupiter.execution.parallel.enabled=false). Chrome/Firefox are headless and each test has its own browser, so they can run in parallel; Safari cannot (safaridriver allows one session) and a full Safari run takes ~16 min. Owner direction (2026-11-26): first let the suite run in CI (TASK-14) and measure how long Chrome and Firefox take, then enable parallel execution for Chrome/Firefox and run only a limited set of critical tests on Safari, balancing coverage, time and ROI. Note: the screenshot helper finds the running test through a ThreadLocal; verify attachments stay per-test under parallel execution.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 Baseline recorded: per-browser CI job durations (Chrome, Firefox, Safari) from at least 2 TASK-14 runs
- [ ] #2 Parallel execution configurable via a Maven property (e.g. -Dparallel=N), on for Chrome/Firefox, always off for Safari
- [ ] #3 Chrome and Firefox full runs pass in parallel 3 times in a row; attachments stay per-test; VerifyReport OK
- [ ] #4 Safari critical subset defined (JUnit tag, e.g. safari-critical) with a written rationale per test (coverage vs time vs ROI), agreed with the owner
- [ ] #5 CI Safari job runs only the critical subset; new per-browser durations recorded and compared with the baseline
- [ ] #6 README documents parallel runs and the Safari subset
<!-- AC:END -->
