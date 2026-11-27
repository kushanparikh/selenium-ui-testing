package io.github.kushanparikh.selenium.support;

import io.github.kushanparikh.selenium.config.Config;
import io.github.kushanparikh.selenium.driver.DriverFactory;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Selenium counterpart of Playwright's built-in "page" fixture: a fresh browser for every test,
 * a screenshot at the end of every test (Playwright screenshot: 'on'), and the browser closed afterwards,
 * pass or fail. If the browser can't start, the exception propagates and the test is reported as an error.
 */
public class DriverExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private static final Logger LOG = LoggerFactory.getLogger(DriverExtension.class);
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DriverExtension.class);
    private static final ThreadLocal<ExtensionContext> CURRENT = new ThreadLocal<>();

    /** The running test's context, so helpers can save attachments without it being passed around. */
    public static ExtensionContext currentContext() {
        ExtensionContext context = CURRENT.get();
        if (context == null) {
            throw new IllegalStateException("No test is running on this thread");
        }
        return context;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        CURRENT.set(context);
        WebDriver driver = DriverFactory.create(Config.browser(), Config.headless());
        context.getStore(NAMESPACE).put(WebDriver.class, driver);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        WebDriver driver = context.getStore(NAMESPACE).remove(WebDriver.class, WebDriver.class);
        try {
            if (driver != null) {
                try {
                    byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Attachments.save(context, "final", "png", png);
                } catch (RuntimeException e) {
                    LOG.warn("Final screenshot failed: {}", e.getMessage());
                } finally {
                    driver.quit();
                }
            }
        } finally {
            CURRENT.remove();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == WebDriver.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(NAMESPACE).get(WebDriver.class, WebDriver.class);
    }
}
