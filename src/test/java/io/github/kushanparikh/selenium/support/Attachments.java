package io.github.kushanparikh.selenium.support;

import io.github.kushanparikh.selenium.config.Config;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes test evidence (screenshots, diffs, a11y reports) as files and references them from the test's stdout
 * with a Jenkins-style "[[ATTACHMENT|path]]" line, which Surefire puts in the JUnit XML's system-out.
 * Bodies are never embedded in the report (utr contract), and paths are always repo-relative POSIX paths.
 */
public final class Attachments {

    private Attachments() {
    }

    /** Repo-relative path with forward slashes, on every OS. */
    public static String relativePath(Path absolute) {
        Path root = Config.repoRoot();
        Path normalized = absolute.toAbsolutePath().normalize();
        if (!normalized.startsWith(root)) {
            throw new IllegalArgumentException("Path is outside the repository: " + normalized.getFileName());
        }
        StringBuilder posix = new StringBuilder();
        for (Path part : root.relativize(normalized)) {
            if (!posix.isEmpty()) {
                posix.append('/');
            }
            posix.append(part);
        }
        return posix.toString();
    }

    /** Saves results/attachments/<browser>/<TestClass>/<method>/<name>.<ext> and prints its reference. */
    public static Path save(ExtensionContext context, String name, String ext, byte[] body) {
        String testClass = context.getRequiredTestClass().getName()
                .substring(context.getRequiredTestClass().getPackageName().length() + 1);
        Path file = Config.repoRoot()
                .resolve("results").resolve("attachments")
                .resolve(Config.browser().id())
                .resolve(testClass)
                .resolve(context.getRequiredTestMethod().getName())
                .resolve(name + "." + ext);
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, body);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write attachment " + relativePath(file), e);
        }
        System.out.println("[[ATTACHMENT|" + relativePath(file) + "]]");
        return file;
    }
}
