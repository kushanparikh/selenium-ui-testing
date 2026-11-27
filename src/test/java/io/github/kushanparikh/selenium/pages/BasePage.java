package io.github.kushanparikh.selenium.pages;

import io.github.kushanparikh.selenium.config.Config;
import io.github.kushanparikh.selenium.support.Waits;
import org.openqa.selenium.WebDriver;

/** Shared by all page objects: the driver, explicit waits, URL and title. */
public abstract class BasePage {

    protected final WebDriver driver;
    private final Waits waits;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver, Config.timeout());
    }

    public Waits waits() {
        return waits;
    }

    public String getURL() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
