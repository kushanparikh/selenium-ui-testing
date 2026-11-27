package io.github.kushanparikh.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Port of pages/productDetailPage.ts. */
public class ProductDetailPage extends BasePage {

    private static final By PRODUCT_NAME = By.cssSelector("[data-test='inventory-item-name']");
    private static final By PRODUCT_DESCRIPTION = By.cssSelector("[data-test='inventory-item-desc']");
    private static final By PRODUCT_PRICE = By.cssSelector("[data-test='inventory-item-price']");
    private static final By PRODUCT_IMAGE = By.cssSelector("img.inventory_details_img");
    private static final By ADD_TO_CART = By.cssSelector("[data-test='add-to-cart']");
    private static final By REMOVE = By.cssSelector("[data-test='remove']");
    private static final By BACK_TO_PRODUCTS = By.cssSelector("[data-test='back-to-products']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    // Actions

    /**
     * Playwright's locator(...).filter({ hasText }).click() on the inventory page: hasText is a substring match and
     * click() is strict (exactly one match). Waits until exactly one visible item name contains the text, then clicks.
     */
    public void navigateToProductDetailFromInventory(String productName) {
        WebElement item = waits().until("exactly one " + PRODUCT_NAME + " containing '" + productName + "'",
                d -> {
                    List<WebElement> matches = d.findElements(PRODUCT_NAME).stream()
                            .filter(e -> e.isDisplayed() && e.getText().contains(productName))
                            .toList();
                    return matches.size() == 1 ? matches.get(0) : null;
                });
        item.click();
    }

    public void addItemToCart() {
        waits().click(ADD_TO_CART);
    }

    public void removeItemFromCart() {
        waits().click(REMOVE);
    }

    public void clickBackToProducts() {
        waits().click(BACK_TO_PRODUCTS);
    }

    // Getters

    public By getProductName() {
        return PRODUCT_NAME;
    }

    public By getProductDescription() {
        return PRODUCT_DESCRIPTION;
    }

    public By getProductPrice() {
        return PRODUCT_PRICE;
    }

    public By getProductImage() {
        return PRODUCT_IMAGE;
    }

    public By getAddToCartButton() {
        return ADD_TO_CART;
    }

    public By getRemoveButton() {
        return REMOVE;
    }

    public By getBackToProductsButton() {
        return BACK_TO_PRODUCTS;
    }

    public By getCartBadge() {
        return CART_BADGE;
    }

    public By getCartLink() {
        return CART_LINK;
    }
}
