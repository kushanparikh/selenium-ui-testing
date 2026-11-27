---
id: TASK-3
title: utr report contract and verifier
status: To Do
assignee: []
created_date: '2026-11-27T00:47:28Z'
labels:
  - utr
  - reporting
milestone: m-0
dependencies:
  - TASK-2
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: high
type: task
ordinal: 3000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 3 / spec section 8. scripts/VerifyReport.java checks the JUnit XML contract; ContractDemoTest proves retries are recorded.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 Contract-demo run with reruns=2 yields flakyFailure and VerifyReport --expect-flaky exits 0
- [ ] #2 -Dbrowser=unknown run records an error per test and VerifyReport --expect-errors exits 0
- [ ] #3 Normal unit run passes VerifyReport (no properties element, no absolute paths, no base64)
<!-- AC:END -->
