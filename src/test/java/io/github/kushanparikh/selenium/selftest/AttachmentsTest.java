package io.github.kushanparikh.selenium.selftest;

import io.github.kushanparikh.selenium.config.Config;
import io.github.kushanparikh.selenium.support.Attachments;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("selftest")
class AttachmentsTest {

    @Test
    void relativePathIsPosix() {
        Path file = Config.repoRoot().resolve("results").resolve("attachments").resolve("a.png");

        String relative = Attachments.relativePath(file);

        assertThat(relative).isEqualTo("results/attachments/a.png");
        assertThat(relative).doesNotContain("\\").doesNotStartWith("/");
    }

    @Test
    void pathOutsideRepoIsRejected() {
        Path outside = Config.repoRoot().getParent().resolve("somewhere-else.png");

        assertThatThrownBy(() -> Attachments.relativePath(outside))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
