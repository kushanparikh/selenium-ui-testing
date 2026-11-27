package io.github.kushanparikh.selenium.tests.e2e;

import io.github.kushanparikh.selenium.pages.SortOption;
import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Port of tests/e2e/completePurchase.spec.ts.
 * <p>
 * Translation notes: TS reads {@code cartPage.getCartItems().count()} once right after clicking the cart link,
 * which works because Playwright's click waits for the navigation; Selenium's doesn't, so the count check uses
 * {@code cartAssertions.verifyCartItemCount(n)}, which waits for the count. {@code page.waitForTimeout(500)} calls
 * are dropped: the badge-absent wait that follows them already waits for the reset.
 */
@DisplayName("End-to-End Purchase Workflows")
class CompletePurchaseTest extends BaseTest {

    @Test
    @DisplayName("should complete full purchase flow: Login → Add items → Cart → Checkout → Complete")
    void fullPurchaseFlow() {
        open();
        loginAssertions.verifyLoginLandingDetails();

        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.clickCartLink();
        inventoryPage.waits().urlContains("cart.html");
        cartAssertions.verifyCartPageUrl();
        cartAssertions.verifyCartItemCount(2);

        cartPage.clickCheckout();
        checkoutAssertions.verifyCheckoutInformationPageURL();

        checkoutPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutAssertions.verifyItemTotalIsDisplayed();
        checkoutAssertions.verifyTaxIsDisplayed();
        checkoutAssertions.verifyTotalPriceIsDisplayed();

        checkoutPage.clickFinish();
        checkoutAssertions.verifyCheckoutCompletePageURL();
        checkoutAssertions.verifyOrderConfirmationMessage();
        checkoutAssertions.verifyThankYouMessage();

        attachScreenshot("complete-purchase-flow");
    }

    @Test
    @DisplayName("should complete purchase with single item")
    void singleItemPurchase() {
        open();
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-onesie");
        inventoryAssertions.verifyCartBadgeCount("1");

        inventoryPage.clickCartLink();
        cartAssertions.verifyCartPageUrl();
        cartAssertions.verifyCartItemCount(1);

        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("Jane", "Smith", "54321");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutPage.clickFinish();
        checkoutAssertions.verifyCheckoutCompletePageURL();
        checkoutAssertions.verifyOrderConfirmationMessage();

        cartPage.waits().absent(cartPage.getCartBadge());

        attachScreenshot("single-item-purchase");
    }

    @Test
    @DisplayName("should complete purchase with multiple items")
    void multipleItemsPurchase() {
        open();
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryPage.addItemToCart("sauce-labs-bolt-t-shirt");
        inventoryPage.addItemToCart("sauce-labs-fleece-jacket");
        inventoryPage.addItemToCart("sauce-labs-onesie");
        inventoryPage.addItemToCart("test.allthethings()-t-shirt-(red)");
        inventoryAssertions.verifyCartBadgeCount("6");

        inventoryPage.clickCartLink();
        cartAssertions.verifyCartItemCount(6);

        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("Bob", "Johnson", "67890");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        checkoutAssertions.verifyCheckoutCompletePageURL();
        checkoutAssertions.verifyThankYouMessage();

        attachScreenshot("multiple-items-purchase");
    }

    @Test
    @DisplayName("should handle add item, remove item, add again workflow")
    void addRemoveAddWorkflow() {
        open();
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryAssertions.verifyCartBadgeCount("1");

        inventoryPage.removeItemFromCart("sauce-labs-backpack");
        inventoryPage.waits().absent(inventoryPage.getCartBadge());

        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryPage.addItemToCart("sauce-labs-onesie");
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.clickCartLink();
        cartAssertions.verifyCartPageUrl();

        cartPage.removeItemFromCart("sauce-labs-bike-light");
        cartAssertions.verifyCartItemCount(1);

        cartPage.clickContinueShopping();
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.clickCartLink();
        cartPage.clickCheckout();

        // The TS fills the form with raw locators here instead of the checkout page object.
        fill(By.cssSelector("[data-test='firstName']"), "Alice");
        fill(By.cssSelector("[data-test='lastName']"), "Williams");
        fill(By.cssSelector("[data-test='postalCode']"), "11111");
        checkoutPage.waits().click(By.cssSelector("[data-test='continue']"));
        checkoutPage.waits().click(By.cssSelector("[data-test='finish']"));

        checkoutAssertions.verifyCheckoutCompletePageURL();

        attachScreenshot("add-remove-add-workflow");
    }

    @Test
    @DisplayName("should sort products, add to cart, and checkout")
    void sortAddAndCheckout() {
        open();
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        // The TS also reads the item names into an unused variable here; that read is not ported.
        inventoryPage.applyFilter(SortOption.LOHI);

        inventoryPage.addItemToCart("sauce-labs-onesie");
        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.applyFilter(SortOption.HILO);
        inventoryPage.addItemToCart("sauce-labs-fleece-jacket");
        inventoryAssertions.verifyCartBadgeCount("3");

        inventoryPage.clickCartLink();
        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("Charlie", "Brown", "22222");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        checkoutAssertions.verifyCheckoutCompletePageURL();

        attachScreenshot("sort-and-purchase");
    }

    @Test
    @DisplayName("should navigate to product detail, add to cart, and checkout")
    void productDetailAddAndCheckout() {
        open();
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.waits().click(By.cssSelector("[data-test='item-4-title-link']"));
        inventoryPage.waits().urlContains("inventory-item.html");
        productDetailAssertions.verifyProductDetailPageLoaded();

        productDetailPage.addItemToCart();
        inventoryPage.waits().text(inventoryPage.getCartBadge(), "1");

        productDetailPage.clickBackToProducts();
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.waits().click(By.cssSelector("[data-test='item-0-title-link']"));
        productDetailAssertions.verifyProductDetailPageLoaded();
        productDetailPage.addItemToCart();

        inventoryPage.waits().click(By.cssSelector("[data-test='shopping-cart-link']"));
        cartAssertions.verifyCartItemCount(2);

        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("David", "Miller", "33333");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        checkoutAssertions.verifyCheckoutCompletePageURL();

        attachScreenshot("product-detail-purchase");
    }

    @Test
    @DisplayName("should maintain cart persistence across navigation")
    void cartPersistenceAcrossNavigation() {
        open();
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.waits().click(By.cssSelector("[data-test='item-2-title-link']"));
        inventoryPage.waits().text(inventoryPage.getCartBadge(), "2");

        productDetailPage.addItemToCart();
        inventoryPage.waits().text(inventoryPage.getCartBadge(), "3");

        productDetailPage.clickBackToProducts();
        inventoryAssertions.verifyInventoryLandingDetails();
        inventoryPage.waits().text(inventoryPage.getCartBadge(), "3");

        inventoryPage.clickCartLink();
        cartAssertions.verifyCartItemCount(3);

        cartPage.clickContinueShopping();
        inventoryAssertions.verifyInventoryLandingDetails();
        inventoryPage.waits().text(inventoryPage.getCartBadge(), "3");

        inventoryPage.removeItemFromCart("sauce-labs-backpack");
        inventoryPage.waits().text(inventoryPage.getCartBadge(), "2");

        inventoryPage.clickCartLink();
        cartAssertions.verifyCartItemCount(2);

        attachScreenshot("cart-persistence");
    }

    @Test
    @DisplayName("should handle reset app state during shopping journey")
    @DisabledIfSystemProperty(named = "browser", matches = "(?i)safari",
            disabledReason = "Safari under WebDriver: saucedemo's own script never saves the cart to localStorage, so the badge stays at 1 (clicks do register; the same steps show 3 items in normal Safari). Known browser difference, see TODO.")
    void resetAppStateDuringShopping() {
        open();
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryPage.addItemToCart("sauce-labs-onesie");
        inventoryAssertions.verifyCartBadgeCount("3");

        hamburgerMenuPage.openMenu();
        hamburgerMenuPage.clickResetAppStateLink();
        hamburgerMenuPage.closeMenu();

        inventoryPage.waits().absent(inventoryPage.getCartBadge());

        attachScreenshot("reset-app-state");
    }

    @Test
    @DisplayName("should complete checkout with problem_user")
    void problemUserCheckout() {
        open();
        loginPage.login(Users.PROBLEM, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.clickCartLink();
        cartAssertions.verifyCartItemCount(2);

        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("Problem", "User", "99999");
        checkoutPage.clickContinue();

        // problem_user can't complete the form: it stays on step one with an error.
        checkoutPage.waits().urlContains("checkout-step-one.html");
        checkoutPage.waits().visible(checkoutPage.getErrorMessage());

        attachScreenshot("problem-user-checkout");
    }

    @Test
    @DisplayName("should complete checkout with performance_glitch_user")
    void performanceGlitchUserCheckout() {
        open();
        loginPage.login(Users.PERFORMANCE_GLITCH, Users.PASSWORD);
        inventoryPage.waits().urlContains("inventory.html");
        inventoryAssertions.verifyInventoryLandingDetails();

        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-fleece-jacket");
        inventoryAssertions.verifyCartBadgeCount("2");

        inventoryPage.clickCartLink();
        inventoryPage.waits().urlContains("cart.html");
        cartAssertions.verifyCartItemCount(2);

        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("Performance", "User", "88888");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutPage.clickFinish();
        checkoutAssertions.verifyCheckoutCompletePageURL();
        checkoutAssertions.verifyOrderConfirmationMessage();

        attachScreenshot("performance-glitch-user-checkout");
    }

    /** Playwright's locator.fill(): clear, then type. */
    private void fill(By locator, String value) {
        WebElement field = checkoutPage.waits().visible(locator);
        field.clear();
        field.sendKeys(value);
    }
}
