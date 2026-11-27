package io.github.kushanparikh.selenium.tests;

import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.math.BigDecimal;

/** Port of tests/checkout.spec.ts. */
class CheckoutTest extends BaseTest {

    @BeforeEach
    void openCheckoutInformationPage() {
        open();
        loginAssertions.verifyLoginLandingDetails();

        // Login with standard user
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        // Add an item to cart and navigate to cart
        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryAssertions.verifyCartBadgeCount("1");

        // Navigate to cart (TS: expect(page).toHaveURL(/.*cart.html/))
        inventoryPage.clickCartLink();
        checkoutPage.waits().urlContains("cart.html");

        // The TS clicks the cart page's checkout button with a raw page.locator, not a page object; kept inline.
        checkoutPage.waits().click(By.cssSelector("[data-test='checkout']"));
        checkoutPage.waits().urlContains("checkout-step-one.html");
    }

    @Test
    @DisplayName("should enter valid information and continue to checkout overview")
    void validInformationContinuesToOverview() {
        checkoutAssertions.verifyCheckoutInformationPageURL();

        checkoutPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();

        checkoutAssertions.verifyCheckoutOverviewPageURL();

        attachScreenshot("checkout-overview-page");
    }

    @Test
    @DisplayName("should show error when first name is empty")
    void errorWhenFirstNameEmpty() {
        checkoutPage.fillLastName("Doe");
        checkoutPage.fillPostalCode("12345");
        checkoutPage.clickContinue();

        checkoutAssertions.verifyErrorMessage("Error: First Name is required");
        checkoutAssertions.verifyCheckoutInformationPageURL();

        attachScreenshot("empty-first-name-error");
    }

    @Test
    @DisplayName("should show error when last name is empty")
    void errorWhenLastNameEmpty() {
        checkoutPage.fillFirstName("John");
        checkoutPage.fillPostalCode("12345");
        checkoutPage.clickContinue();

        checkoutAssertions.verifyErrorMessage("Error: Last Name is required");
        checkoutAssertions.verifyCheckoutInformationPageURL();

        attachScreenshot("empty-last-name-error");
    }

    @Test
    @DisplayName("should show error when postal code is empty")
    void errorWhenPostalCodeEmpty() {
        checkoutPage.fillFirstName("John");
        checkoutPage.fillLastName("Doe");
        checkoutPage.clickContinue();

        checkoutAssertions.verifyErrorMessage("Error: Postal Code is required");
        checkoutAssertions.verifyCheckoutInformationPageURL();

        attachScreenshot("empty-postal-code-error");
    }

    @Test
    @DisplayName("should return to cart page when cancel button is clicked")
    void cancelReturnsToCart() {
        checkoutPage.fillFirstName("John");
        checkoutPage.fillLastName("Doe");
        checkoutPage.clickCancel();

        checkoutAssertions.verifyCartPageURL();

        attachScreenshot("returned-to-cart-page");
    }

    @Test
    @DisplayName("should display appropriate validation messages for all empty fields")
    void validationForAllEmptyFields() {
        checkoutPage.clickContinue();

        checkoutAssertions.verifyErrorMessage("Error: First Name is required");
        checkoutAssertions.verifyCheckoutInformationPageURL();

        attachScreenshot("all-fields-empty-error");
    }

    @Test
    @DisplayName("should accept special characters in name fields")
    void specialCharactersInNames() {
        checkoutPage.fillCheckoutInformation("John-Paul O'Brien", "José García-Márquez", "12345");
        checkoutPage.clickContinue();

        checkoutAssertions.verifyCheckoutOverviewPageURL();

        attachScreenshot("special-characters-accepted");
    }

    @Test
    @DisplayName("should display order confirmation message on checkout complete page")
    void orderConfirmationMessage() {
        checkoutPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutPage.clickFinish();
        checkoutAssertions.verifyCheckoutCompletePageURL();

        checkoutAssertions.verifyCompleteHeader("Thank you for your order!");

        attachScreenshot("order-confirmation-message");
    }

    @Test
    @DisplayName("should display thank you message on checkout complete page")
    void thankYouMessage() {
        checkoutPage.fillCheckoutInformation("Jane", "Smith", "54321");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutPage.clickFinish();
        checkoutAssertions.verifyCheckoutCompletePageURL();

        checkoutAssertions.verifyCompleteText("Your order has been dispatched");

        attachScreenshot("thank-you-message");
    }

    @Test
    @DisplayName("should return to inventory page when back home button is clicked")
    void backHomeReturnsToInventory() {
        checkoutPage.fillCheckoutInformation("Bob", "Johnson", "98765");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutPage.clickFinish();
        checkoutAssertions.verifyCheckoutCompletePageURL();

        checkoutAssertions.verifyBackHomeButtonIsVisible();
        checkoutPage.clickBackHome();

        checkoutAssertions.verifyInventoryPageURL();

        attachScreenshot("back-home-navigation");
    }

    @Test
    @DisplayName("should reset cart badge to empty after completing order")
    void cartBadgeResetAfterOrder() {
        inventoryAssertions.verifyCartBadgeCount("1");

        checkoutPage.fillCheckoutInformation("Alice", "Williams", "11111");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutPage.clickFinish();
        checkoutAssertions.verifyCheckoutCompletePageURL();

        checkoutPage.clickBackHome();
        checkoutAssertions.verifyInventoryPageURL();

        inventoryAssertions.verifyCartBadgeNotVisible();

        attachScreenshot("cart-badge-reset");
    }

    // Checkout Overview Page Tests

    @Test
    @DisplayName("should review items in checkout overview")
    void reviewItemsInOverview() {
        checkoutPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutAssertions.verifyCartItemsCount(1);
        checkoutAssertions.verifyCartItemName("Sauce Labs Backpack", 0);

        checkoutAssertions.verifyPaymentInfoIsVisible();
        checkoutAssertions.verifyShippingInfoIsVisible();

        attachScreenshot("checkout-overview-items");
    }

    @Test
    @DisplayName("should verify item total calculation in checkout overview")
    void itemTotalCalculation() {
        checkoutPage.fillCheckoutInformation("Jane", "Smith", "54321");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        // Sauce Labs Backpack = $29.99
        checkoutAssertions.verifySubtotalIsVisible();
        checkoutAssertions.verifySubtotalAmount(new BigDecimal("29.99"));

        attachScreenshot("item-total-calculation");
    }

    @Test
    @DisplayName("should verify tax calculation in checkout overview")
    void taxCalculation() {
        checkoutPage.fillCheckoutInformation("Bob", "Johnson", "98765");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        // 8% of $29.99 = $2.40
        checkoutAssertions.verifyTaxIsVisible();
        checkoutAssertions.verifyTaxAmount(new BigDecimal("2.40"));

        attachScreenshot("tax-calculation");
    }

    @Test
    @DisplayName("should verify total price calculation in checkout overview")
    void totalPriceCalculation() {
        checkoutPage.fillCheckoutInformation("Alice", "Williams", "11111");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutAssertions.verifyTotalIsVisible();

        // subtotal + tax = $29.99 + $2.40 = $32.39
        checkoutAssertions.verifyTotalCalculation();
        checkoutAssertions.verifyTotalAmount(new BigDecimal("32.39"));

        attachScreenshot("total-price-calculation");
    }

    @Test
    @DisplayName("should return to inventory when cancel button is clicked on overview")
    void cancelOnOverviewReturnsToInventory() {
        checkoutPage.fillCheckoutInformation("Charlie", "Brown", "55555");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutAssertions.verifyCancelButtonOnOverviewIsVisible();
        checkoutPage.clickCancelOnOverview();

        checkoutAssertions.verifyInventoryPageURL();

        attachScreenshot("cancel-returns-to-inventory");
    }

    @Test
    @DisplayName("should complete order when finish button is clicked")
    void finishCompletesOrder() {
        checkoutPage.fillCheckoutInformation("David", "Miller", "77777");
        checkoutPage.clickContinue();
        checkoutAssertions.verifyCheckoutOverviewPageURL();

        checkoutAssertions.verifyFinishButtonIsVisible();
        checkoutPage.clickFinish();

        checkoutAssertions.verifyCheckoutCompletePageURL();
        checkoutAssertions.verifyCompleteHeader("Thank you for your order!");

        attachScreenshot("finish-completes-order");
    }
}
