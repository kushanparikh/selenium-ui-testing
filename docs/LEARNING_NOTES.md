# Learning Notes: Playwright → Selenium

Notes from migrating [playwright-typescript-framework](https://github.com/kushanparikh/playwright-typescript-framework)
to Selenium 4 + Java. Each topic follows the TS repo's LEARNING_NOTES and answers three questions:
what Playwright did, what Selenium does, and why they differ. The last section lists the surprises
the migration ran into, with the evidence.

## Table of Contents

1. [Test Structure](#test-structure)
2. [Fixtures](#fixtures)
3. [Hooks](#hooks)
4. [Locator Strategies](#locator-strategies)
5. [Waiting](#waiting)
6. [Configuration](#configuration)
7. [Assertions](#assertions)
8. [Java Patterns](#java-patterns)
9. [Accessibility Testing](#accessibility-testing)
10. [Visual Regression Testing](#visual-regression-testing)
11. [Reporting and Attachments](#reporting-and-attachments)
12. [CI/CD Integration](#cicd-integration)
13. [Browser-Specific Findings](#browser-specific-findings)
14. [Findings About the Playwright Suite](#findings-about-the-playwright-suite)

## Test Structure

| Playwright | JUnit 5 |
|---|---|
| `test('title', async (...) => {})` | `@Test @DisplayName("title") void method()` |
| `test.describe('name', () => {})` | `@Nested @DisplayName("name") class Name` (non-static inner class) |
| file with one top-level describe | class-level `@DisplayName`, no `@Nested` |
| `@flaky` in the title + `--grep-invert` | `@Tag("flaky")` + `-Pstable` (`excludedGroups`) |
| `test.setTimeout(60000)` | no default per-test timeout in JUnit; waits have their own timeout |

**Why it matters for reporting:** JUnit's XML `classname` becomes `LoginTest$LoginWithInvalidCredentials` for a
nested class, and `name` is the display name. That's a stable identity (no file path, no OS), which is what utr needs.

## Fixtures

**Playwright:** `test.extend<MyFixtures>({ loginPage: async ({ page }, use) => use(new LoginPage(page)) })`.
Fixtures are lazy, composable and torn down automatically.

**Selenium + JUnit:** there are no fixtures. The same effect takes two pieces:
- `DriverExtension` (a JUnit extension) creates the browser before each test and quits it after, even on failure.
- `BaseTest` receives the driver in a `@BeforeEach` (via `ParameterResolver`) and builds every page object and
  assertion class.

**Difference:** Playwright creates only the fixtures a test asks for; `BaseTest` builds all 14 objects for every
test. They're thin wrappers around the driver, so it costs nothing measurable.

## Hooks

| Playwright | JUnit 5 |
|---|---|
| `test.beforeEach` at file level | `@BeforeEach` in the test class |
| `test.beforeEach` inside a describe | `@BeforeEach` inside the `@Nested` class |
| outer `beforeEach` runs for nested tests | outer `@BeforeEach` also runs for `@Nested` tests, so the TS "Special user types" block's repeated `goto('/')` wasn't ported twice |
| `afterEach` with `testInfo.status` | `AfterEachCallback` in an extension (screenshot + quit) |

## Locator Strategies

| Playwright | Selenium |
|---|---|
| `page.locator('[data-test="x"]')` | `By.cssSelector("[data-test='x']")` |
| `getByPlaceholder('Username')` | `By.cssSelector("[placeholder='Username']")` |
| `getByRole('button', { name: 'Login' })` | `By.cssSelector("input[type='submit'][value='Login']")`; Selenium has no role locators |
| `getByText('Products')` | `By.xpath("//*[normalize-space(text())='Products']")` |
| `locator.nth(i)` | XPath `(//…)[i+1]` or `findElements(by).get(i)`; CSS can't pick the n-th match across the document |
| `locator.filter({ hasText })` + strict click | filter `findElements` inside a wait and require exactly one match yourself |

Page getters return `By`, not `WebElement`. A `By` is looked up again at the moment it's used; a stored
`WebElement` goes stale as soon as React re-renders it.

## Waiting

This is the biggest difference between the two tools.

**Playwright** waits automatically: `click()` waits for the element to be visible, enabled and stable, and waits
for the navigation it starts; `expect(locator)` retries until its timeout.

**Selenium** does each call exactly once. `click()` returns as soon as the click is sent, before the page changes.
So:
- Every check here goes through `Waits` (`WebDriverWait` with 10 s, ignoring stale and missing elements); a
  timeout becomes an `AssertionError` with a readable message.
- A URL check after a click must wait (`urlContains`); the TS `expect(page.url()).toContain(...)` works only
  because Playwright's click already waited.
- `toHaveCount(n)` becomes "wait until `findElements(by).size() == n`". `waits().all()` needs at least one match and
  can never pass for 0.
- `not.toBeVisible()` becomes `waits().absent(by)`: wait until nothing matching is displayed. It doesn't throw
  `NoSuchElementException`, and it doesn't pass before the element is gone.
- **Find and click in one step.** `clickable(by).click()` finds the element, then clicks it, and React can
  replace the element in between (`StaleElementReferenceException`). That broke the hamburger-menu test right
  after a route change. `Waits.click(by)` finds and clicks inside one retried wait, like Playwright's
  `locator.click()`.
- Never `Thread.sleep`. The TS `waitForTimeout(500)` calls were dropped: the badge-absent wait that follows them
  already covers the reset.

## Configuration

| `playwright.config.ts` | Here |
|---|---|
| `use.baseURL` | `Config.baseUrl()` (`-DbaseUrl`) |
| `projects: chromium/firefox/webkit` | `-Dbrowser=chrome|firefox|safari`, one Maven run per browser |
| `retries: CI ? 2 : 0` | Maven profile `ci` (activated by env `CI`) sets `surefire.rerunFailingTestsCount=2` |
| `workers: CI ? 1 : undefined` | JUnit parallel execution off (TASK-17 plans parallel Chrome/Firefox) |
| `expect.timeout: 10000` | `Config.timeout()` = 10 s (`-DtimeoutSeconds`) |
| `screenshot: 'on'` | `DriverExtension` saves `final.png` after every test |
| device viewport 1280x720 | `DriverFactory` sizes the *viewport*; see below |
| `npm run test:*` scripts | Maven profiles `stable`, `functional`, `visual`, `a11y` |

**Viewport vs window:** Playwright sets the viewport (the page area). Selenium's `window().setSize()` sets the
window, and toolbars take part of it: measured 1280x577 in headless Chrome and 1280x635 in Firefox for a
1280x720 window. `DriverFactory` measures `innerWidth`/`innerHeight` and grows the window by the difference.
Until that fix, every Chrome test had run on a shorter page than Playwright's.

## Assertions

| Playwright | Here |
|---|---|
| `expect(locator).toBeVisible()` | `waits().visible(by)` |
| `toHaveText('x')` | `waits().text(by, "x")` (exact `getText()`, already trimmed) |
| `toContainText('x')` | `waits().containsText(by, "x")` |
| `toHaveAttribute('type', 'password')` | wait on `getDomAttribute("type")` |
| `toHaveCSS(...)` | wait on `getCssValue(...)` |
| `toBeFocused()` | wait until `switchTo().activeElement()` equals the element |
| `toBeEnabled()` / `toBeDisabled()` | wait on `isEnabled()` |
| `expect(value).toBe(x)` (plain value) | AssertJ `assertThat(value).isEqualTo(x)` |
| `toBeCloseTo(x, 2)` for money | `BigDecimal` + `isEqualByComparingTo`: exact, no tolerance needed |

Attribute gotchas: Selenium's `getAttribute("src")` returns the *property* (an absolute URL);
`getDomAttribute` returns the raw attribute like Playwright's `getAttribute`. `textContent()` (raw DOM text)
is `getDomProperty("textContent")`; `getText()` is the rendered, trimmed text. `inputValue()` is
`getDomProperty("value")`; `selectOption({ value })` is `new Select(el).selectByValue(v)`.

## Java Patterns

- `record` for small value types (`KnownIssue`, `VisualComparator.Result`, `AccessibilityChecker.Report`).
- `enum` with a field for the TS string union `FilterType` → `SortOption.LOHI.value()`.
- `BigDecimal` for prices: `equals` treats `2.4` and `2.40` as different, so compare with `compareTo`.
- JS `Array.sort()` and Java `String.compareTo` both sort by UTF-16 code unit, so expected sort orders port 1:1.
- Playwright `fill()` clears the field first; Selenium `sendKeys()` appends, so page objects `clear()` first.

## Accessibility Testing

**Playwright:** `new AxeBuilder({ page }).withTags([...]).analyze()`.
**Selenium:** `new AxeBuilder().withTags(List.of(...)).analyze(driver)` from `com.deque.html.axe-core:selenium`.
The results have the same shape. `Node.getTarget()` is typed `Object`, because targets can be nested lists for
iframes and shadow DOM.

**Stale allow-lists:** saucedemo fixed the `select-name` violation (the sort dropdown now has
`aria-label="Sort products"`), so the known-issue entry no longer matches anything, and the TS suite can't tell.
`AccessibilityChecker` lists allow-listed issues axe no longer reports under "Stale Known Issues" and logs a
warning; the test still passes.

`withTags(wcag…)` excludes best-practice rules such as `page-has-heading-one`, which an untagged scan reports.

## Visual Regression Testing

**Playwright** `toHaveScreenshot()` does everything: full-page capture, animation freezing, waiting for a stable
page, pixelmatch comparison, baseline management.

**Here** it's built from parts:
- `FullPageScreenshot`: Chrome uses CDP `Page.captureScreenshot` with `captureBeyondViewport` at device scale
  factor 1 (otherwise a Retina Mac gives a 2x image); Firefox has `getFullPageScreenshotAs`; Safari can only
  capture the viewport. Animations, transitions and the caret are disabled with injected CSS; the capture
  waits for images and fonts, then until two shots in a row are identical.
- `VisualComparator`: a port of pixelmatch's YIQ colour delta with threshold 0.2, at most
  min(`maxDiffPixels` 100, `maxDiffPixelRatio` 1%) differing pixels (Playwright takes the stricter of the two),
  and pixelmatch's **anti-aliasing detection**. The plan originally left anti-aliasing out; the Chrome visual tests
  then failed at random on text edges (114 and 143 differing pixels). With the port they passed 6 runs in a row.
- Baselines are per browser and OS: `<name>-<browser>-<os>.png`. A missing baseline is written from the actual
  image and the test fails, like Playwright.

## Reporting and Attachments

- `testInfo.attach(name, { body })` embeds the screenshot in Playwright's JSON report (about 70 MB). Here
  attachments are files, referenced from the JUnit XML's `<system-out>` as `[[ATTACHMENT|path]]`.
- **Surefire's `<properties>` leak:** `enablePropertiesElement=false` only works for passing tests. Failed,
  rerun and skipped tests still get every JVM system property, including the home directory and temp paths.
  There's no setting for it, so `scripts/SanitizeReport.java` removes the block before CI uploads.
- Retries: Surefire + JUnit 5 records each attempt (`<flakyFailure>`); TestNG would report retries as skips.

## CI/CD Integration

- Chrome and Firefox are preinstalled on `windows-latest`; Selenium Manager resolves the drivers.
- Safari needs `sudo safaridriver --enable` and macOS. `safaridriver` allows one session at a time, so Safari
  can't run in parallel.
- `upload-artifact` strips the common path prefix; three browser jobs each upload their XML, and a final job merges
  them with `separate-directories: true`, so the artifact has `chrome/`, `firefox/`, `safari/` at its root.
- `fail-fast: false` keeps one browser's failure from cancelling the others.

## Browser-Specific Findings

| Finding | Evidence | Handling |
|---|---|---|
| Chrome shows a "change your password" dialog for `secret_sauce` (it's in breach lists) and blocks clicks | first Chrome runs | password manager and leak detection turned off in `ChromeOptions` |
| Headless Chrome viewport 1280x577, Firefox 1280x635 for a 1280x720 window | `DriverFactoryTest` | viewport sized by measurement |
| Safari stops rendering when the display sleeps or the screen locks | first full Safari run: 48 failures, black 1153x695 screenshots after the display slept at 22:33 | run Safari with `caffeinate -d`, screen unlocked |
| Safari's Tab key skips buttons unless "Press Tab to highlight each item" is on | tab-order test never focuses the Login button | skipped on Safari |
| In WebDriver-driven Safari 26.6, saucedemo never saves the cart to `localStorage` (badge stays at 1, cart page empty); in normal Safari the same steps show 3 items | probe: Chrome writes `cart-contents` `[4]`→`[4,0]`→`[4,0,2]`, Safari writes nothing, yet `localStorage.setItem` from a test script works | 2 cart tests skipped on Safari |
| Safari can only screenshot the viewport | SafariDriver API | Safari baselines are viewport-only |

## Findings About the Playwright Suite

Porting line by line exposed checks in the TS suite that can't fail or no longer match the site.
They're kept as-is in the port, for parity, and listed in [TODO.md](../TODO.md).

- saucedemo renamed the footer's Twitter link to X; the TS footer test fails in CI, but the run shows green
  because of `continue-on-error`.
- `verifyAllProductImagesDisplayed` uses `[data-test="inventory-item-img"]`, which matches nothing, so its loop
  never runs.
- `verifyMenuIsClosed` checks only `aria-hidden="true"`, which the menu already has before it's ever opened.
- `count()` followed by a loop over `nth(i)` passes without checking anything when the list hasn't rendered yet.
- Several tests destructure fixtures they never use, and several assertion methods are never called.
