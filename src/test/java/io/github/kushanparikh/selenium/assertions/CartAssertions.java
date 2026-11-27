package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.CartPage;

/** Port of assertions/cartAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class CartAssertions {

    private final CartPage cartPage;

    public CartAssertions(CartPage cartPage) {
        this.cartPage = cartPage;
    }
}
