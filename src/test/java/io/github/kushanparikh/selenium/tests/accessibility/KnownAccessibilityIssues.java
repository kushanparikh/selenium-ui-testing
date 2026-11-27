package io.github.kushanparikh.selenium.tests.accessibility;

import java.util.List;
import java.util.Map;

/**
 * Port of tests/accessibility/knownAccessibilityIssues.ts.
 *
 * <p>Known accessibility violations in the demo website that won't be fixed. Tests only fail if NEW issues
 * (not in this list) are detected. A violation matches a known issue on {@code id} and {@code impact}.
 */
public final class KnownAccessibilityIssues {

    /** One known violation; {@code description} is documentation only and is not used for matching. */
    public record KnownIssue(String id, String impact, String description) {
    }

    /** Page name (as passed to the checker) to its known issues. Add known issues here as they are identified. */
    public static final Map<String, List<KnownIssue>> FOR_PAGE = Map.of(
            "Login Page", List.of(),
            "Inventory Page", List.of(
                    new KnownIssue("select-name", "critical", "Ensure select element has an accessible name")),
            "Cart Page", List.of());

    private KnownAccessibilityIssues() {
    }
}
