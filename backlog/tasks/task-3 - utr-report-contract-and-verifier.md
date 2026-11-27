---
id: TASK-3
title: utr report contract and verifier
status: Done
assignee: []
created_date: '2026-11-27T00:47:28Z'
updated_date: '2026-11-27T02:14:38Z'
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
- [x] #1 Contract-demo run with reruns=2 yields flakyFailure and VerifyReport --expect-flaky exits 0
- [x] #2 -Dbrowser=unknown run records an error per test and VerifyReport --expect-errors exits 0
- [x] #3 Normal unit run passes VerifyReport (no properties element, no absolute paths, no base64)
<!-- AC:END -->

## Implementation Notes

<!-- SECTION:NOTES:BEGIN -->
Commit: feat: utr report contract verifier and sanitizer. FINDING: Surefire 3.6.1 writes <properties> (user.home, tmpdir, repoRoot) for every failed/rerun test despite enablePropertiesElement=false. Added scripts/SanitizeReport.java; CI must run SanitizeReport then VerifyReport before upload (added to TASK-14).
<!-- SECTION:NOTES:END -->
