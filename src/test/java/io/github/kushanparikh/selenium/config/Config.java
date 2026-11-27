package io.github.kushanparikh.selenium.config;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Locale;

/**
 * All runtime settings in one place (the Java counterpart of playwright.config.ts).
 * Each value comes from a system property (-Dname=value, passed through by Surefire) or a default.
 * Values are read on every call, so tests can change a property and see the effect.
 */
public final class Config {

    private Config() {
    }

    public static String baseUrl() {
        return property("baseUrl", "https://www.saucedemo.com/");
    }

    public static Browser browser() {
        return Browser.from(property("browser", "chrome"));
    }

    /** Headless by default on CI, headed locally. */
    public static boolean headless() {
        return Boolean.parseBoolean(property("headless", String.valueOf(isCi())));
    }

    /** Explicit-wait timeout; Playwright's expect.timeout was 10 s. */
    public static Duration timeout() {
        return Duration.ofSeconds(Long.parseLong(property("timeoutSeconds", "10")));
    }

    public static boolean isCi() {
        String ci = System.getenv("CI");
        return ci != null && !ci.isBlank();
    }

    public static boolean updateSnapshots() {
        return Boolean.parseBoolean(property("updateSnapshots", "false"));
    }

    /**
     * Whether a missing visual baseline is written from the actual image (Playwright's behaviour). On by default
     * locally, off on CI: with retries, a written baseline made the retry compare the page against itself and pass.
     */
    public static boolean writeMissingBaselines() {
        return Boolean.parseBoolean(property("writeMissingBaselines", String.valueOf(!isCi())));
    }

    /** OS id used in visual baseline names: windows, mac or linux. */
    public static String os() {
        String name = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (name.contains("win")) {
            return "windows";
        }
        if (name.contains("mac")) {
            return "mac";
        }
        return "linux";
    }

    /** Repository root; Surefire passes it as -DrepoRoot, otherwise the working directory. */
    public static Path repoRoot() {
        return Path.of(property("repoRoot", "")).toAbsolutePath().normalize();
    }

    /** A blank property counts as unset, so the pom can pass empty defaults through. */
    private static String property(String name, String defaultValue) {
        String value = System.getProperty(name);
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }
}
