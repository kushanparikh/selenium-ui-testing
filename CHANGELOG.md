# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).
Changes to the [utr contract](README.md#utr-contract) are marked **(utr contract)**.

---

## [1.0.0] - Unreleased

### Added
- Migration of all 85 tests from playwright-typescript-framework to Selenium 4.54 + Java 21 + JUnit 5:
  login 13, inventory 13, filter 3, product detail 5, cart 8, checkout 17, hamburger menu 6, footer 4,
  e2e purchase 10, visual regression 3, accessibility 3. Test titles are the Playwright titles, verbatim.
- Page objects and assertion classes with the TS method names; `BaseTest` + `DriverExtension` in place of
  `baseTest.ts`.
- Explicit-wait layer (`Waits`) incl. a find-and-click step that retries stale and intercepted clicks.
- Visual comparison equivalent to `toHaveScreenshot`: pixelmatch YIQ delta (threshold 0.2), at most
  min(100 px, 1 %), anti-aliasing detection; full-page capture for Chrome (CDP) and Firefox, viewport for Safari.
- Accessibility checks with axe-core (WCAG 2.0/2.1 A + AA), the TS known-issue allow-list, and detection of
  allow-listed issues that no longer occur.
- Chrome, Firefox and Safari; viewport sized to 1280x720 on every browser.
- 25 framework self-tests (tag `selftest`).
- GitHub Actions: Chrome + Firefox on Windows, Safari on macOS; push/PR on `main`, nightly 06:30 UTC, manual
  runs with `update_snapshots`.
- **(utr contract)** Surefire JUnit XML in `results/junit/<browser>/`; one `utr-results` artifact per run
  (`<browser>/TEST-*.xml`, 90 days); attachments referenced as `[[ATTACHMENT|…]]`; `scripts/SanitizeReport.java`
  strips Surefire's `<properties>`; `scripts/VerifyReport.java` checks the contract.
- Backlog.md task board in `backlog/`.

### Known differences from the Playwright suite
- Footer test accepts the Twitter→X rename on saucedemo.
- Three tests skipped on Safari only (tab order; cart not saved under WebDriver-driven Safari).
- Safari visual baselines cover the viewport only.
