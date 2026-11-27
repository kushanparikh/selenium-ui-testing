package io.github.kushanparikh.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Port of pages/cartPage.ts. */
public class CartPage extends BasePage {

    private static final By SECONDARY_TITLE = By.cssSelector("[data-test='title']");
    private static final By CART_ITEM = By.cssSelector("[data-test='inventory-item']");
    private static final By ITEM_NAME = By.cssSelector("[data-test='inventory-item-name']");
    private static final By ITEM_PRICE = By.cssSelector("[data-test='inventory-item-price']");
    private static final By ITEM_QUANTITY = By.cssSelector("[data-test='item-quantity']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CONTINUE_SHOPPING = By.cssSelector("[data-test='continue-shopping']");
    private static final By CHECKOUT = By.cssSelector("[data-test='checkout']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    // Actions

    public void navigateToCart() {
        waits().click(CART_LINK);
    }

    public void removeItemFromCart(String itemTestId) {
        waits().click(getRemoveButton(itemTestId));
    }

    public void clickContinueShopping() {
        waits().click(CONTINUE_SHOPPING);
    }

    public void clickCheckout() {
        waits().click(CHECKOUT);
    }

    // Getters

    public By getSecondaryTitle() {
        return SECONDARY_TITLE;
    }

    public By getCartItems() {
        return CART_ITEM;
    }

    /**
     * Playwright's filter({ hasText }): an item name whose text contains the given string. Playwright matches
     * case-insensitively; XPath contains() is case-sensitive. No caller passes anything but an exact name.
     */
    public By getCartItemName(String itemTestId) {
        return By.xpath("//*[@data-test='inventory-item-name'][contains(normalize-space(.), " + xpathLiteral(itemTestId) + ")]");
    }

    public By getAllCartItemNames() {
        return ITEM_NAME;
    }

    public By getAllCartItemPrices() {
        return ITEM_PRICE;
    }

    public By getCartQuantity() {
        return ITEM_QUANTITY;
    }

    public By getAllCartQuantities() {
        return ITEM_QUANTITY;
    }

    public By getRemoveButton(String itemTestId) {
        return By.cssSelector("[data-test='remove-" + itemTestId + "']");
    }

    public By getContinueShoppingButton() {
        return CONTINUE_SHOPPING;
    }

    public By getCheckoutButton() {
        return CHECKOUT;
    }

    public By getCartBadge() {
        return CART_BADGE;
    }

    /** An XPath string literal for any text, including one that holds both quote kinds. */
    private static String xpathLiteral(String text) {
        if (!text.contains("'")) {
            return "'" + text + "'";
        }
        if (!text.contains("\"")) {
            return "\"" + text + "\"";
        }
        return "concat('" + text.replace("'", "', \"'\", '") + "')";
    }
}
