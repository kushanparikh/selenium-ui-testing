package io.github.kushanparikh.selenium.tests;

import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/** Port of tests/cart.spec.ts. */
class CartTest extends BaseTest {

    private static final String BACKPACK = "sauce-labs-backpack";
    private static final String BIKE_LIGHT = "sauce-labs-bike-light";
    private static final String BOLT_T_SHIRT = "sauce-labs-bolt-t-shirt";

    @BeforeEach
    void openLoginPage() {
        open();
        loginAssertions.verifyLoginLandingDetails();
    }

    @Test
    @DisplayName("should navigate to cart page")
    void navigateToCartPage() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        cartPage.navigateToCart();

        cartAssertions.verifyCartPageUrl();
        cartAssertions.verifyCartPageTitle();
        cartAssertions.verifySecondaryTitle("Your Cart");

        attachScreenshot("cart-page-navigation");
    }

    @Test
    @DisplayName("should display correct items in cart")
    void displaysCorrectItems() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryPage.addItemToCart(BACKPACK);
        inventoryPage.addItemToCart(BIKE_LIGHT);
        inventoryPage.addItemToCart(BOLT_T_SHIRT);

        cartPage.navigateToCart();

        cartAssertions.verifyCartItemCount(3);
        cartAssertions.verifyAllItemsInCart(List.of(
                "Sauce Labs Backpack",
                "Sauce Labs Bike Light",
                "Sauce Labs Bolt T-Shirt"));

        attachScreenshot("cart-displays-correct-items");
    }

    @Test
    @DisplayName("should display correct quantities")
    void displaysCorrectQuantities() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryPage.addItemToCart(BACKPACK);
        inventoryPage.addItemToCart(BIKE_LIGHT);

        cartPage.navigateToCart();

        cartAssertions.verifyAllQuantities("1");

        attachScreenshot("cart-quantities");
    }

    @Test
    @DisplayName("should remove item from cart page")
    void removeItemFromCartPage() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryPage.addItemToCart(BACKPACK);
        inventoryPage.addItemToCart(BIKE_LIGHT);
        inventoryPage.addItemToCart(BOLT_T_SHIRT);

        cartPage.navigateToCart();
        cartAssertions.verifyCartItemCount(3);

        cartPage.removeItemFromCart(BIKE_LIGHT);

        cartAssertions.verifyCartItemCount(2);
        cartAssertions.verifyItemNotInCart("Sauce Labs Bike Light");
        cartAssertions.verifyItemInCart("Sauce Labs Backpack");
        cartAssertions.verifyItemInCart("Sauce Labs Bolt T-Shirt");

        attachScreenshot("cart-item-removed");
    }

    @Test
    @DisplayName("should remove all items from cart")
    void removeAllItemsFromCart() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryPage.addItemToCart(BACKPACK);
        inventoryPage.addItemToCart(BIKE_LIGHT);
        inventoryPage.addItemToCart(BOLT_T_SHIRT);

        cartPage.navigateToCart();
        cartAssertions.verifyCartItemCount(3);

        cartPage.removeItemFromCart(BACKPACK);
        cartPage.removeItemFromCart(BIKE_LIGHT);
        cartPage.removeItemFromCart(BOLT_T_SHIRT);

        // Review Focus 1: straight after the last removal, with no wait in between. It must pass by waiting
        // for the badge to go, not throw NoSuchElementException or pass while the badge is still shown.
        cartAssertions.verifyCartBadgeNotVisible();
        cartAssertions.verifyCartIsEmpty();

        attachScreenshot("cart-all-items-removed");
    }

    @Test
    @DisplayName("should return to inventory when continue shopping is clicked")
    void continueShoppingReturnsToInventory() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryPage.addItemToCart(BACKPACK);
        cartPage.navigateToCart();
        cartAssertions.verifyCartPageUrl();

        cartPage.clickContinueShopping();

        cartAssertions.verifyInventoryUrl();

        attachScreenshot("continue-shopping-navigation");
    }

    @Test
    @DisplayName("should navigate to checkout when checkout button is clicked")
    void checkoutNavigatesToStepOne() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryPage.addItemToCart(BACKPACK);
        inventoryPage.addItemToCart(BIKE_LIGHT);
        cartPage.navigateToCart();
        cartAssertions.verifyCartPageUrl();

        cartPage.clickCheckout();

        cartAssertions.verifyCheckoutStepOneUrl();

        attachScreenshot("checkout-navigation");
    }

    @Test
    @DisplayName("should display empty cart state")
    void displaysEmptyCartState() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        cartPage.navigateToCart();

        cartAssertions.verifyCartIsEmpty();
        cartAssertions.verifyCartBadgeNotVisible();

        cartAssertions.verifyContinueShoppingButtonVisible();
        cartAssertions.verifyCheckoutButtonVisible();

        attachScreenshot("empty-cart-state");
    }
}
