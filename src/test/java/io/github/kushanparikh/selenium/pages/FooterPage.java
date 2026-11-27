package io.github.kushanparikh.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Port of pages/footerPage.ts. */
public class FooterPage extends BasePage {

    private static final By FOOTER = By.cssSelector(".footer");
    /**
     * TS selector plus the current markup: saucedemo renamed the Twitter link to X
     * ({@code data-test="social-x"}, href x.com), which also breaks the TS spec in CI.
     */
    private static final By TWITTER = By.cssSelector("[data-test='social-twitter'], [data-test='social-x']");
    private static final By FACEBOOK = By.cssSelector("[data-test='social-facebook']");
    private static final By LINKEDIN = By.cssSelector("[data-test='social-linkedin']");
    private static final By COPYRIGHT = By.cssSelector(".footer_copy");

    public FooterPage(WebDriver driver) {
        super(driver);
    }

    // Getters

    public By getFooterContainer() {
        return FOOTER;
    }

    public By getTwitterLink() {
        return TWITTER;
    }

    public By getFacebookLink() {
        return FACEBOOK;
    }

    public By getLinkedInLink() {
        return LINKEDIN;
    }

    public By getCopyrightText() {
        return COPYRIGHT;
    }

    // Actions

    /** Playwright's getAttribute('href'): the raw attribute, or null when absent. */
    public String getTwitterLinkHref() {
        return href(TWITTER);
    }

    public String getFacebookLinkHref() {
        return href(FACEBOOK);
    }

    public String getLinkedInLinkHref() {
        return href(LINKEDIN);
    }

    /**
     * Playwright's {@code textContent() || ''}: the raw DOM textContent (not Selenium's rendered getText()),
     * with null mapped to the empty string.
     */
    public String getCopyrightTextContent() {
        String text = waits().visible(COPYRIGHT).getDomProperty("textContent");
        return text == null ? "" : text;
    }

    private String href(By locator) {
        return waits().until("presence of " + locator, d -> d.findElement(locator)).getDomAttribute("href");
    }
}
