package io.github.kushanparikh.selenium.support;

import io.github.kushanparikh.selenium.assertions.CartAssertions;
import io.github.kushanparikh.selenium.assertions.CheckoutAssertions;
import io.github.kushanparikh.selenium.assertions.FooterAssertions;
import io.github.kushanparikh.selenium.assertions.HamburgerMenuAssertions;
import io.github.kushanparikh.selenium.assertions.InventoryAssertions;
import io.github.kushanparikh.selenium.assertions.LoginAssertions;
import io.github.kushanparikh.selenium.assertions.ProductDetailAssertions;
import io.github.kushanparikh.selenium.config.Config;
import io.github.kushanparikh.selenium.pages.CartPage;
import io.github.kushanparikh.selenium.pages.CheckoutPage;
import io.github.kushanparikh.selenium.pages.FooterPage;
import io.github.kushanparikh.selenium.pages.HamburgerMenuPage;
import io.github.kushanparikh.selenium.pages.InventoryPage;
import io.github.kushanparikh.selenium.pages.LoginPage;
import io.github.kushanparikh.selenium.pages.ProductDetailPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * The Java counterpart of tests/fixtures/baseTest.ts: every test class extends this and gets a fresh driver
 * plus ready-made page objects and assertion classes, the way Playwright fixtures inject them.
 */
@ExtendWith(DriverExtension.class)
public abstract class BaseTest {

    protected WebDriver driver;

    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected CartPage cartPage;
    protected ProductDetailPage productDetailPage;
    protected CheckoutPage checkoutPage;
    protected FooterPage footerPage;
    protected HamburgerMenuPage hamburgerMenuPage;

    protected LoginAssertions loginAssertions;
    protected InventoryAssertions inventoryAssertions;
    protected CartAssertions cartAssertions;
    protected ProductDetailAssertions productDetailAssertions;
    protected CheckoutAssertions checkoutAssertions;
    protected FooterAssertions footerAssertions;
    protected HamburgerMenuAssertions hamburgerMenuAssertions;

    @BeforeEach
    void setUpBase(WebDriver driver) {
        this.driver = driver;

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        checkoutPage = new CheckoutPage(driver);
        footerPage = new FooterPage(driver);
        hamburgerMenuPage = new HamburgerMenuPage(driver);

        loginAssertions = new LoginAssertions(loginPage);
        inventoryAssertions = new InventoryAssertions(inventoryPage);
        cartAssertions = new CartAssertions(cartPage);
        productDetailAssertions = new ProductDetailAssertions(productDetailPage);
        checkoutAssertions = new CheckoutAssertions(checkoutPage);
        footerAssertions = new FooterAssertions(footerPage);
        hamburgerMenuAssertions = new HamburgerMenuAssertions(hamburgerMenuPage);
    }

    /** Playwright's page.goto('/') with baseURL. */
    protected void open() {
        driver.get(Config.baseUrl());
    }

    /** Playwright's testInfo.attach(name, { body: await page.screenshot() }). */
    protected void attachScreenshot(String name) {
        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Attachments.save(DriverExtension.currentContext(), name, "png", png);
    }
}
