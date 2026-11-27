package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.FooterPage;
import org.openqa.selenium.By;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/** Port of assertions/footerAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class FooterAssertions {

    private final FooterPage footerPage;

    public FooterAssertions(FooterPage footerPage) {
        this.footerPage = footerPage;
    }

    /** TS expects 'twitter.com'; the site now links to x.com (Twitter's rebrand), so either domain passes. */
    public void verifyTwitterLinkIsValid() {
        footerPage.waits().visible(footerPage.getTwitterLink());
        assertThat(footerPage.getTwitterLinkHref()).containsAnyOf("twitter.com", "x.com");
    }

    public void verifyFacebookLinkIsValid() {
        verifyLinkIsValid(footerPage.getFacebookLink(), footerPage::getFacebookLinkHref, "facebook.com");
    }

    public void verifyLinkedInLinkIsValid() {
        verifyLinkIsValid(footerPage.getLinkedInLink(), footerPage::getLinkedInLinkHref, "linkedin.com");
    }

    public void verifyCopyrightTextIsDisplayed() {
        footerPage.waits().visible(footerPage.getCopyrightText());
        String text = footerPage.getCopyrightTextContent();
        assertThat(text).contains("©");
        assertThat(text).contains("Sauce Labs");
    }

    /** TS: expect(link).toBeVisible() (retried), then a one-shot expect(href).toContain(domain). */
    private void verifyLinkIsValid(By link, Supplier<String> href, String domain) {
        footerPage.waits().visible(link);
        assertThat(href.get()).contains(domain);
    }
}
