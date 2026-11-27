package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.FooterPage;

/** Port of assertions/footerAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class FooterAssertions {

    private final FooterPage footerPage;

    public FooterAssertions(FooterPage footerPage) {
        this.footerPage = footerPage;
    }
}
