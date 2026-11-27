package io.github.kushanparikh.selenium.tests;

import io.github.kushanparikh.selenium.pages.SortOption;
import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

/** Port of tests/inventory.spec.ts. */
@Tag("functional")
class InventoryTest extends BaseTest {

    private static final String BACKPACK = "sauce-labs-backpack";
    private static final String BIKE_LIGHT = "sauce-labs-bike-light";
    private static final String BOLT_T_SHIRT = "sauce-labs-bolt-t-shirt";
    private static final String FLEECE_JACKET = "sauce-labs-fleece-jacket";
    private static final String ONESIE = "sauce-labs-onesie";
    private static final String RED_T_SHIRT = "test.allthethings()-t-shirt-(red)";

    @BeforeEach
    void openLoginPage() {
        open();
        loginAssertions.verifyLoginLandingDetails();
    }

    @Test
    @DisplayName("should add Sauce Labs Backpack to cart")
    void addBackpackToCart() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart(BACKPACK);

        attachScreenshot("cart-page-after-adding-backpack");

        inventoryAssertions.verifyCartBadgeCount("1");
        inventoryAssertions.verifyCartLinkContainsCount("1");
        inventoryAssertions.verifyRemoveButtonText(BACKPACK, "Remove");

        attachScreenshot("cart-page-after-adding-backpack-final");
    }

    @Test
    @DisplayName("should add Sauce Labs Backpack and Bike Light to cart")
    void addBackpackAndBikeLightToCart() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart(BACKPACK);

        attachScreenshot("cart-page-after-adding-backpack");

        inventoryPage.addItemToCart(BIKE_LIGHT);

        inventoryAssertions.verifyCartBadgeCount("2");
        inventoryAssertions.verifyCartLinkContainsCount("2");
        inventoryAssertions.verifyRemoveButtonText(BACKPACK, "Remove");
        inventoryAssertions.verifyRemoveButtonText(BIKE_LIGHT, "Remove");

        attachScreenshot("cart-page-after-adding-bike-light");
    }

    @Test
    @DisplayName("should remove single item from cart")
    void removeSingleItem() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart(BACKPACK);
        inventoryAssertions.verifyCartBadgeCount("1");

        inventoryPage.removeItemFromCart(BACKPACK);

        inventoryAssertions.verifyCartBadgeNotVisible();
        inventoryAssertions.verifyAddToCartButtonExists(BACKPACK);

        attachScreenshot("cart-after-removing-item");
    }

    @Test
    @DisplayName("should remove multiple items from cart")
    void removeMultipleItems() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart(BACKPACK);
        inventoryPage.addItemToCart(BIKE_LIGHT);
        inventoryPage.addItemToCart(BOLT_T_SHIRT);
        inventoryAssertions.verifyCartBadgeCount("3");

        inventoryPage.removeItemFromCart(BACKPACK);
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.removeItemFromCart(BIKE_LIGHT);
        inventoryAssertions.verifyCartBadgeCount("1");

        inventoryPage.removeItemFromCart(BOLT_T_SHIRT);
        inventoryAssertions.verifyCartBadgeNotVisible();

        attachScreenshot("cart-after-removing-all-items");
    }

    @Test
    @DisplayName("should update cart badge correctly when adding and removing items")
    void cartBadgeUpdates() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryAssertions.verifyCartBadgeNotVisible();

        inventoryPage.addItemToCart(BACKPACK);
        inventoryAssertions.verifyCartBadgeCount("1");

        inventoryPage.addItemToCart(BIKE_LIGHT);
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.removeItemFromCart(BACKPACK);
        inventoryAssertions.verifyCartBadgeCount("1");

        inventoryPage.addItemToCart(BOLT_T_SHIRT);
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.removeItemFromCart(BIKE_LIGHT);
        inventoryPage.removeItemFromCart(BOLT_T_SHIRT);
        inventoryAssertions.verifyCartBadgeNotVisible();

        attachScreenshot("cart-badge-updates");
    }

    @Test
    @DisplayName("should hide cart badge when cart is empty")
    void hideBadgeWhenCartEmpty() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryAssertions.verifyCartBadgeNotVisible();

        inventoryPage.addItemToCart(BACKPACK);
        inventoryAssertions.verifyCartBadgeCount("1");

        inventoryPage.removeItemFromCart(BACKPACK);
        inventoryAssertions.verifyCartBadgeNotVisible();

        attachScreenshot("empty-cart-no-badge");
    }

    @Test
    @DisplayName("should display all 6 products correctly")
    void displaysAllSixProducts() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryAssertions.verifyInventoryItemCount(6);

        attachScreenshot("all-6-products-displayed");
    }

    @Test
    @DisplayName("should display correct product names")
    void displaysCorrectProductNames() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        List<String> expectedProductNames = List.of(
                "Sauce Labs Backpack",
                "Sauce Labs Bike Light",
                "Sauce Labs Bolt T-Shirt",
                "Sauce Labs Fleece Jacket",
                "Sauce Labs Onesie",
                "Test.allTheThings() T-Shirt (Red)");

        inventoryAssertions.verifyProductNamesExist(expectedProductNames);

        attachScreenshot("product-names-verification");
    }

    @Test
    @DisplayName("should display correct product prices")
    void displaysCorrectProductPrices() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        List<String> expectedPrices = List.of("$29.99", "$9.99", "$15.99", "$49.99", "$7.99", "$15.99");

        inventoryAssertions.verifyProductPrices(expectedPrices);

        attachScreenshot("product-prices-verification");
    }

    @Test
    @DisplayName("should display product images without errors")
    void displaysProductImages() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryAssertions.verifyAllProductImagesDisplayed();

        attachScreenshot("product-images-displayed");
    }

    @Test
    @DisplayName("should have default sort order as A-Z")
    void defaultSortOrderIsAz() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryAssertions.verifyDefaultSortOrder();
        inventoryAssertions.verifyProductsSorted(SortOption.AZ);

        attachScreenshot("default-sort-order");
    }

    @Test
    @DisplayName("should add all items to cart")
    void addAllItemsToCart() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart(BACKPACK);
        inventoryPage.addItemToCart(BIKE_LIGHT);
        inventoryPage.addItemToCart(BOLT_T_SHIRT);
        inventoryPage.addItemToCart(FLEECE_JACKET);
        inventoryPage.addItemToCart(ONESIE);
        inventoryPage.addItemToCart(RED_T_SHIRT);

        inventoryAssertions.verifyCartBadgeCount("6");

        inventoryAssertions.verifyRemoveButtonExists(BACKPACK);
        inventoryAssertions.verifyRemoveButtonExists(BIKE_LIGHT);
        inventoryAssertions.verifyRemoveButtonExists(BOLT_T_SHIRT);
        inventoryAssertions.verifyRemoveButtonExists(FLEECE_JACKET);
        inventoryAssertions.verifyRemoveButtonExists(ONESIE);
        inventoryAssertions.verifyRemoveButtonExists(RED_T_SHIRT);

        attachScreenshot("all-items-added-to-cart");
    }

    @Test
    @DisplayName("should remove all items from cart")
    void removeAllItemsFromCart() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart(BACKPACK);
        inventoryPage.addItemToCart(BIKE_LIGHT);
        inventoryPage.addItemToCart(BOLT_T_SHIRT);
        inventoryPage.addItemToCart(FLEECE_JACKET);
        inventoryPage.addItemToCart(ONESIE);
        inventoryPage.addItemToCart(RED_T_SHIRT);
        inventoryAssertions.verifyCartBadgeCount("6");

        inventoryPage.removeItemFromCart(BACKPACK);
        inventoryPage.removeItemFromCart(BIKE_LIGHT);
        inventoryPage.removeItemFromCart(BOLT_T_SHIRT);
        inventoryPage.removeItemFromCart(FLEECE_JACKET);
        inventoryPage.removeItemFromCart(ONESIE);
        inventoryPage.removeItemFromCart(RED_T_SHIRT);

        inventoryAssertions.verifyCartBadgeNotVisible();

        inventoryAssertions.verifyAddToCartButtonExists(BACKPACK);
        inventoryAssertions.verifyAddToCartButtonExists(BIKE_LIGHT);
        inventoryAssertions.verifyAddToCartButtonExists(BOLT_T_SHIRT);
        inventoryAssertions.verifyAddToCartButtonExists(FLEECE_JACKET);
        inventoryAssertions.verifyAddToCartButtonExists(ONESIE);
        inventoryAssertions.verifyAddToCartButtonExists(RED_T_SHIRT);

        attachScreenshot("all-items-removed-from-cart");
    }
}
