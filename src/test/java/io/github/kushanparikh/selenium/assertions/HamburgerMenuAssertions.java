package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.HamburgerMenuPage;

/** Port of assertions/hamburgerMenuAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class HamburgerMenuAssertions {

    private final HamburgerMenuPage hamburgerMenuPage;

    public HamburgerMenuAssertions(HamburgerMenuPage hamburgerMenuPage) {
        this.hamburgerMenuPage = hamburgerMenuPage;
    }

    /** toBeVisible() then toHaveCSS('visibility', 'visible'), both retried. */
    public void verifyMenuIsOpen() {
        hamburgerMenuPage.waits().visible(hamburgerMenuPage.getMenuContainer());
        hamburgerMenuPage.waits().until("CSS visibility 'visible' on " + hamburgerMenuPage.getMenuContainer(),
                d -> "visible".equals(d.findElement(hamburgerMenuPage.getMenuContainer()).getCssValue("visibility")));
    }

    /** toHaveAttribute('aria-hidden', 'true'): the raw attribute, hence getDomAttribute. */
    public void verifyMenuIsClosed() {
        hamburgerMenuPage.waits().until("aria-hidden='true' on " + hamburgerMenuPage.getMenuContainer(),
                d -> "true".equals(d.findElement(hamburgerMenuPage.getMenuContainer()).getDomAttribute("aria-hidden")));
    }

    public void verifyAllItemsLinkIsVisible() {
        hamburgerMenuPage.waits().visible(hamburgerMenuPage.getAllItemsLink());
    }

    public void verifyAboutLinkIsVisible() {
        hamburgerMenuPage.waits().visible(hamburgerMenuPage.getAboutLink());
    }

    public void verifyLogoutLinkIsVisible() {
        hamburgerMenuPage.waits().visible(hamburgerMenuPage.getLogoutLink());
    }

    public void verifyResetAppStateLinkIsVisible() {
        hamburgerMenuPage.waits().visible(hamburgerMenuPage.getResetAppStateLink());
    }

    public void verifyMenuButtonIsVisible() {
        hamburgerMenuPage.waits().visible(hamburgerMenuPage.getMenuButton());
    }

    public void verifyCloseButtonIsVisible() {
        hamburgerMenuPage.waits().visible(hamburgerMenuPage.getCloseButton());
    }
}
