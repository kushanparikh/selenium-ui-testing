package io.github.kushanparikh.selenium.driver;

import io.github.kushanparikh.selenium.config.Browser;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Creates a fresh browser for each test. Drivers are resolved by Selenium Manager (built into Selenium 4),
 * so there is nothing to download by hand. No implicit wait is ever set: all waiting is explicit (see Waits).
 */
public final class DriverFactory {

    /** Playwright's "Desktop Chrome/Firefox/Safari" devices use a 1280x720 viewport. */
    public static final Dimension WINDOW_SIZE = new Dimension(1280, 720);

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);
    private static final AtomicBoolean SAFARI_HEADLESS_WARNED = new AtomicBoolean();

    private DriverFactory() {
    }

    public static WebDriver create(Browser browser, boolean headless) {
        WebDriver driver = switch (browser) {
            case CHROME -> new ChromeDriver(chromeOptions(headless));
            case FIREFOX -> new FirefoxDriver(firefoxOptions(headless));
            case SAFARI -> {
                if (headless && SAFARI_HEADLESS_WARNED.compareAndSet(false, true)) {
                    LOG.warn("Safari has no headless mode; running headed.");
                }
                yield new SafariDriver();
            }
        };
        driver.manage().window().setSize(WINDOW_SIZE);
        return driver;
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-search-engine-choice-screen");
        // saucedemo's password "secret_sauce" appears in breach lists; without this Chrome shows a
        // "change your password" dialog that blocks clicks. Playwright's bundled Chromium has no password manager.
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }
}
