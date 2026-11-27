package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.CartPage;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Port of assertions/cartAssertions.ts. Each check that is a retrying expect(locator) in the TS waits here.
 * The TS reads page.url()/page.title() once; here they are waited for, since a click may not have navigated yet.
 * Checks the TS makes on a one-shot allTextContents()/count() read once.
 */
public class CartAssertions {

    private final CartPage cartPage;

    public CartAssertions(CartPage cartPage) {
        this.cartPage = cartPage;
    }

    public void verifyCartPageUrl() {
        cartPage.waits().urlContains("/cart.html");
    }

    public void verifyCartPageTitle() {
        cartPage.waits().until("title containing 'Swag Labs'", d -> d.getTitle().contains("Swag Labs"));
    }

    public void verifySecondaryTitle(String expectedText) {
        cartPage.waits().text(cartPage.getSecondaryTitle(), expectedText);
    }

    /** Playwright toHaveCount: waits until exactly this many cart items are present. */
    public void verifyCartItemCount(int expectedCount) {
        cartPage.waits().until("exactly " + expectedCount + " cart items",
                d -> d.findElements(cartPage.getCartItems()).size() == expectedCount);
    }

    public void verifyCartIsEmpty() {
        verifyCartItemCount(0);
    }

    public void verifyItemInCart(String itemName) {
        assertThat(texts(cartPage.waits().all(cartPage.getAllCartItemNames()))).contains(itemName);
    }

    /** One-shot read like the TS allTextContents(); an empty cart passes. */
    public void verifyItemNotInCart(String itemName) {
        List<String> items = cartPage.waits().until("cart item name lookup",
                d -> texts(d.findElements(cartPage.getAllCartItemNames())));
        assertThat(items).doesNotContain(itemName);
    }

    public void verifyAllItemsInCart(List<String> itemNames) {
        List<String> cartItemNames = texts(cartPage.waits().all(cartPage.getAllCartItemNames()));
        for (String name : itemNames) {
            assertThat(cartItemNames).contains(name);
        }
    }

    /** Playwright's toHaveText on a strict locator would fail on several quantities; this checks the first. */
    public void verifyItemQuantity(String expectedQuantity) {
        cartPage.waits().text(cartPage.getCartQuantity(), expectedQuantity);
    }

    /** Like the TS count(): a one-shot read, so an empty cart passes vacuously; each quantity then waits. */
    public void verifyAllQuantities(String expectedQuantity) {
        List<WebElement> quantities = cartPage.waits().until("cart quantity lookup",
                d -> d.findElements(cartPage.getAllCartQuantities()));

        for (WebElement quantity : quantities) {
            cartPage.waits().until("quantity text '" + expectedQuantity + "'",
                    d -> expectedQuantity.equals(quantity.getText()));
        }
    }

    public void verifyContinueShoppingButtonVisible() {
        cartPage.waits().visible(cartPage.getContinueShoppingButton());
    }

    public void verifyCheckoutButtonVisible() {
        cartPage.waits().visible(cartPage.getCheckoutButton());
    }

    public void verifyRemoveButtonVisible(String itemTestId) {
        cartPage.waits().visible(cartPage.getRemoveButton(itemTestId));
    }

    public void verifyCartBadgeCount(String expectedCount) {
        cartPage.waits().text(cartPage.getCartBadge(), expectedCount);
    }

    /** Waits for the badge to go away (Review Focus 1); passes at once if it was never there. */
    public void verifyCartBadgeNotVisible() {
        cartPage.waits().absent(cartPage.getCartBadge());
    }

    public void verifyInventoryUrl() {
        cartPage.waits().urlContains("/inventory.html");
    }

    public void verifyCheckoutStepOneUrl() {
        cartPage.waits().urlContains("/checkout-step-one.html");
    }

    private static List<String> texts(List<WebElement> elements) {
        return elements.stream().map(WebElement::getText).toList();
    }
}
