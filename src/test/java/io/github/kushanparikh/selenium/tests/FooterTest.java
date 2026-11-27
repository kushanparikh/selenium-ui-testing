package io.github.kushanparikh.selenium.tests;

import io.github.kushanparikh.selenium.support.BaseTest;
import io.github.kushanparikh.selenium.support.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Port of tests/footer.spec.ts. */
@DisplayName("Footer Tests")
class FooterTest extends BaseTest {

    @BeforeEach
    void logIn() {
        open();
        loginPage.login(Users.STANDARD, Users.PASSWORD);
    }

    @Test
    @DisplayName("should verify Twitter link is present and valid")
    void twitterLinkIsValid() {
        footerAssertions.verifyTwitterLinkIsValid();

        attachScreenshot("footer-twitter-link");
    }

    @Test
    @DisplayName("should verify Facebook link is present and valid")
    void facebookLinkIsValid() {
        footerAssertions.verifyFacebookLinkIsValid();

        attachScreenshot("footer-facebook-link");
    }

    @Test
    @DisplayName("should verify LinkedIn link is present and valid")
    void linkedInLinkIsValid() {
        footerAssertions.verifyLinkedInLinkIsValid();

        attachScreenshot("footer-linkedin-link");
    }

    @Test
    @DisplayName("should verify copyright text is displayed correctly")
    void copyrightTextIsDisplayed() {
        footerAssertions.verifyCopyrightTextIsDisplayed();

        attachScreenshot("footer-copyright-text");
    }
}
