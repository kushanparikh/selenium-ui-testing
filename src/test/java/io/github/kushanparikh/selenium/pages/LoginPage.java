package io.github.kushanparikh.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** Port of pages/loginPage.ts. */
public class LoginPage extends BasePage {

    private static final By USERNAME = By.cssSelector("[placeholder='Username']");
    private static final By PASSWORD = By.cssSelector("[placeholder='Password']");
    private static final By LOGIN_BUTTON = By.cssSelector("input[type='submit'][value='Login']");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private static final By LOGO = By.cssSelector(".login_logo");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Actions

    /** Playwright's fill() clears the field first; Selenium's sendKeys() appends, so clear explicitly. */
    public void login(String username, String password) {
        fill(USERNAME, username);
        fill(PASSWORD, password);
        waits().clickable(LOGIN_BUTTON).click();
    }

    // Getters

    public String getDisplayedErrorMessage() {
        return waits().visible(ERROR).getText();
    }

    public By getLogo() {
        return LOGO;
    }

    public By getUsernameField() {
        return USERNAME;
    }

    public By getPasswordField() {
        return PASSWORD;
    }

    public By getLoginButton() {
        return LOGIN_BUTTON;
    }

    private void fill(By locator, String value) {
        WebElement field = waits().visible(locator);
        field.clear();
        field.sendKeys(value);
    }
}
