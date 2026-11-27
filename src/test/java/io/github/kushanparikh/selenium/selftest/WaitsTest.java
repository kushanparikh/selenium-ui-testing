package io.github.kushanparikh.selenium.selftest;

import io.github.kushanparikh.selenium.config.Browser;
import io.github.kushanparikh.selenium.driver.DriverFactory;
import io.github.kushanparikh.selenium.support.Waits;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Framework self-test: needs headless Chrome but not saucedemo.com. */
@Tag("selftest")
class WaitsTest {

    private WebDriver driver;

    @BeforeEach
    void startBrowser() {
        driver = DriverFactory.create(Browser.CHROME, true);
        driver.get("about:blank");
    }

    @AfterEach
    void stopBrowser() {
        driver.quit();
    }

    @Test
    void absentWaitsForRemoval() {
        ((JavascriptExecutor) driver).executeScript(
                "const x = document.createElement('div'); x.id = 'x'; x.textContent = 'badge';"
                        + "document.body.appendChild(x);"
                        + "setTimeout(() => x.remove(), 1000);");
        Waits waits = new Waits(driver, Duration.ofSeconds(5));

        long start = System.nanoTime();
        waits.absent(By.id("x"));
        long elapsedMs = Duration.ofNanos(System.nanoTime() - start).toMillis();

        assertThat(elapsedMs).isGreaterThanOrEqualTo(900);
        assertThat(driver.findElements(By.id("x"))).isEmpty();
    }

    @Test
    void timeoutMessage() {
        Waits waits = new Waits(driver, Duration.ofSeconds(1));

        assertThatThrownBy(() -> waits.visible(By.id("never")))
                .isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Timed out after 1s waiting for");
    }
}
