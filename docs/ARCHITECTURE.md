# Architecture

How the framework is put together, and why. It mirrors the Playwright repo's
[ARCHITECTURE.md](https://github.com/kushanparikh/playwright-typescript-framework/blob/main/docs/ARCHITECTURE.md)
section by section; where Selenium forced a different design, this document says so.

## Table of Contents

1. [Architecture Principles](#architecture-principles)
2. [Framework Layers](#framework-layers)
3. [Design Patterns](#design-patterns)
4. [Project Structure](#project-structure)
5. [Data Flow](#data-flow)
6. [Testing Strategy](#testing-strategy)
7. [CI/CD Architecture](#cicd-architecture)
8. [Design Decisions](#design-decisions)
9. [Scalability Considerations](#scalability-considerations)

## Architecture Principles

The same principles as the Playwright repo:

- **Separation of concerns**: tests orchestrate, page objects act, assertion classes verify.
- **DRY**: one page object per page, one assertion class per page, shared waits and attachments.
- **Single responsibility**: each class has one reason to change.
- **Dependency injection**: tests declare nothing and receive ready page objects (Playwright fixtures → `BaseTest`).
- **Test isolation**: a new browser for every test, so tests run in any order.

One Selenium-specific principle is added:

- **Every check waits.** Playwright's locators and `expect()` retry until a timeout; Selenium's calls run once.
  Every interaction and assertion here goes through `Waits` (explicit waits, 10 s). There is no `Thread.sleep`
  and no implicit wait anywhere.

## Framework Layers

```
┌──────────────────────────────────────────────────────────────┐
│ TEST LAYER          tests/**/*Test.java                      │
│  scenarios; @DisplayName = Playwright title, @Nested = describe│
└──────────────────────────────────────────────────────────────┘
                               ↓ extends
┌──────────────────────────────────────────────────────────────┐
│ TEST BASE           support/BaseTest + DriverExtension       │
│  new driver per test, page objects + assertion classes,      │
│  final screenshot, driver.quit() even on failure             │
└──────────────────────────────────────────────────────────────┘
                 ↓                              ↓
┌─────────────────────────────┐  ┌─────────────────────────────┐
│ PAGE OBJECTS  pages/        │  │ ASSERTIONS  assertions/     │
│  actions (void), By getters │  │  verify*() that wait        │
└─────────────────────────────┘  └─────────────────────────────┘
                 ↓                              ↓
┌──────────────────────────────────────────────────────────────┐
│ SUPPORT  Waits · Attachments · VisualComparator ·            │
│          FullPageScreenshot · AccessibilityChecker · Users   │
└──────────────────────────────────────────────────────────────┘
                               ↓
┌──────────────────────────────────────────────────────────────┐
│ DRIVER + CONFIG  DriverFactory (Selenium Manager), Config    │
└──────────────────────────────────────────────────────────────┘
```

| Playwright | This repo |
|---|---|
| `playwright.config.ts` | `config/Config.java` (system properties with defaults) + `pom.xml` profiles |
| built-in `page` fixture | `support/DriverExtension.java` (JUnit 5 extension) |
| `tests/fixtures/baseTest.ts` | `support/BaseTest.java` |
| `Locator` returned by getters | `By` returned by getters |
| `expect(locator).toBe…()` | `page.waits().visible/text/absent/until(…)` + AssertJ |
| `testInfo.attach()` | `support/Attachments.java` → `[[ATTACHMENT|path]]` |
| `toHaveScreenshot()` | `support/VisualComparator.java` + `FullPageScreenshot.java` |
| `@axe-core/playwright` | `support/AccessibilityChecker.java` (`com.deque.html.axe-core:selenium`) |

## Design Patterns

- **Page Object Model**: `pages/*Page.java` extend `BasePage` (driver, `waits()`, `getURL()`, `getTitle()`).
  Getters return `By`, not `WebElement`: a `By` is resolved again at the moment it is used, so nothing goes stale
  across re-renders (saucedemo is a React app).
- **Assertion classes**: `assertions/*Assertions.java`, constructor takes the page; the method names are the TS
  names, so a TS test and its Java port read the same.
- **Extension as fixture**: `DriverExtension` implements `BeforeEachCallback` / `AfterEachCallback` /
  `ParameterResolver`. If the browser can't start, the exception propagates and the test is reported as an error.
- **Find-and-act in one retried step**: `Waits.click(By)` looks the element up and clicks inside one wait,
  retrying stale and intercepted clicks, which is what Playwright's `locator.click()` does.
  `clickable(by).click()` (two steps) races with React re-renders and was replaced everywhere.

## Project Structure

See the [README](../README.md#project-structure). Test tags: `functional` (login, inventory, filter, product
detail, as in `npm run test:functional`), `visual`, `a11y`, `selftest`, `contract-demo`, `flaky`.

## Data Flow

```
mvn test -Dbrowser=firefox
  → Surefire forks a JVM with -Dbrowser, -Dheadless, -DrepoRoot … (systemPropertyVariables)
  → DriverExtension.beforeEach: DriverFactory.create(Config.browser(), Config.headless())
        → size window, measure innerWidth/innerHeight, resize until the viewport is 1280x720
  → BaseTest.setUpBase: new page objects + assertion classes around that driver
  → test: open(), actions, verify*() (all waiting), attachScreenshot(name)
        → Attachments: results/attachments/firefox/<Class>/<method>/<name>.png
          + "[[ATTACHMENT|…]]" on stdout → <system-out> of the testcase
  → DriverExtension.afterEach: final.png, driver.quit()
  → Surefire: results/junit/firefox/TEST-*.xml (+ Allure: target/allure-results)
CI → SanitizeReport (drop <properties>) → VerifyReport (contract) → upload → merge into utr-results
```

## Testing Strategy

| Suite | Tests | Notes |
|---|---|---|
| Login | 13 | incl. special users and keyboard navigation |
| Inventory | 13 | |
| Filter | 3 | `SortOption` = TS `FilterType` |
| Product detail | 5 | |
| Cart | 8 | |
| Checkout | 17 | amounts as `BigDecimal` |
| Hamburger menu | 6 | |
| Footer | 4 | accepts the Twitter→X rename |
| E2E purchase | 10 | |
| Visual regression | 3 | per browser + OS baselines |
| Accessibility | 3 | WCAG 2.0/2.1 A + AA, known-issue allow-list with stale detection |
| **Total** | **85** | same as the Playwright suite |

Framework self-tests (`selftest`, 25): config parsing, attachment paths (POSIX even on Windows), waits incl. stale-
element retry, viewport size per browser, comparator maths, stale allow-list detection, smoke test.

## CI/CD Architecture

```
push main / PR / nightly 06:30 UTC / manual
  ├─ chrome  (windows-latest) ─┐   each: mvn test → sanitize → verify →
  ├─ firefox (windows-latest) ─┤         upload <browser> (JUnit), test-evidence-<browser>,
  └─ safari  (macos-latest)   ─┘         allure-results-<browser>[, baselines-<browser>]
                                 └─ utr-results job: merge {chrome,firefox,safari} → utr-results (90 days)
```

Retries: 2 on CI (Maven profile `ci`, activated by the `CI` env var), every attempt kept in the XML.

## Design Decisions

| Decision | Why |
|---|---|
| JUnit 5, not TestNG | Surefire's `rerunFailingTestsCount` records every retry in the JUnit XML; TestNG retries show up as skips and lose the flakiness signal utr needs. |
| Maven Surefire JUnit XML as the report | Standard format, one file per class per browser; utr gets a JUnit adapter instead of a custom format. |
| Selenium Manager, no WebDriverManager | Built into Selenium 4; also downloads Firefox when missing. |
| `By` getters | Re-resolved on use, so they survive React re-renders. |
| Viewport sized by measurement | `setSize` sizes the window; toolbars took 143 px (headless Chrome) and 85 px (Firefox). |
| Chrome screenshots via CDP at DPR 1 | Retina headed runs would otherwise double the image size. |
| pixelmatch port incl. anti-aliasing detection | Text edges vary run to run; without it the Chrome visual tests were flaky. |
| Sequential execution | Safari allows one session; parallel Chrome/Firefox is planned (backlog TASK-17) after CI timings are known. |
| Fresh browser per test | Same isolation as Playwright's per-test context; costs startup time. |

## Scalability Considerations

- **Parallel runs**: tests share no state; enabling JUnit parallel execution for Chrome/Firefox is a configuration
  change (TASK-17). Safari stays serial.
- **Safari subset**: Safari is the slowest job; TASK-17 picks a critical subset by coverage, time and ROI.
- **New page**: page object + assertion class + two fields in `BaseTest`.
- **New browser**: one `Browser` value and one `DriverFactory` case.
