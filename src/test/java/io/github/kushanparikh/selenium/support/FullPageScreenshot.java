package io.github.kushanparikh.selenium.support;

import io.github.kushanparikh.selenium.config.Config;
import io.github.kushanparikh.selenium.driver.DriverFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Playwright's page.screenshot({ fullPage: true, animations: 'disabled', caret: 'hide' }) for Selenium.
 * <ul>
 *   <li>Chrome: CDP Page.captureScreenshot with captureBeyondViewport, clipped to the page's full content size.
 *       The viewport is pinned to 1280x720 at device scale factor 1 while capturing, so headed (Retina, browser
 *       toolbar) and headless runs produce the same image, as Playwright's "Desktop Chrome" device does.</li>
 *   <li>Firefox: the native FirefoxDriver#getFullPageScreenshotAs.</li>
 *   <li>Safari: viewport only. SafariDriver has no full-page capture; this is a documented limitation.</li>
 * </ul>
 * Like toHaveScreenshot, it waits until two consecutive captures are identical before returning.
 */
public final class FullPageScreenshot {

    /** Playwright's animations: 'disabled' and caret: 'hide'. */
    private static final String FREEZE_CSS =
            "*,*::before,*::after{animation:none!important;transition:none!important;caret-color:transparent!important}";

    private static final String INJECT_FREEZE_CSS = """
            if (!document.getElementById('__visual_freeze')) {
              const style = document.createElement('style');
              style.id = '__visual_freeze';
              style.textContent = arguments[0];
              (document.head || document.documentElement).appendChild(style);
            }""";

    private static final String PAGE_SETTLED = """
            return document.readyState === 'complete'
              && Array.from(document.images).every(img => img.complete)
              && (!document.fonts || document.fonts.status === 'loaded');""";

    private FullPageScreenshot() {
    }

    public static byte[] capture(WebDriver driver) {
        Waits waits = new Waits(driver, Config.timeout());
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(INJECT_FREEZE_CSS, FREEZE_CSS);
        waits.until("page, images and fonts to finish loading", d -> Boolean.TRUE.equals(js.executeScript(PAGE_SETTLED)));

        if (driver instanceof ChromeDriver chrome) {
            chrome.executeCdpCommand("Emulation.setDeviceMetricsOverride", Map.of(
                    "width", DriverFactory.VIEWPORT.getWidth(),
                    "height", DriverFactory.VIEWPORT.getHeight(),
                    "deviceScaleFactor", 1,
                    "mobile", false));
            try {
                return stable(waits, () -> chromeFullPage(chrome));
            } finally {
                chrome.executeCdpCommand("Emulation.clearDeviceMetricsOverride", Map.of());
            }
        }
        if (driver instanceof FirefoxDriver firefox) {
            return stable(waits, () -> firefox.getFullPageScreenshotAs(OutputType.BYTES));
        }
        return stable(waits, () -> ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
    }

    @SuppressWarnings("unchecked")
    private static byte[] chromeFullPage(ChromeDriver chrome) {
        Map<String, Object> metrics = chrome.executeCdpCommand("Page.getLayoutMetrics", Map.of());
        Map<String, Object> content = (Map<String, Object>) metrics.get("cssContentSize");
        long width = (long) Math.ceil(((Number) content.get("width")).doubleValue());
        long height = (long) Math.ceil(((Number) content.get("height")).doubleValue());
        Map<String, Object> shot = chrome.executeCdpCommand("Page.captureScreenshot", Map.of(
                "format", "png",
                "captureBeyondViewport", true,
                "clip", Map.of("x", 0, "y", 0, "width", width, "height", height, "scale", 1)));
        return Base64.getDecoder().decode((String) shot.get("data"));
    }

    /** Repeats the capture until two in a row are byte-identical (Playwright's screenshot stability check). */
    private static byte[] stable(Waits waits, java.util.function.Supplier<byte[]> shot) {
        AtomicReference<byte[]> previous = new AtomicReference<>(shot.get());
        return waits.until("two consecutive identical screenshots", d -> {
            byte[] current = shot.get();
            byte[] last = previous.getAndSet(current);
            return Arrays.equals(last, current) ? current : null;
        });
    }
}
