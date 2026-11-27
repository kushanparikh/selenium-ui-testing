---
id: TASK-8
title: Port checkout tests
status: To Do
assignee: []
created_date: '2026-11-27T00:47:29Z'
updated_date: '2026-11-27T02:20:11Z'
labels:
  - port
  - functional
milestone: m-1
dependencies:
  - TASK-5
references:
  - docs/superpowers/plans/2026-11-26-selenium-migration.md
documentation:
  - docs/superpowers/specs/2026-11-26-selenium-migration-design.md
priority: medium
type: task
ordinal: 8000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Plan Task 8. CheckoutPage (BigDecimal amounts), CheckoutAssertions, CheckoutTest (17).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [ ] #1 mvn -q test -Dtest=CheckoutTest: 17 pass
- [ ] #2 verifyTotalCalculation uses BigDecimal compareTo
- [ ] #3 VerifyReport exits 0
<!-- AC:END -->
