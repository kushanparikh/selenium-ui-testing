package io.github.kushanparikh.selenium.tests;

import io.github.kushanparikh.selenium.pages.SortOption;
import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Port of tests/filter.spec.ts. */
@Tag("functional")
class FilterTest extends BaseTest {

    @BeforeEach
    void openLoginPage() {
        open();
        loginAssertions.verifyLoginLandingDetails();
    }

    @Nested
    @DisplayName("Filter products")
    class FilterProducts {

        @Test
        @DisplayName("filter products by item name by reverse order")
        void nameReverseOrder() {
            loginPage.login(Users.STANDARD, Users.PASSWORD);

            inventoryAssertions.verifyInventoryLandingDetails();

            attachScreenshot("inventory-page-after-login");

            inventoryPage.applyFilter(SortOption.ZA);

            attachScreenshot("inventory-page-after-applying-filter");

            inventoryAssertions.verifyProductsSorted(SortOption.ZA);
        }

        @Test
        @DisplayName("filter products by price - low to high")
        void priceLowToHigh() {
            loginPage.login(Users.STANDARD, Users.PASSWORD);

            inventoryAssertions.verifyInventoryLandingDetails();

            attachScreenshot("inventory-page-after-login");

            inventoryPage.applyFilter(SortOption.LOHI);

            attachScreenshot("inventory-page-after-applying-filter");

            inventoryAssertions.verifyProductsSorted(SortOption.LOHI);
        }

        @Test
        @DisplayName("filter products by price - high to low")
        void priceHighToLow() {
            loginPage.login(Users.STANDARD, Users.PASSWORD);

            inventoryAssertions.verifyInventoryLandingDetails();

            attachScreenshot("inventory-page-after-login");

            inventoryPage.applyFilter(SortOption.HILO);

            attachScreenshot("inventory-page-after-applying-filter");

            inventoryAssertions.verifyProductsSorted(SortOption.HILO);
        }
    }
}
