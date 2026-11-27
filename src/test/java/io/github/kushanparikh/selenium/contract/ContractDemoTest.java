package io.github.kushanparikh.selenium.contract;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Proves retries are recorded in the JUnit XML (utr reads retries as the flakiness signal).
 * Excluded by default; run with -Dgroups=contract-demo -DexcludedGroups= -Dsurefire.rerunFailingTestsCount=2.
 * Attempts alternate fail/pass using a counter file, so every run's first attempt fails and its retry passes.
 */
@Tag("contract-demo")
class ContractDemoTest {

    private static final Path COUNTER = Path.of("target", "contract-demo-attempts");

    @Test
    void failsOnceThenPasses() throws IOException {
        int attempt = Files.exists(COUNTER) ? Integer.parseInt(Files.readString(COUNTER).trim()) : 0;
        Files.createDirectories(COUNTER.getParent());
        Files.writeString(COUNTER, String.valueOf(attempt + 1));

        assertThat(attempt % 2).as("attempt %d (first attempt of each run fails on purpose)", attempt).isEqualTo(1);
    }
}
