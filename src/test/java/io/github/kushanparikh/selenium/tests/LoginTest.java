package io.github.kushanparikh.selenium.tests;

import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Port of tests/login.spec.ts. */
@Tag("functional")
class LoginTest extends BaseTest {

    @BeforeEach
    void openLoginPage() {
        open();
        loginAssertions.verifyLoginLandingDetails();
    }

    @Test
    @DisplayName("should login successfully with valid credentials")
    void loginWithValidCredentials() {
        loginPage.login(Users.STANDARD, Users.PASSWORD);

        inventoryAssertions.verifyInventoryLandingDetails();

        attachScreenshot("inventory-page-after-login");
    }

    @Nested
    @DisplayName("Login with invalid credentials")
    class LoginWithInvalidCredentials {

        @Test
        @DisplayName("should show error for locked out user")
        void lockedOutUser() {
            loginPage.login(Users.LOCKED_OUT, Users.PASSWORD);

            loginAssertions.verifyDisplayedErrorMessage("Epic sadface: Sorry, this user has been locked out.");

            attachScreenshot("error-locked-out-user");
        }

        @Test
        @DisplayName("should show error with non-existent username")
        void nonExistentUsername() {
            loginPage.login("invalid_user", Users.PASSWORD);

            loginAssertions.verifyDisplayedErrorMessage(
                    "Epic sadface: Username and password do not match any user in this service");

            attachScreenshot("error-non-existent-username");
        }

        @Test
        @DisplayName("should show error with empty password")
        void emptyPassword() {
            loginPage.login(Users.STANDARD, "");

            loginAssertions.verifyDisplayedErrorMessage("Epic sadface: Password is required");

            attachScreenshot("error-empty-password");
        }

        @Test
        @DisplayName("should show error with empty username")
        void emptyUsername() {
            loginPage.login("", Users.PASSWORD);

            loginAssertions.verifyDisplayedErrorMessage("Epic sadface: Username is required");

            attachScreenshot("error-empty-username");
        }
    }

    /** The TS describe repeats the outer beforeEach; the outer @BeforeEach already runs for nested tests. */
    @Nested
    @DisplayName("Special user types")
    class SpecialUserTypes {

        @Test
        @DisplayName("should login with problem_user and verify broken product images")
        void problemUserBrokenImages() {
            loginPage.login(Users.PROBLEM, Users.PASSWORD);

            inventoryAssertions.verifyInventoryLandingDetails();

            List<WebElement> images = inventoryPage.waits().all(inventoryPage.getProductImages());
            for (WebElement image : images) {
                assertThat(image.getAttribute("src")).contains("sl-404");
            }

            attachScreenshot("problem-user-broken-images");
        }

        @Test
        @DisplayName("should login with performance_glitch_user and handle delayed response")
        void performanceGlitchUser() {
            long start = System.currentTimeMillis();

            loginPage.login(Users.PERFORMANCE_GLITCH, Users.PASSWORD);

            inventoryAssertions.verifyInventoryLandingDetails();

            long loadTime = System.currentTimeMillis() - start;
            assertThat(loadTime).isGreaterThan(1000);

            attachScreenshot("performance-glitch-user-login");
        }

        @Test
        @DisplayName("should login with error_user")
        void errorUser() {
            loginPage.login(Users.ERROR, Users.PASSWORD);

            inventoryAssertions.verifyInventoryLandingDetails();

            attachScreenshot("error-user-login");
        }

        @Test
        @DisplayName("should login with visual_user")
        void visualUser() {
            loginPage.login(Users.VISUAL, Users.PASSWORD);

            inventoryAssertions.verifyInventoryLandingDetails();

            attachScreenshot("visual-user-login");
        }
    }

    @Nested
    @DisplayName("UI element verification")
    class UiElementVerification {

        @Test
        @DisplayName("should verify logo is visible")
        void logoIsVisible() {
            loginAssertions.verifyLogoIsVisible();
        }

        @Test
        @DisplayName("should verify password field is masked")
        void passwordIsMasked() {
            loginAssertions.verifyPasswordFieldIsMasked();
        }

        @Test
        @DisplayName("should verify login logo text content")
        void logoTextContent() {
            loginPage.waits().containsText(By.cssSelector(".login_logo"), "Swag Labs");
        }
    }

    @Nested
    @DisplayName("Keyboard navigation")
    class KeyboardNavigation {

        @Test
        @DisplayName("should support tab order navigation")
        void tabOrderNavigation() {
            Actions keyboard = new Actions(driver);

            keyboard.sendKeys(Keys.TAB).perform();
            assertFocused(loginPage.getUsernameField());

            keyboard.sendKeys(Keys.TAB).perform();
            assertFocused(loginPage.getPasswordField());

            keyboard.sendKeys(Keys.TAB).perform();
            assertFocused(loginPage.getLoginButton());

            driver.findElement(loginPage.getUsernameField()).sendKeys(Users.STANDARD);
            driver.findElement(loginPage.getPasswordField()).sendKeys(Users.PASSWORD);

            keyboard.sendKeys(Keys.ENTER).perform();

            loginPage.waits().urlContains("inventory.html");
        }

        /** Playwright's toBeFocused(): the element is the document's active element (retried). */
        private void assertFocused(By locator) {
            loginPage.waits().until("focus on " + locator,
                    d -> d.switchTo().activeElement().equals(d.findElement(locator)));
        }
    }
}
