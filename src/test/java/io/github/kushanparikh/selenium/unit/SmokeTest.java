package io.github.kushanparikh.selenium.unit;

import io.github.kushanparikh.selenium.support.BaseTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** End-to-end check of the test base: driver per test, base URL, final screenshot. */
class SmokeTest extends BaseTest {

    @Test
    void opensSauceDemo() {
        open();

        assertThat(driver.getTitle()).isEqualTo("Swag Labs");
    }
}
