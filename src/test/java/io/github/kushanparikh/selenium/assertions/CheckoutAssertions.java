package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.CheckoutPage;

/** Port of assertions/checkoutAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class CheckoutAssertions {

    private final CheckoutPage checkoutPage;

    public CheckoutAssertions(CheckoutPage checkoutPage) {
        this.checkoutPage = checkoutPage;
    }
}
