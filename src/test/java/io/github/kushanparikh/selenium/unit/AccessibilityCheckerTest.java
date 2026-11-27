package io.github.kushanparikh.selenium.unit;

import com.deque.html.axecore.results.Rule;
import io.github.kushanparikh.selenium.support.AccessibilityChecker;
import io.github.kushanparikh.selenium.tests.accessibility.KnownAccessibilityIssues.KnownIssue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Known issues that axe no longer reports are flagged as stale (they don't fail the test). */
@Tag("unit")
class AccessibilityCheckerTest {

    private static final KnownIssue SELECT_NAME =
            new KnownIssue("select-name", "critical", "Ensure select element has an accessible name");

    @Test
    void knownIssueStillReportedIsNotStale() {
        assertThat(AccessibilityChecker.staleKnownIssues(List.of(SELECT_NAME), List.of(rule("select-name", "critical"))))
                .isEmpty();
    }

    @Test
    void knownIssueNoLongerReportedIsStale() {
        assertThat(AccessibilityChecker.staleKnownIssues(List.of(SELECT_NAME), List.of())).containsExactly(SELECT_NAME);
    }

    @Test
    void sameIdWithDifferentImpactIsStale() {
        assertThat(AccessibilityChecker.staleKnownIssues(List.of(SELECT_NAME), List.of(rule("select-name", "serious"))))
                .containsExactly(SELECT_NAME);
    }

    private static Rule rule(String id, String impact) {
        Rule rule = new Rule();
        rule.setId(id);
        rule.setImpact(impact);
        return rule;
    }
}
