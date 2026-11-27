package io.github.kushanparikh.selenium.selftest;

import io.github.kushanparikh.selenium.support.Waits;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Waits.click must survive the element being re-rendered between "find" and "click" (React replaces the
 * header when saucedemo changes route). No real browser can hit that window on demand, so this uses a fake
 * driver whose element goes stale on the first click.
 */
@Tag("selftest")
class WaitsClickTest {

    @Test
    void clickRetriesWhenElementGoesStale() {
        assertClickRetriesAfter(new StaleElementReferenceException("re-rendered"));
    }

    /** Safari on CI: the element exists but isn't interactable yet (ElementNotInteractableException). */
    @Test
    void clickRetriesWhenElementIsNotInteractableYet() {
        assertClickRetriesAfter(new ElementNotInteractableException("not interactable yet"));
    }

    private void assertClickRetriesAfter(WebDriverException firstClickFailure) {
        AtomicInteger clicks = new AtomicInteger();
        AtomicInteger finds = new AtomicInteger();
        WebElement element = (WebElement) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[]{WebElement.class}, (proxy, method, args) -> switch (method.getName()) {
                    case "isDisplayed", "isEnabled" -> true;
                    case "click" -> {
                        if (clicks.incrementAndGet() == 1) {
                            throw firstClickFailure;
                        }
                        yield null;
                    }
                    case "toString" -> "fake element";
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        WebDriver driver = (WebDriver) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[]{WebDriver.class}, (proxy, method, args) -> switch (method.getName()) {
                    case "findElement" -> {
                        finds.incrementAndGet();
                        yield element;
                    }
                    case "toString" -> "fake driver";
                    default -> throw new UnsupportedOperationException(method.getName());
                });

        new Waits(driver, Duration.ofSeconds(2)).click(By.id("menu"));

        assertThat(clicks.get()).isEqualTo(2);
        assertThat(finds.get()).as("element is looked up again after the failed click").isEqualTo(2);
    }
}
