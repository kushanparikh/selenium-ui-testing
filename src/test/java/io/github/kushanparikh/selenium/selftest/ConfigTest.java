package io.github.kushanparikh.selenium.selftest;

import io.github.kushanparikh.selenium.config.Browser;
import io.github.kushanparikh.selenium.config.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("selftest")
class ConfigTest {

    private String savedBrowser;

    @BeforeEach
    void saveBrowserProperty() {
        savedBrowser = System.getProperty("browser");
    }

    @AfterEach
    void restoreBrowserProperty() {
        if (savedBrowser == null) {
            System.clearProperty("browser");
        } else {
            System.setProperty("browser", savedBrowser);
        }
    }

    @Test
    void browserDefaultsToChrome() {
        System.clearProperty("browser");
        assertThat(Config.browser()).isEqualTo(Browser.CHROME);
    }

    @Test
    void browserFromIsCaseInsensitive() {
        assertThat(Browser.from("FireFox")).isEqualTo(Browser.FIREFOX);
    }

    @Test
    void unknownBrowserMessage() {
        assertThatThrownBy(() -> Browser.from("edge"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown browser 'edge'. Use chrome, firefox or safari.");
    }

    @Test
    void timeoutDefaultsToTenSeconds() {
        assertThat(Config.timeout()).isEqualTo(Duration.ofSeconds(10));
    }

    @Test
    void baseUrlDefault() {
        assertThat(Config.baseUrl()).isEqualTo("https://www.saucedemo.com/");
    }
}
