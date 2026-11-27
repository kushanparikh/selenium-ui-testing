package io.github.kushanparikh.selenium.tests.visual;

import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.DriverExtension;
import io.github.kushanparikh.selenium.support.Users;
import io.github.kushanparikh.selenium.support.VisualComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Port of tests/visual/visualregression.spec.ts. toHaveScreenshot(name, { fullPage: true }) becomes
 * VisualComparator.assertMatches with the same thresholds; baselines are per browser and OS.
 */
@Tag("visual")
@DisplayName("Visual Regression Tests")
class VisualRegressionTest extends BaseTest {

    @BeforeEach
    void openLoginPage() {
        open();
        loginAssertions.verifyLoginLandingDetails();
    }

    @Test
    @DisplayName("login page appearance")
    void loginPageAppearance() {
        VisualComparator.assertMatches(driver, DriverExtension.currentContext(), "login-page");
    }

    @Test
    @DisplayName("inventory page appearance")
    void inventoryPageAppearance() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        VisualComparator.assertMatches(driver, DriverExtension.currentContext(), "inventory-page");
    }

    @Test
    @DisplayName("cart with items appearance")
    void cartWithItemsAppearance() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();
        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryPage.clickCartLink();
        // Playwright's screenshot retries until the page is stable; waiting for the cart URL makes sure the
        // stable page is the cart and not the inventory page it navigated away from.
        cartPage.waits().urlContains("cart.html");

        VisualComparator.assertMatches(driver, DriverExtension.currentContext(), "cart-page-with-items");
    }
}
