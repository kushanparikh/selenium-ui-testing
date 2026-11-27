package io.github.kushanparikh.selenium.support;

import io.github.kushanparikh.selenium.config.Config;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The Java counterpart of Playwright's expect(page).toHaveScreenshot(name, { fullPage: true }) with the options in
 * playwright.config.ts: per-pixel threshold 0.2 (pixelmatch's YIQ colour delta), and at most
 * min(maxDiffPixels 100, maxDiffPixelRatio 1%) differing pixels. Images of different sizes never match.
 * Like pixelmatch's default (includeAA: false), anti-aliased pixels are detected and not counted: font edges vary
 * slightly from run to run, and counting them made the visual tests flaky.
 *
 * <p>Baselines live in src/test/resources/visual-baselines/&lt;name&gt;-&lt;browser&gt;-&lt;os&gt;.png. A missing baseline
 * is written from the actual image and the test fails (a re-run compares); -DupdateSnapshots=true overwrites the
 * baseline and passes. On CI a missing baseline is not written, so every retry fails too (see
 * Config.writeMissingBaselines). Every path in a failure message is repo-relative.
 */
public final class VisualComparator {

    /** pixelmatch's maximum possible YIQ delta. */
    private static final double MAX_YIQ_DELTA = 35215;
    private static final double THRESHOLD = 0.2;
    private static final double MAX_DELTA = MAX_YIQ_DELTA * THRESHOLD * THRESHOLD;
    private static final int MAX_DIFF_PIXELS = 100;
    private static final double MAX_DIFF_PIXEL_RATIO = 0.01;

    private static final int DIFF_COLOUR = 0xFFFF0000;
    /** pixelmatch draws detected anti-aliased pixels in yellow. */
    private static final int ANTI_ALIASED_COLOUR = 0xFFFFFF00;

    private VisualComparator() {
    }

    /** sizeMatches false means diffPixels is meaningless and diff is null. */
    public record Result(boolean sizeMatches, int diffPixels, int allowedDiffPixels, BufferedImage diff) {
        public boolean passed() {
            return sizeMatches && diffPixels <= allowedDiffPixels;
        }
    }

    public static Path defaultBaselineDir() {
        return Config.repoRoot().resolve("src").resolve("test").resolve("resources").resolve("visual-baselines");
    }

    /** The baseline file for this name, browser and OS in the default baseline directory. */
    public static Path baselinePath(String name) {
        return baselinePath(defaultBaselineDir(), name);
    }

    private static Path baselinePath(Path baselineDir, String name) {
        return baselineDir.resolve(name + "-" + Config.browser().id() + "-" + Config.os() + ".png");
    }

    public static Result compare(BufferedImage expected, BufferedImage actual) {
        int width = expected.getWidth();
        int height = expected.getHeight();
        int allowed = (int) Math.min(MAX_DIFF_PIXELS, Math.floor((double) width * height * MAX_DIFF_PIXEL_RATIO));
        if (width != actual.getWidth() || height != actual.getHeight()) {
            return new Result(false, 0, allowed, null);
        }
        BufferedImage diff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int diffPixels = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = expected.getRGB(x, y);
                int b = actual.getRGB(x, y);
                if (a != b && colourDelta(a, b) > MAX_DELTA) {
                    if (antialiased(expected, x, y, width, height, actual)
                            || antialiased(actual, x, y, width, height, expected)) {
                        diff.setRGB(x, y, ANTI_ALIASED_COLOUR);
                    } else {
                        diffPixels++;
                        diff.setRGB(x, y, DIFF_COLOUR);
                    }
                } else {
                    diff.setRGB(x, y, faded(a));
                }
            }
        }
        return new Result(true, diffPixels, allowed, diff);
    }

    /** Compares a full-page screenshot with its baseline in src/test/resources/visual-baselines. */
    public static void assertMatches(WebDriver driver, ExtensionContext context, String name) {
        assertMatches(driver, context, name, defaultBaselineDir());
    }

    /** Same, with another baseline directory (inside the repository, so messages can show it repo-relative). */
    public static void assertMatches(WebDriver driver, ExtensionContext context, String name, Path baselineDir) {
        byte[] actualPng = FullPageScreenshot.capture(driver);
        Path baseline = baselinePath(baselineDir, name);
        String baselineRef = Attachments.relativePath(baseline);

        if (!Files.exists(baseline) && !Config.updateSnapshots() && !Config.writeMissingBaselines()) {
            Attachments.save(context, name + "-actual", "png", actualPng);
            throw new AssertionError("Missing baseline " + baselineRef + "; not written on CI (the screenshot is the "
                    + name + "-actual.png attachment; commit it as the baseline or run with -DupdateSnapshots=true).");
        }
        if (Config.updateSnapshots() || !Files.exists(baseline)) {
            boolean missing = !Files.exists(baseline);
            write(baseline, actualPng, baselineRef);
            if (missing && !Config.updateSnapshots()) {
                Attachments.save(context, name + "-actual", "png", actualPng);
                throw new AssertionError("Missing baseline " + baselineRef + "; wrote actual. Re-run to compare.");
            }
            return;
        }

        BufferedImage expected = read(readBytes(baseline, baselineRef), baselineRef);
        BufferedImage actual = read(actualPng, name + " screenshot");
        Result result = compare(expected, actual);
        if (result.passed()) {
            return;
        }
        Attachments.save(context, name + "-expected", "png", readBytes(baseline, baselineRef));
        Attachments.save(context, name + "-actual", "png", actualPng);
        if (!result.sizeMatches()) {
            throw new AssertionError(name + ": image size " + actual.getWidth() + "x" + actual.getHeight()
                    + " differs from baseline " + expected.getWidth() + "x" + expected.getHeight()
                    + " (" + baselineRef + ")");
        }
        Attachments.save(context, name + "-diff", "png", png(result.diff()));
        throw new AssertionError(name + ": " + result.diffPixels() + " pixels differ (allowed "
                + result.allowedDiffPixels() + ")");
    }

    /** pixelmatch colorDelta (without its sign): YIQ distance after blending any transparency onto white. */
    private static double colourDelta(int a, int b) {
        double r1 = blend(a >>> 16 & 0xFF, a >>> 24);
        double g1 = blend(a >>> 8 & 0xFF, a >>> 24);
        double b1 = blend(a & 0xFF, a >>> 24);
        double r2 = blend(b >>> 16 & 0xFF, b >>> 24);
        double g2 = blend(b >>> 8 & 0xFF, b >>> 24);
        double b2 = blend(b & 0xFF, b >>> 24);

        double dy = y(r1, g1, b1) - y(r2, g2, b2);
        double di = i(r1, g1, b1) - i(r2, g2, b2);
        double dq = q(r1, g1, b1) - q(r2, g2, b2);
        return 0.5053 * dy * dy + 0.299 * di * di + 0.1957 * dq * dq;
    }

    /**
     * Port of pixelmatch's antialiased(): the pixel at (x1, y1) in img is anti-aliased if, among its 8 neighbours,
     * at most 2 have the same brightness, some are darker and some lighter, and the darkest or the lightest
     * neighbour sits in a flat area (3+ identical neighbours) in both images.
     */
    private static boolean antialiased(BufferedImage img, int x1, int y1, int width, int height, BufferedImage img2) {
        int x0 = Math.max(x1 - 1, 0);
        int y0 = Math.max(y1 - 1, 0);
        int x2 = Math.min(x1 + 1, width - 1);
        int y2 = Math.min(y1 + 1, height - 1);
        int centre = img.getRGB(x1, y1);
        int zeroes = x1 == x0 || x1 == x2 || y1 == y0 || y1 == y2 ? 1 : 0;
        double min = 0;
        double max = 0;
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        for (int x = x0; x <= x2; x++) {
            for (int y = y0; y <= y2; y++) {
                if (x == x1 && y == y1) {
                    continue;
                }
                double delta = brightnessDelta(centre, img.getRGB(x, y));
                if (delta == 0) {
                    zeroes++;
                    if (zeroes > 2) {
                        return false;
                    }
                } else if (delta < min) {
                    min = delta;
                    minX = x;
                    minY = y;
                } else if (delta > max) {
                    max = delta;
                    maxX = x;
                    maxY = y;
                }
            }
        }
        if (min == 0 || max == 0) {
            return false;
        }
        return (hasManySiblings(img, minX, minY, width, height) && hasManySiblings(img2, minX, minY, width, height))
                || (hasManySiblings(img, maxX, maxY, width, height) && hasManySiblings(img2, maxX, maxY, width, height));
    }

    /** Port of pixelmatch's hasManySiblings(): 3+ neighbours (image edge counts as one) with exactly the same colour. */
    private static boolean hasManySiblings(BufferedImage img, int x1, int y1, int width, int height) {
        int x0 = Math.max(x1 - 1, 0);
        int y0 = Math.max(y1 - 1, 0);
        int x2 = Math.min(x1 + 1, width - 1);
        int y2 = Math.min(y1 + 1, height - 1);
        int centre = img.getRGB(x1, y1);
        int zeroes = x1 == x0 || x1 == x2 || y1 == y0 || y1 == y2 ? 1 : 0;
        for (int x = x0; x <= x2; x++) {
            for (int y = y0; y <= y2; y++) {
                if (x == x1 && y == y1) {
                    continue;
                }
                if (img.getRGB(x, y) == centre) {
                    zeroes++;
                }
                if (zeroes > 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /** pixelmatch's colorDelta(..., yOnly = true): signed brightness difference after blending onto white. */
    private static double brightnessDelta(int a, int b) {
        return y(blend(a >>> 16 & 0xFF, a >>> 24), blend(a >>> 8 & 0xFF, a >>> 24), blend(a & 0xFF, a >>> 24))
                - y(blend(b >>> 16 & 0xFF, b >>> 24), blend(b >>> 8 & 0xFF, b >>> 24), blend(b & 0xFF, b >>> 24));
    }

    private static double blend(int channel, int alpha) {
        return 255 + (channel - 255) * (alpha / 255.0);
    }

    private static double y(double r, double g, double b) {
        return r * 0.29889531 + g * 0.58662247 + b * 0.11448223;
    }

    private static double i(double r, double g, double b) {
        return r * 0.59597799 - g * 0.27417610 - b * 0.32180189;
    }

    private static double q(double r, double g, double b) {
        return r * 0.21147017 - g * 0.52261711 + b * 0.31114694;
    }

    /** Unchanged pixels as pale grey, like pixelmatch's diff output (alpha 0.1 over white). */
    private static int faded(int argb) {
        double luma = y(argb >>> 16 & 0xFF, argb >>> 8 & 0xFF, argb & 0xFF);
        int grey = (int) Math.round(255 + (luma - 255) * 0.1);
        return 0xFF000000 | grey << 16 | grey << 8 | grey;
    }

    private static void write(Path file, byte[] body, String ref) {
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, body);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write baseline " + ref, e);
        }
    }

    private static byte[] readBytes(Path file, String ref) {
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read baseline " + ref, e);
        }
    }

    private static BufferedImage read(byte[] png, String what) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(png));
            if (image == null) {
                throw new IllegalStateException("Not a PNG image: " + what);
            }
            return image;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not decode " + what, e);
        }
    }

    private static byte[] png(BufferedImage image) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not encode diff image", e);
        }
    }
}
