package io.github.kushanparikh.selenium.selftest;

import io.github.kushanparikh.selenium.config.Config;
import io.github.kushanparikh.selenium.driver.DriverFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The page area (viewport) is 1280x720 on every browser, like Playwright's Desktop devices. Sizing the window to
 * 1280x720 is not enough: browser toolbars take part of it (Firefox: 67 px). Runs on the browser selected with
 * -Dbrowser, so run it once per browser.
 */
@Tag("selftest")
class DriverFactoryTest {

    @Test
    void viewportIs1280x720() {
        WebDriver driver = DriverFactory.create(Config.browser(), Config.headless());
        try {
            driver.get("about:blank");
            Object size = ((JavascriptExecutor) driver)
                    .executeScript("return [window.innerWidth, window.innerHeight];");

            assertThat((List<Object>) size).as("viewport [width, height]").containsExactly(1280L, 720L);
        } finally {
            driver.quit();
        }
    }
}
