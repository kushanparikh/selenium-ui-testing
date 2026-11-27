# Coverage and Open Items

Status of every Playwright test suite in this port, the known differences, and what is still open.
The task board (`backlog board`) tracks the work itself.

## Coverage vs the Playwright suite

| Suite | Playwright | Selenium | Chrome | Firefox | Safari |
|---|---|---|---|---|---|
| Login | 13 | 13 | ✅ 13 | ✅ 13 | ✅ 12, ⏭ 1 (tab order) |
| Inventory | 13 | 13 | ✅ | ✅ | ✅ |
| Filter | 3 | 3 | ✅ | ✅ | ✅ |
| Product detail | 5 | 5 | ✅ | ✅ | ✅ |
| Cart | 8 | 8 | ✅ | ✅ | ✅ 7, ⏭ 1 (cart not saved) |
| Checkout | 17 | 17 | ✅ | ✅ | ✅ |
| Hamburger menu | 6 | 6 | ✅ | ✅ | ✅ |
| Footer | 4 | 4 | ✅ | ✅ | ✅ |
| E2E purchase | 10 | 10 | ✅ | ✅ | ✅ 9, ⏭ 1 (cart not saved) |
| Visual regression | 3 | 3 | ✅ | ✅ | ✅ (viewport only) |
| Accessibility | 3 | 3 | ✅ | ✅ | ✅ |
| **Total** | **85** | **85** | **85** | **85** | **82 + 3 skipped** |

Local results on macOS. CI results (Windows for Chrome/Firefox, macOS for Safari) are on the PR.

## Known differences from the Playwright suite

| Difference | Why | Where |
|---|---|---|
| Footer test accepts `social-x` / `x.com` as well as `social-twitter` / `twitter.com` | saucedemo renamed the link; the TS test fails on it in CI (hidden by `continue-on-error`) | `FooterPage`, `FooterAssertions` |
| "Stale Known Issues" section in accessibility reports | saucedemo fixed `select-name`; the allow-list entry is kept | `AccessibilityChecker` |
| Tab-order test skipped on Safari | Safari's Tab key skips buttons by default | `LoginTest` |
| Cart "remove item" and e2e "reset app state" skipped on Safari | saucedemo doesn't save the cart in WebDriver-driven Safari; normal Safari works | `CartTest`, `CompletePurchaseTest` |
| Cart count checks wait | TS reads the count once, relying on Playwright's click waiting for navigation | e2e, cart |
| `waitForTimeout(500)` calls dropped | the following badge-absent wait covers them | e2e, hamburger menu |
| "remove all items" checks the badge before the empty cart | in the TS order the empty-cart wait hides whether the badge check waits | `CartTest` |
| Visual comparison counts anti-aliasing exactly like pixelmatch; Safari baselines are viewport-only | SafariDriver has no full-page capture | `VisualComparator`, `FullPageScreenshot` |

## Checks in the Playwright suite that can't fail

Ported as-is for parity. Fixing them means changing both repos.

- [ ] `verifyAllProductImagesDisplayed`: `[data-test="inventory-item-img"]` matches nothing on saucedemo, so the
      loop never runs.
- [ ] `verifyMenuIsClosed` checks only `aria-hidden="true"`, which the menu has before it's ever opened.
- [ ] `verifyAllQuantities` / other `count()` + `nth(i)` loops pass with nothing checked if the list hasn't
      rendered yet.
- [ ] "should display empty cart state": `verifyCartBadgeNotVisible` can't fail, since nothing was added.
- [ ] The TS footer test fails today (Twitter→X) but CI shows green.

## Open items

- [ ] Commit Windows and CI-macOS visual baselines from the first CI run (TASK-14).
- [ ] Parallel Chrome/Firefox and a Safari critical subset, after measuring CI durations (TASK-17).
- [ ] unified-test-reporting: a JUnit XML adapter for this repo's `utr-results` (separate project).
- [ ] Decide whether to update the Playwright repo for the Twitter→X rename.
