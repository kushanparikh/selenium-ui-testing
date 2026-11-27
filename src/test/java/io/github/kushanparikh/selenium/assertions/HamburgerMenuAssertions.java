package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.HamburgerMenuPage;

/** Port of assertions/hamburgerMenuAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class HamburgerMenuAssertions {

    private final HamburgerMenuPage hamburgerMenuPage;

    public HamburgerMenuAssertions(HamburgerMenuPage hamburgerMenuPage) {
        this.hamburgerMenuPage = hamburgerMenuPage;
    }
}
