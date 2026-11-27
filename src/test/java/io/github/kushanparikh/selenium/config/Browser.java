package io.github.kushanparikh.selenium.config;

import java.util.Locale;

/** Browsers the suite runs on. Playwright's chromium/firefox/webkit map to Chrome/Firefox/Safari. */
public enum Browser {
    CHROME, FIREFOX, SAFARI;

    /** Lower-case id used on the command line (-Dbrowser=chrome) and in report/baseline paths. */
    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static Browser from(String id) {
        for (Browser b : values()) {
            if (b.id().equalsIgnoreCase(id == null ? "" : id.trim())) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unknown browser '" + id + "'. Use chrome, firefox or safari.");
    }
}
