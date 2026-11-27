package io.github.kushanparikh.selenium.tests.accessibility;

import io.github.kushanparikh.selenium.support.AccessibilityChecker;
import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.DriverExtension;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Port of tests/accessibility/accessibility.spec.ts. */
@Tag("a11y")
@DisplayName("Accessibility Tests - WCAG 2.1 Level AA standards")
class AccessibilityTest extends BaseTest {

    @BeforeEach
    void openSite() {
        open();
    }

    @Test
    @DisplayName("Login Page accessibility validation")
    void loginPage() {
        AccessibilityChecker.Report report = check("Login Page");
        assertThat(report.newViolations()).as("New accessibility violations detected").isEmpty();
    }

    @Test
    @DisplayName("Inventory Page accessibility validation")
    void inventoryPage() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();

        AccessibilityChecker.Report report = check("Inventory Page");
        assertThat(report.newViolations()).as("New accessibility violations detected").isEmpty();
    }

    @Test
    @DisplayName("Cart Page accessibility validation")
    void cartPage() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();
        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.clickCartLink();
        // Playwright's click waits for the navigation it starts; Selenium's doesn't, so wait before scanning.
        inventoryPage.waits().urlContains("cart.html");

        AccessibilityChecker.Report report = check("Cart Page");
        assertThat(report.newViolations()).as("New accessibility violations detected").isEmpty();
    }

    private AccessibilityChecker.Report check(String pageName) {
        return AccessibilityChecker.check(driver, DriverExtension.currentContext(), pageName);
    }
}
