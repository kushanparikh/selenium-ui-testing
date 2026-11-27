package io.github.kushanparikh.selenium.tests;

import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Port of tests/productDetail.spec.ts.
 */
@Tag("functional")
class ProductDetailTest extends BaseTest {

    @BeforeEach
    void openLoginPage() {
        open();
        loginAssertions.verifyLoginLandingDetails();
    }

    @Test
    @DisplayName("should navigate to product detail page from inventory and display correct information")
    void navigateToProductDetailAndDisplayInformation() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        productDetailPage.navigateToProductDetailFromInventory("Sauce Labs Backpack");

        productDetailAssertions.verifyProductDetailPageLoaded();

        attachScreenshot("product-detail-page-loaded");

        productDetailAssertions.verifyProductInformation(
                "Sauce Labs Backpack",
                "$29.99",
                "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with"
                        + " unequaled laptop and tablet protection.");

        productDetailAssertions.verifyProductImageDisplayed();

        productDetailAssertions.verifyAddToCartButtonVisible();

        productDetailAssertions.verifyBackToProductsButtonVisible();

        attachScreenshot("product-detail-page-verified");
    }

    @Test
    @DisplayName("should add item to cart from product detail page")
    void addItemToCartFromProductDetail() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        productDetailPage.navigateToProductDetailFromInventory("Sauce Labs Bike Light");
        productDetailAssertions.verifyProductDetailPageLoaded();

        productDetailAssertions.verifyCartBadgeNotVisible();

        productDetailPage.addItemToCart();

        attachScreenshot("product-detail-after-add-to-cart");

        productDetailAssertions.verifyCartBadgeCount("1");

        productDetailAssertions.verifyRemoveButtonVisible();

        attachScreenshot("product-detail-cart-updated");
    }

    @Test
    @DisplayName("should remove item from cart on product detail page")
    void removeItemFromCartOnProductDetail() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        productDetailPage.navigateToProductDetailFromInventory("Sauce Labs Bolt T-Shirt");
        productDetailAssertions.verifyProductDetailPageLoaded();

        productDetailPage.addItemToCart();
        productDetailAssertions.verifyCartBadgeCount("1");
        productDetailAssertions.verifyRemoveButtonVisible();

        attachScreenshot("product-detail-before-remove");

        productDetailPage.removeItemFromCart();

        productDetailAssertions.verifyCartBadgeNotVisible();

        productDetailAssertions.verifyAddToCartButtonVisible();

        attachScreenshot("product-detail-after-remove");
    }

    @Test
    @DisplayName("should navigate back to products page using Back to Products button")
    void navigateBackToProducts() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        productDetailPage.navigateToProductDetailFromInventory("Sauce Labs Fleece Jacket");
        productDetailAssertions.verifyProductDetailPageLoaded();

        attachScreenshot("on-product-detail-page");

        productDetailPage.clickBackToProducts();

        productDetailAssertions.verifyNavigatedBackToInventory();
        inventoryAssertions.verifyInventoryLandingDetails();

        attachScreenshot("back-on-inventory-page");
    }

    @Test
    @DisplayName("should maintain cart state when navigating between product detail and inventory pages")
    void maintainCartStateAcrossPages() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        productDetailPage.navigateToProductDetailFromInventory("Sauce Labs Onesie");
        productDetailAssertions.verifyProductDetailPageLoaded();

        productDetailPage.addItemToCart();
        productDetailAssertions.verifyCartBadgeCount("1");

        attachScreenshot("first-item-added");

        productDetailPage.clickBackToProducts();
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryAssertions.verifyCartBadgeCount("1");

        productDetailPage.navigateToProductDetailFromInventory("Test.allTheThings() T-Shirt (Red)");
        productDetailAssertions.verifyProductDetailPageLoaded();

        productDetailPage.addItemToCart();
        productDetailAssertions.verifyCartBadgeCount("2");

        attachScreenshot("second-item-added");

        productDetailPage.clickBackToProducts();
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryAssertions.verifyCartBadgeCount("2");

        attachScreenshot("cart-state-maintained");
    }
}
