package io.github.kushanparikh.selenium.support;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Explicit waits: the Selenium counterpart of Playwright's auto-waiting locators and retrying expect().
 * Every method polls until its condition holds or the timeout passes; a timeout becomes an AssertionError
 * so it reads as a test failure, not an infrastructure error.
 */
public final class Waits {

    private final WebDriver driver;
    private final Duration timeout;

    public Waits(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.timeout = timeout;
    }

    public WebElement visible(By locator) {
        return until("visibility of " + locator, ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator) {
        return until("clickability of " + locator, ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Finds and clicks in one retried step, like Playwright's locator.click(). Splitting it into
     * clickable(locator).click() races with re-renders: saucedemo is a React app, so after a route change the
     * element found a moment ago can be replaced before the click lands (StaleElementReferenceException), or
     * be covered by the sliding menu (ElementClickInterceptedException). Both are retried until the timeout.
     */
    public void click(By locator) {
        until("click on " + locator, d -> {
            WebElement element = d.findElement(locator);
            if (!element.isDisplayed() || !element.isEnabled()) {
                return null;
            }
            try {
                element.click();
                return true;
            } catch (ElementClickInterceptedException e) {
                return null;
            }
        });
    }

    /** Waits until at least one element matches, then returns all matches. */
    public List<WebElement> all(By locator) {
        return until("at least one " + locator, d -> {
            List<WebElement> found = d.findElements(locator);
            return found.isEmpty() ? null : found;
        });
    }

    /** Waits until no matching element is present or displayed (e.g. a cart badge after the last removal). */
    public void absent(By locator) {
        until("absence of " + locator, d -> d.findElements(locator).stream().noneMatch(WebElement::isDisplayed));
    }

    public void text(By locator, String expected) {
        until("text '" + expected + "' in " + locator,
                d -> expected.equals(d.findElement(locator).getText()));
    }

    public void containsText(By locator, String expected) {
        until("text containing '" + expected + "' in " + locator,
                d -> d.findElement(locator).getText().contains(expected));
    }

    public void urlContains(String fragment) {
        until("URL containing '" + fragment + "'", ExpectedConditions.urlContains(fragment));
    }

    public <T> T until(Function<WebDriver, T> condition) {
        return until("condition " + condition, condition);
    }

    public <T> T until(String description, Function<WebDriver, T> condition) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
        try {
            return wait.until(condition::apply);
        } catch (TimeoutException e) {
            throw new AssertionError("Timed out after " + timeout.toSeconds() + "s waiting for " + description, e);
        }
    }
}
