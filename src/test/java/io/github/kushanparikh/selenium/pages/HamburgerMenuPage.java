package io.github.kushanparikh.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Port of pages/hamburgerMenuPage.ts. The menu slides in with a CSS transition, so every click waits for
 * clickability first (Playwright's click() auto-waits for actionability).
 */
public class HamburgerMenuPage extends BasePage {

    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By CLOSE_BUTTON = By.id("react-burger-cross-btn");
    private static final By MENU_CONTAINER = By.cssSelector(".bm-menu-wrap");
    private static final By ALL_ITEMS_LINK = By.id("inventory_sidebar_link");
    private static final By ABOUT_LINK = By.id("about_sidebar_link");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    private static final By RESET_APP_STATE_LINK = By.id("reset_sidebar_link");

    public HamburgerMenuPage(WebDriver driver) {
        super(driver);
    }

    // Actions

    public void openMenu() {
        waits().click(MENU_BUTTON);
    }

    public void closeMenu() {
        waits().click(CLOSE_BUTTON);
    }

    public void clickAllItemsLink() {
        waits().click(ALL_ITEMS_LINK);
    }

    public void clickAboutLink() {
        waits().click(ABOUT_LINK);
    }

    public void clickLogoutLink() {
        waits().click(LOGOUT_LINK);
    }

    public void clickResetAppStateLink() {
        waits().click(RESET_APP_STATE_LINK);
    }

    // Getters

    public By getMenuButton() {
        return MENU_BUTTON;
    }

    public By getCloseButton() {
        return CLOSE_BUTTON;
    }

    public By getMenuContainer() {
        return MENU_CONTAINER;
    }

    public By getAllItemsLink() {
        return ALL_ITEMS_LINK;
    }

    public By getAboutLink() {
        return ABOUT_LINK;
    }

    public By getLogoutLink() {
        return LOGOUT_LINK;
    }

    public By getResetAppStateLink() {
        return RESET_APP_STATE_LINK;
    }
}
