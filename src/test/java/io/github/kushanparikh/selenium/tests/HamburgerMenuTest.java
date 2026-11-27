package io.github.kushanparikh.selenium.tests;

import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Port of tests/hamburgerMenu.spec.ts. */
@DisplayName("Hamburger Menu Tests")
class HamburgerMenuTest extends BaseTest {

    @BeforeEach
    void logIn() {
        open();
        // Login first as hamburger menu is only available after login
        loginPage.login(Users.STANDARD, Users.PASSWORD);
        inventoryAssertions.verifyInventoryLandingDetails();
    }

    @Test
    @DisplayName("should open hamburger menu")
    void openMenu() {
        hamburgerMenuPage.openMenu();

        hamburgerMenuAssertions.verifyMenuIsOpen();

        hamburgerMenuAssertions.verifyAllItemsLinkIsVisible();
        hamburgerMenuAssertions.verifyAboutLinkIsVisible();
        hamburgerMenuAssertions.verifyLogoutLinkIsVisible();
        hamburgerMenuAssertions.verifyResetAppStateLinkIsVisible();
    }

    @Test
    @DisplayName("should close hamburger menu when close button is clicked")
    void closeMenu() {
        hamburgerMenuPage.openMenu();
        hamburgerMenuAssertions.verifyMenuIsOpen();

        hamburgerMenuPage.closeMenu();
        hamburgerMenuAssertions.verifyMenuIsClosed();
    }

    @Test
    @DisplayName("should navigate to All Items when clicking All Items link")
    void allItemsLinkNavigatesToInventory() {
        inventoryPage.clickCartLink();

        // The TS reads page.url() once, right after the click; waiting here removes that race.
        inventoryPage.waits().urlContains("/cart.html");

        hamburgerMenuPage.openMenu();
        hamburgerMenuAssertions.verifyMenuIsOpen();

        hamburgerMenuPage.clickAllItemsLink();

        // toHaveURL(/.*inventory.html/)
        inventoryPage.waits().urlContains("inventory.html");
    }

    @Test
    @DisplayName("should navigate to About page when About link is clicked")
    void aboutLinkNavigatesToSauceLabs() {
        hamburgerMenuPage.openMenu();
        hamburgerMenuAssertions.verifyMenuIsOpen();

        hamburgerMenuPage.clickAboutLink();

        // External site: only the URL is checked, as in the TS.
        hamburgerMenuPage.waits().urlContains("saucelabs.com");
    }

    @Test
    @DisplayName("should logout successfully when clicking logout link")
    void logoutLinkLogsOut() {
        hamburgerMenuPage.openMenu();

        hamburgerMenuPage.clickLogoutLink();

        loginAssertions.verifyLoginLandingDetails();
    }

    @Test
    @DisplayName("should reset app state and clear cart")
    void resetAppStateClearsCart() {
        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-bike-light");

        // expect(cartBadge).toHaveText('2')
        inventoryAssertions.verifyCartBadgeCount("2");

        hamburgerMenuPage.openMenu();
        hamburgerMenuAssertions.verifyMenuIsOpen();

        hamburgerMenuPage.clickResetAppStateLink();

        // The TS waitForTimeout(500) is dropped: verifyCartBadgeNotVisible() below waits for the reset instead.

        hamburgerMenuPage.closeMenu();
        hamburgerMenuAssertions.verifyMenuIsClosed();

        inventoryAssertions.verifyCartBadgeNotVisible();
    }
}
