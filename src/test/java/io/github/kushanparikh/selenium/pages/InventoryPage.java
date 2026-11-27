package io.github.kushanparikh.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/** Port of pages/inventoryPage.ts. */
public class InventoryPage extends BasePage {

    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_LINK_CLASS = By.cssSelector(".shopping_cart_link");
    private static final By ITEM_PRICE = By.cssSelector("[data-test='inventory-item-price']");
    private static final By ITEM_NAME = By.cssSelector("[data-test='inventory-item-name']");
    private static final By INVENTORY_ITEM = By.cssSelector("[data-test='inventory-item']");
    private static final By INVENTORY_ITEM_IMG = By.cssSelector("[data-test='inventory-item-img']");
    private static final By PRODUCT_SORT_CONTAINER = By.cssSelector("[data-test='product-sort-container']");
    private static final By PRODUCT_IMAGES = By.cssSelector(".inventory_item_img img");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // Actions

    public void addItemToCart(String itemTestId) {
        waits().clickable(getAddToCartButton(itemTestId)).click();
    }

    public void removeItemFromCart(String itemTestId) {
        waits().clickable(getRemoveButton(itemTestId)).click();
    }

    /** Playwright's selectOption({ value }). */
    public void applyFilter(SortOption filterType) {
        new Select(waits().visible(PRODUCT_SORT_CONTAINER)).selectByValue(filterType.value());
    }

    /** The TS clicks the .shopping_cart_link class here, while getCartLink() uses data-test; both kept as-is. */
    public void clickCartLink() {
        waits().clickable(CART_LINK_CLASS).click();
    }

    // Getters

    /** Playwright getByText('Products'): an element whose text is "Products". */
    public By getSecondaryTitle() {
        return By.xpath("//*[normalize-space(text())='Products']");
    }

    public By getCartBadge() {
        return CART_BADGE;
    }

    public By getCartLink() {
        return CART_LINK;
    }

    public By getAddToCartButton(String itemTestId) {
        return By.cssSelector("[data-test='add-to-cart-" + itemTestId + "']");
    }

    public By getRemoveButton(String itemTestId) {
        return By.cssSelector("[data-test='remove-" + itemTestId + "']");
    }

    public By getItemPriceLocator() {
        return ITEM_PRICE;
    }

    public By getItemNameLocator() {
        return ITEM_NAME;
    }

    public By getAllInventoryItems() {
        return INVENTORY_ITEM;
    }

    public By getInventoryItemImage() {
        return INVENTORY_ITEM_IMG;
    }

    public By getProductSortContainer() {
        return PRODUCT_SORT_CONTAINER;
    }

    public By getProductImages() {
        return PRODUCT_IMAGES;
    }
}
