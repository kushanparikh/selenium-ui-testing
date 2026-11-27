package io.github.kushanparikh.selenium.support;

import io.github.kushanparikh.selenium.assertions.InventoryAssertions;
import io.github.kushanparikh.selenium.assertions.LoginAssertions;
import io.github.kushanparikh.selenium.config.Config;
import io.github.kushanparikh.selenium.pages.InventoryPage;
import io.github.kushanparikh.selenium.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * The Java counterpart of tests/fixtures/baseTest.ts: every test class extends this and gets a fresh driver
 * plus ready-made page objects and assertion classes (added as the pages are ported).
 */
@ExtendWith(DriverExtension.class)
public abstract class BaseTest {

    protected WebDriver driver;

    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;

    protected LoginAssertions loginAssertions;
    protected InventoryAssertions inventoryAssertions;

    @BeforeEach
    void setUpBase(WebDriver driver) {
        this.driver = driver;

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);

        loginAssertions = new LoginAssertions(loginPage);
        inventoryAssertions = new InventoryAssertions(inventoryPage);
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
