# Selenium UI Testing

UI test automation for [saucedemo.com](https://www.saucedemo.com/) with **Selenium 4, Java 21 and JUnit 5**.
It is a migration of [playwright-typescript-framework](https://github.com/kushanparikh/playwright-typescript-framework):
the same 85 tests, the same page objects and assertion names, so the two repos can be read side by side.
Where Selenium needs something Playwright gives you for free (auto-waiting, screenshot diffing, viewport sizing),
the framework builds it and [docs/LEARNING_NOTES.md](docs/LEARNING_NOTES.md) explains the difference.

| | |
|---|---|
| Tests | 85 (79 functional, 3 visual regression, 3 accessibility) + 25 framework self-tests |
| Browsers | Chrome, Firefox, Safari (Playwright's chromium / firefox / webkit) |
| Stack | Selenium 4.54, JUnit 5.14, AssertJ, axe-core 4.14, Allure, Maven, Temurin 21 |
| Report | Surefire JUnit XML per browser, consumed by unified-test-reporting ([utr contract](#utr-contract)) |

## Prerequisites

- **JDK 21** and **Maven 3.9+**: `brew install openjdk@21 maven`, then `export JAVA_HOME=/opt/homebrew/opt/openjdk@21`
  (Homebrew's Maven otherwise runs on its newest JDK).
- **Browsers**: nothing to download by hand. Selenium Manager fetches drivers, and even Firefox if it isn't installed.
- **Safari** (macOS only), once per machine: `sudo safaridriver --enable`.
  Safari runs in a visible window: keep the display awake and the screen unlocked for the whole run
  (`caffeinate -d mvn test -Dbrowser=safari`). A sleeping display makes every remaining Safari test fail with black screenshots.

## Running tests

| Playwright repo | This repo |
|---|---|
| `npx playwright test` | `mvn test` (Chrome by default) |
| `npx playwright test --project=firefox` | `mvn test -Dbrowser=firefox` (`chrome` \| `firefox` \| `safari`) |
| `npx playwright test tests/login.spec.ts` | `mvn test -Dtest=LoginTest` |
| `npx playwright test -g "locked out"` | `mvn test -Dtest='LoginTest$LoginWithInvalidCredentials#lockedOutUser'` |
| `npx playwright test --headed` | `mvn test -Dheadless=false` (headed is the default locally, headless on CI) |
| `npm run test:stable` (skip `@flaky`) | `mvn test -Pstable` |
| `npm run test:functional` | `mvn test -Pfunctional` |
| `npm run test:visual` | `mvn test -Pvisual` |
| `npm run test:a11y` | `mvn test -Pa11y` |
| `npx playwright test --update-snapshots` | `mvn test -Pvisual -DupdateSnapshots=true` |
| `npx playwright show-report` | `allure serve target/allure-results` (needs `brew install allure`) |

Only the 85 saucedemo tests: `mvn test -DexcludedGroups=contract-demo,selftest` (what CI runs).
Only the framework self-tests: `mvn test -Dgroups=selftest`.

Other switches: `-DbaseUrl=…`, `-DtimeoutSeconds=…` (explicit-wait timeout, default 10).
Retries are off locally and 2 on CI (`-Dsurefire.rerunFailingTestsCount=N` to change).

## Project structure

```
src/test/java/io/github/kushanparikh/selenium/
  config/       Config (all runtime settings), Browser
  driver/       DriverFactory: browser per test, 1280x720 viewport
  pages/        page objects: actions + By locators (TS method names)
  assertions/   assertion classes: waiting checks (TS method names)
  support/      BaseTest + DriverExtension (the baseTest.ts fixture), Waits, Attachments,
                VisualComparator, FullPageScreenshot, AccessibilityChecker, Users
  tests/        the 85 ported tests (e2e/, visual/, accessibility/)
  selftest/     tests of the framework's own helpers (tag "selftest")
  contract/     ContractDemoTest: proves retries reach the XML (tag "contract-demo", excluded by default)
src/test/resources/visual-baselines/   <name>-<browser>-<os>.png
scripts/        VerifyReport.java, SanitizeReport.java (utr contract tooling)
.github/workflows/selenium.yml
backlog/        task board (Backlog.md)
```

How the layers fit together: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

## Reports and evidence

| Output | Where |
|---|---|
| JUnit XML (machine-readable, utr) | `results/junit/<browser>/TEST-*.xml` |
| Screenshots, visual diffs, a11y reports | `results/attachments/<browser>/<TestClass>/<method>/` |
| Allure (human-readable) | `target/allure-results/` |

Every test saves `final.png` when it ends, plus the screenshots the Playwright test attached.
A failing visual test saves `<name>-expected.png`, `-actual.png` and `-diff.png` (red = different, yellow = anti-aliasing, ignored).

## utr contract

unified-test-reporting (a separate, private project) collects this repo's CI results. The contract below is what its
adapter relies on; changing any of it is a breaking change and goes in [CHANGELOG.md](CHANGELOG.md).

1. **Format**: Maven Surefire JUnit XML (`TEST-*.xml`, JUnit5 XML 3.0 reporter). Retries appear as
   `<flakyFailure>` / `<rerunFailure>`; skipped tests carry their reason in `<skipped message>`.
2. **Artifact**: one GitHub Actions artifact named **`utr-results`** per workflow run, kept 90 days, uploaded even
   when tests fail. Layout: `<browser>/TEST-*.xml` at the artifact root, `<browser>` = `chrome` | `firefox` | `safari`.
3. **Triggers**: every push to `main` and nightly at 06:30 UTC (also pull requests and manual runs).
4. **Identity**: `classname` (fully-qualified test class, including `$Nested` classes) + `name` (the Playwright test
   title, verbatim) + the browser directory. None of them contains an OS, path or timestamp.
5. **No inline binaries**: evidence files are referenced from `<system-out>` as
   `[[ATTACHMENT|results/attachments/…]]` (repo-relative, `/` separators); bodies are never embedded.
6. **No machine data**: Surefire writes a `<properties>` block (usernames, absolute paths) for failed, rerun and
   skipped tests even with `enablePropertiesElement=false`. CI removes it with `scripts/SanitizeReport.java` and
   then checks every rule above with `scripts/VerifyReport.java`. Local reports of failing runs still contain it.
7. **Browser can't start**: every affected test is recorded as an `<error>`, never dropped.

Check a report yourself: `java scripts/VerifyReport.java results/junit/chrome`.

## CI

[.github/workflows/selenium.yml](.github/workflows/selenium.yml): Chrome and Firefox on `windows-latest`
(same OS as the Playwright CI), Safari on `macos-latest`, `fail-fast: false`. Test failures don't fail the
job (like the Playwright workflow's `continue-on-error`), so read the test counts. Manual runs take
`update_snapshots: true` to regenerate baselines, uploaded as `baselines-<browser>` artifacts to review and commit.

## Known differences from the Playwright suite

- **Footer Twitter link**: saucedemo renamed it to X (`data-test="social-x"`, `x.com`). This suite accepts either;
  the TS test still looks for `social-twitter` and fails.
- **Accessibility allow-list**: saucedemo fixed `select-name`; the entry is kept, and the report lists it under
  "Stale Known Issues".
- **Three tests are skipped on Safari only** (reason in the code and in the XML): tab order (Safari's Tab key skips
  buttons by default) and two cart tests (saucedemo doesn't save the cart in WebDriver-driven Safari; normal Safari
  works).
- **Safari visual tests cover the viewport only**: SafariDriver has no full-page screenshot.

The full list, including checks in the TS suite that can't fail, is in [TODO.md](TODO.md).

## Task board

Work is tracked with [Backlog.md](https://github.com/MrLesk/Backlog.md) in `backlog/`:
`backlog board` (terminal kanban) or `backlog browser` (web).

## License

[MIT](LICENSE)
