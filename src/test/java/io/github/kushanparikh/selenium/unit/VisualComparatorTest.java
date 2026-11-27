package io.github.kushanparikh.selenium.unit;

import io.github.kushanparikh.selenium.config.Browser;
import io.github.kushanparikh.selenium.config.Config;
import io.github.kushanparikh.selenium.driver.DriverFactory;
import io.github.kushanparikh.selenium.support.Attachments;
import io.github.kushanparikh.selenium.support.VisualComparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Framework self-test for the Playwright-equivalent image comparison (synthetic images, no saucedemo.com). */
@Tag("unit")
class VisualComparatorTest {

    private static BufferedImage filled(int width, int height, Color colour) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, colour.getRGB());
            }
        }
        return image;
    }

    @Test
    void identicalImagesPass() {
        VisualComparator.Result result = VisualComparator.compare(
                filled(100, 100, Color.WHITE), filled(100, 100, Color.WHITE));

        assertThat(result.sizeMatches()).isTrue();
        assertThat(result.diffPixels()).isZero();
        assertThat(result.passed()).isTrue();
    }

    @Test
    void sizeMismatchFails() {
        VisualComparator.Result result = VisualComparator.compare(
                filled(100, 100, Color.WHITE), filled(100, 101, Color.WHITE));

        assertThat(result.sizeMatches()).isFalse();
        assertThat(result.passed()).isFalse();
    }

    @Test
    void allowedIsStricterOfTheTwo() {
        assertThat(VisualComparator.compare(filled(100, 100, Color.WHITE), filled(100, 100, Color.WHITE))
                .allowedDiffPixels()).isEqualTo(100);
        assertThat(VisualComparator.compare(filled(50, 50, Color.WHITE), filled(50, 50, Color.WHITE))
                .allowedDiffPixels()).isEqualTo(25);
    }

    @Test
    void smallColourShiftIgnored() {
        VisualComparator.Result result = VisualComparator.compare(
                filled(100, 100, Color.WHITE), filled(100, 100, new Color(250, 250, 250)));

        assertThat(result.diffPixels()).isZero();
    }

    @Test
    void blackOnWhiteCounts() {
        BufferedImage actual = filled(100, 100, Color.WHITE);
        for (int x = 0; x < 10; x++) {
            actual.setRGB(x, 5, Color.BLACK.getRGB());
        }

        VisualComparator.Result result = VisualComparator.compare(filled(100, 100, Color.WHITE), actual);

        assertThat(result.diffPixels()).isEqualTo(10);
        assertThat(result.passed()).isTrue();
        assertThat(result.diff().getRGB(0, 5)).isNotEqualTo(result.diff().getRGB(50, 50));
    }

    @Test
    void defaultBaselinePathIsRepoRelative() {
        String expected = "src/test/resources/visual-baselines/login-page-"
                + Config.browser().id() + "-" + Config.os() + ".png";

        assertThat(Attachments.relativePath(VisualComparator.baselinePath("login-page"))).isEqualTo(expected);
    }

    /** Needs headless Chrome (a blank page), but not saucedemo.com. */
    @Nested
    class MissingBaseline {

        private ExtensionContext context;
        private final WebDriver driver = DriverFactory.create(Browser.CHROME, true);
        private final Path baselineDir = Config.repoRoot()
                .resolve("target").resolve("visual-baselines-test").resolve(UUID.randomUUID().toString());

        @RegisterExtension
        final BeforeEachCallback captureContext = ctx -> context = ctx;

        @AfterEach
        void cleanUp() throws IOException {
            driver.quit();
            if (Files.exists(baselineDir)) {
                try (Stream<Path> files = Files.walk(baselineDir)) {
                    for (Path file : files.sorted(Comparator.reverseOrder()).toList()) {
                        Files.delete(file);
                    }
                }
            }
        }

        private Path expectedBaseline(String name) {
            return baselineDir.resolve(name + "-" + Config.browser().id() + "-" + Config.os() + ".png");
        }

        @Test
        void missingBaselineWritesActualAndFails() {
            driver.get("data:text/html,<h1>visual</h1>");

            assertThatThrownBy(() -> VisualComparator.assertMatches(driver, context, "blank", baselineDir))
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining("Missing baseline")
                    .hasMessageContaining("wrote actual. Re-run to compare.");
            assertThat(expectedBaseline("blank")).exists();

            // Second run compares against the baseline just written and passes, like Playwright.
            VisualComparator.assertMatches(driver, context, "blank", baselineDir);
        }

        @Test
        void missingBaselineMessageIsRepoRelative() {
            driver.get("data:text/html,<h1>visual</h1>");
            String relative = Attachments.relativePath(expectedBaseline("blank"));

            assertThatThrownBy(() -> VisualComparator.assertMatches(driver, context, "blank", baselineDir))
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining(relative)
                    .hasMessageNotContaining(Config.repoRoot().toString())
                    .satisfies(e -> assertThat(e.getMessage()).doesNotContain(" /").doesNotStartWith("/"));
            assertThat(relative).startsWith("target/visual-baselines-test/");
        }
    }
}
