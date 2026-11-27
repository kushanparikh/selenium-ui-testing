package io.github.kushanparikh.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Port of pages/checkoutPage.ts. */
public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE = By.cssSelector("[data-test='continue']");
    private static final By CANCEL = By.cssSelector("[data-test='cancel']");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private static final By FINISH = By.cssSelector("[data-test='finish']");
    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By SUBTOTAL_LABEL = By.cssSelector("[data-test='subtotal-label']");
    private static final By TAX_LABEL = By.cssSelector("[data-test='tax-label']");
    private static final By TOTAL_LABEL = By.cssSelector("[data-test='total-label']");
    private static final By PAYMENT_INFO = By.cssSelector("[data-test='payment-info-value']");
    private static final By SHIPPING_INFO = By.cssSelector("[data-test='shipping-info-value']");
    private static final By BACK_HOME = By.cssSelector("[data-test='back-to-products']");
    private static final By COMPLETE_HEADER = By.cssSelector("[data-test='complete-header']");
    private static final By COMPLETE_TEXT = By.cssSelector("[data-test='complete-text']");

    /** The TS regex /\$(\d+\.\d+)/. */
    private static final Pattern AMOUNT = Pattern.compile("\\$(\\d+\\.\\d+)");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Actions

    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        fill(FIRST_NAME, firstName);
        fill(LAST_NAME, lastName);
        fill(POSTAL_CODE, postalCode);
    }

    public void clickContinue() {
        waits().click(CONTINUE);
    }

    public void clickCancel() {
        waits().click(CANCEL);
    }

    public void fillFirstName(String firstName) {
        fill(FIRST_NAME, firstName);
    }

    public void fillLastName(String lastName) {
        fill(LAST_NAME, lastName);
    }

    public void fillPostalCode(String postalCode) {
        fill(POSTAL_CODE, postalCode);
    }

    // Getters

    public By getFirstNameField() {
        return FIRST_NAME;
    }

    public By getLastNameField() {
        return LAST_NAME;
    }

    public By getPostalCodeField() {
        return POSTAL_CODE;
    }

    public By getContinueButton() {
        return CONTINUE;
    }

    public By getCancelButton() {
        return CANCEL;
    }

    public By getErrorMessage() {
        return ERROR;
    }

    public String getErrorMessageText() {
        return textOf(ERROR);
    }

    // Checkout Overview Page Actions

    public void clickFinish() {
        waits().click(FINISH);
    }

    public void clickCancelOnOverview() {
        waits().click(CANCEL);
    }

    // Checkout Overview Page Getters

    public By getFinishButton() {
        return FINISH;
    }

    public By getCancelButtonOnOverview() {
        return CANCEL;
    }

    public By getCartItems() {
        return CART_ITEMS;
    }

    /** Like the TS count(): a one-shot read, no wait. */
    public int getCartItemsCount() {
        return driver.findElements(CART_ITEMS).size();
    }

    /** Playwright's locator('.inventory_item_name').nth(index), as an XPath (CSS has no "nth match"). */
    public By getCartItemName(int index) {
        return By.xpath("(//*[contains(concat(' ', normalize-space(@class), ' '), ' inventory_item_name ')])["
                + (index + 1) + "]");
    }

    public By getCartItemName() {
        return getCartItemName(0);
    }

    public String getCartItemNameText(int index) {
        return textOf(getCartItemName(index));
    }

    public String getCartItemNameText() {
        return getCartItemNameText(0);
    }

    public By getSubtotalLabel() {
        return SUBTOTAL_LABEL;
    }

    public String getSubtotalText() {
        return textOf(SUBTOTAL_LABEL);
    }

    public BigDecimal getSubtotalAmount() {
        return amountIn(getSubtotalText());
    }

    public By getTaxLabel() {
        return TAX_LABEL;
    }

    public String getTaxText() {
        return textOf(TAX_LABEL);
    }

    public BigDecimal getTaxAmount() {
        return amountIn(getTaxText());
    }

    public By getTotalLabel() {
        return TOTAL_LABEL;
    }

    public String getTotalText() {
        return textOf(TOTAL_LABEL);
    }

    public BigDecimal getTotalAmount() {
        return amountIn(getTotalText());
    }

    public By getPaymentInfo() {
        return PAYMENT_INFO;
    }

    public String getPaymentInfoText() {
        return textOf(PAYMENT_INFO);
    }

    public By getShippingInfo() {
        return SHIPPING_INFO;
    }

    public String getShippingInfoText() {
        return textOf(SHIPPING_INFO);
    }

    // Checkout Complete Page Actions

    public void clickBackHome() {
        waits().click(BACK_HOME);
    }

    // Checkout Complete Page Getters

    public By getCompleteHeader() {
        return COMPLETE_HEADER;
    }

    public String getCompleteHeaderText() {
        return textOf(COMPLETE_HEADER);
    }

    public By getCompleteText() {
        return COMPLETE_TEXT;
    }

    public String getCompleteTextContent() {
        return textOf(COMPLETE_TEXT);
    }

    public By getBackHomeButton() {
        return BACK_HOME;
    }

    /** Playwright's fill() clears the field first; Selenium's sendKeys() appends, so clear explicitly. */
    private void fill(By locator, String value) {
        WebElement field = waits().visible(locator);
        field.clear();
        field.sendKeys(value);
    }

    private String textOf(By locator) {
        return waits().visible(locator).getText();
    }

    /** Like the TS: the number after the first "$", or 0 when there is none. Exact decimal, no float drift. */
    private static BigDecimal amountIn(String text) {
        Matcher match = AMOUNT.matcher(text);
        return match.find() ? new BigDecimal(match.group(1)) : BigDecimal.ZERO;
    }
}
