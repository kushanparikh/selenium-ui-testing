package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.CheckoutPage;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Port of assertions/checkoutAssertions.ts. Each check waits, like Playwright's retrying expect(). The TS URL
 * checks are a one-shot expect(page.url()).toContain(); here they wait, because Selenium's click does not wait
 * for the navigation it starts. Amounts are BigDecimal and compared with compareTo: the page shows two decimals,
 * so exact comparison is what the TS toBeCloseTo(x, 2) means.
 */
public class CheckoutAssertions {

    private final CheckoutPage checkoutPage;

    public CheckoutAssertions(CheckoutPage checkoutPage) {
        this.checkoutPage = checkoutPage;
    }

    public void verifyCheckoutInformationPageURL() {
        checkoutPage.waits().urlContains("checkout-step-one.html");
    }

    public void verifyCheckoutOverviewPageURL() {
        checkoutPage.waits().urlContains("checkout-step-two.html");
    }

    public void verifyCartPageURL() {
        checkoutPage.waits().urlContains("cart.html");
    }

    public void verifyErrorMessage(String message) {
        checkoutPage.waits().visible(checkoutPage.getErrorMessage());
        checkoutPage.waits().containsText(checkoutPage.getErrorMessage(), message);
    }

    public void verifyFirstNameFieldIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getFirstNameField());
    }

    public void verifyLastNameFieldIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getLastNameField());
    }

    public void verifyPostalCodeFieldIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getPostalCodeField());
    }

    public void verifyContinueButtonIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getContinueButton());
    }

    public void verifyCancelButtonIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getCancelButton());
    }

    public void verifyCheckoutCompletePageURL() {
        checkoutPage.waits().urlContains("checkout-complete.html");
    }

    public void verifyInventoryPageURL() {
        checkoutPage.waits().urlContains("inventory.html");
    }

    public void verifyCompleteHeader(String expectedText) {
        checkoutPage.waits().visible(checkoutPage.getCompleteHeader());
        checkoutPage.waits().containsText(checkoutPage.getCompleteHeader(), expectedText);
    }

    public void verifyCompleteText(String expectedText) {
        checkoutPage.waits().visible(checkoutPage.getCompleteText());
        checkoutPage.waits().containsText(checkoutPage.getCompleteText(), expectedText);
    }

    public void verifyBackHomeButtonIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getBackHomeButton());
    }

    public void verifyFinishButtonIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getFinishButton());
    }

    public void verifyCancelButtonOnOverviewIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getCancelButtonOnOverview());
    }

    /** The TS reads count() once; here it waits for the count, so a slow overview render is not a false fail. */
    public void verifyCartItemsCount(int expectedCount) {
        checkoutPage.waits().until("exactly " + expectedCount + " cart items",
                d -> checkoutPage.getCartItemsCount() == expectedCount);
    }

    public void verifyCartItemName(String expectedName, int index) {
        checkoutPage.waits().text(checkoutPage.getCartItemName(index), expectedName);
    }

    public void verifyCartItemName(String expectedName) {
        verifyCartItemName(expectedName, 0);
    }

    public void verifySubtotalIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getSubtotalLabel());
    }

    public void verifySubtotalAmount(BigDecimal expectedAmount) {
        assertThat(checkoutPage.getSubtotalAmount()).isEqualByComparingTo(expectedAmount);
    }

    public void verifyTaxIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getTaxLabel());
    }

    public void verifyTaxAmount(BigDecimal expectedAmount) {
        assertThat(checkoutPage.getTaxAmount()).isEqualByComparingTo(expectedAmount);
    }

    public void verifyTotalIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getTotalLabel());
    }

    public void verifyTotalAmount(BigDecimal expectedAmount) {
        assertThat(checkoutPage.getTotalAmount()).isEqualByComparingTo(expectedAmount);
    }

    public void verifyTotalCalculation() {
        BigDecimal subtotal = checkoutPage.getSubtotalAmount();
        BigDecimal tax = checkoutPage.getTaxAmount();
        BigDecimal total = checkoutPage.getTotalAmount();
        BigDecimal expectedTotal = subtotal.add(tax);
        assertThat(total.compareTo(expectedTotal))
                .as("total %s == subtotal %s + tax %s", total, subtotal, tax)
                .isZero();
    }

    public void verifyPaymentInfoIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getPaymentInfo());
    }

    public void verifyShippingInfoIsVisible() {
        checkoutPage.waits().visible(checkoutPage.getShippingInfo());
    }

    public void verifyItemTotalIsDisplayed() {
        checkoutPage.waits().visible(checkoutPage.getSubtotalLabel());
    }

    public void verifyTaxIsDisplayed() {
        checkoutPage.waits().visible(checkoutPage.getTaxLabel());
    }

    public void verifyTotalPriceIsDisplayed() {
        checkoutPage.waits().visible(checkoutPage.getTotalLabel());
    }

    public void verifyOrderConfirmationMessage() {
        checkoutPage.waits().visible(checkoutPage.getCompleteHeader());
        checkoutPage.waits().containsText(checkoutPage.getCompleteHeader(), "Thank you for your order");
    }

    public void verifyThankYouMessage() {
        checkoutPage.waits().visible(checkoutPage.getCompleteText());
        checkoutPage.waits().containsText(checkoutPage.getCompleteText(), "Your order has been dispatched");
    }
}
