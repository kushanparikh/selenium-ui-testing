package io.github.kushanparikh.selenium.support;

import com.deque.html.axecore.results.CheckedNode;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import io.github.kushanparikh.selenium.tests.accessibility.KnownAccessibilityIssues;
import io.github.kushanparikh.selenium.tests.accessibility.KnownAccessibilityIssues.KnownIssue;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Port of checkAccessibility() in tests/accessibility/accessibility.spec.ts: scans the current page with axe-core
 * against WCAG 2.1 A/AA, splits the violations into known (allow-listed in {@link KnownAccessibilityIssues}) and
 * new ones, and attaches the same plain-text summary the TS helper attaches.
 */
public final class AccessibilityChecker {

    private static final List<String> TAGS = List.of("wcag2a", "wcag2aa", "wcag21a", "wcag21aa");
    private static final String SEPARATOR = "=".repeat(80);

    /** The scan split into new and known violations; {@code total} is every violation axe reported. */
    public record Report(List<Rule> newViolations, List<Rule> knownViolations, int total) {
    }

    private AccessibilityChecker() {
    }

    public static Report check(WebDriver driver, ExtensionContext context, String pageName) {
        Results results = new AxeBuilder().withTags(TAGS).analyze(driver);
        if (results.getErrorMessage() != null) {
            throw new IllegalStateException("axe-core scan failed on " + pageName + ": " + results.getErrorMessage());
        }
        List<Rule> violations = results.getViolations() == null ? List.of() : results.getViolations();
        List<KnownIssue> knownIssues = KnownAccessibilityIssues.FOR_PAGE.getOrDefault(pageName, List.of());

        List<Rule> known = new ArrayList<>();
        List<Rule> fresh = new ArrayList<>();
        for (Rule violation : violations) {
            (isKnownIssue(violation, knownIssues) ? known : fresh).add(violation);
        }

        String summary = summary(pageName, violations, known, fresh, knownIssues);
        String reportName = "accessibility-report-" + pageName.replaceAll("\\s+", "-").toLowerCase(Locale.ROOT);
        Attachments.save(context, reportName, "txt", summary.getBytes(StandardCharsets.UTF_8));

        return new Report(List.copyOf(fresh), List.copyOf(known), violations.size());
    }

    /** TS: violation.impact || 'unknown'. */
    private static String impact(Rule violation) {
        String impact = violation.getImpact();
        return impact == null || impact.isEmpty() ? "unknown" : impact;
    }

    private static boolean isKnownIssue(Rule violation, List<KnownIssue> knownIssues) {
        return knownIssues.stream().anyMatch(known ->
                known.id().equals(violation.getId()) && known.impact().equals(impact(violation)));
    }

    /** Line for line the summary the TS helper builds, including its numbering quirks. */
    private static String summary(String pageName, List<Rule> violations, List<Rule> known, List<Rule> fresh,
                                  List<KnownIssue> knownIssues) {
        List<String> lines = new ArrayList<>();
        lines.add(SEPARATOR);
        lines.add("📋 Accessibility Report for " + pageName);
        lines.add(SEPARATOR);
        lines.add("📊 Total Violations Found: " + violations.size());
        lines.add("✅ Known Issues: " + known.size());
        lines.add("🚨 New Issues: " + fresh.size());

        if (!known.isEmpty()) {
            lines.add("");
            lines.add("📝 Known Issues (will not fail test):");
            for (int i = 0; i < known.size(); i++) {
                Rule violation = known.get(i);
                lines.add((i + 1) + ". " + violation.getId());
                lines.add("   Impact: " + impact(violation));
                lines.add("   Description: " + violation.getDescription());
            }
        }

        if (!fresh.isEmpty()) {
            lines.add("");
            lines.add("🚨 NEW Issues Detected (test will fail):");
            // Like the TS, new issues are numbered by their position among ALL violations, not among new ones.
            for (int i = 0; i < violations.size(); i++) {
                Rule violation = violations.get(i);
                if (isKnownIssue(violation, knownIssues)) {
                    continue;
                }
                List<CheckedNode> nodes = violation.getNodes() == null ? List.of() : violation.getNodes();
                lines.add((i + 1) + ". " + violation.getId());
                lines.add("   Impact: " + violation.getImpact());
                lines.add("   Description: " + violation.getDescription());
                lines.add("   Help: " + violation.getHelp());
                lines.add("   Help URL: " + violation.getHelpUrl());
                lines.add("   Affected Elements: " + nodes.size());
                for (int n = 0; n < nodes.size(); n++) {
                    lines.add("     Element " + (n + 1) + ": " + target(nodes.get(n).getTarget()));
                }
            }
        } else if (violations.isEmpty()) {
            lines.add("");
            lines.add("✅ No accessibility violations found.");
        } else {
            lines.add("");
            lines.add("✅ No new accessibility issues detected.");
        }

        lines.add("");
        lines.add(SEPARATOR);
        return String.join("\n", lines);
    }

    /** TS: node.target.join(", "). Nested selectors (iframes, shadow DOM) join with "," like JS Array#toString. */
    private static String target(Object target) {
        if (target instanceof List<?> parts) {
            return parts.stream().map(AccessibilityChecker::jsString).collect(Collectors.joining(", "));
        }
        return jsString(target);
    }

    private static String jsString(Object value) {
        if (value instanceof List<?> parts) {
            return parts.stream().map(AccessibilityChecker::jsString).collect(Collectors.joining(","));
        }
        return value == null ? "" : value.toString();
    }
}
