package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;

/** Port of assertions/loginAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class LoginAssertions {

    private final LoginPage loginPage;

    public LoginAssertions(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public void verifyLoginLandingDetails() {
        loginPage.waits().until("title containing 'Swag Labs'", d -> d.getTitle().contains("Swag Labs"));
    }

    public void verifyDisplayedErrorMessage(String message) {
        assertThat(loginPage.getDisplayedErrorMessage()).contains(message);
    }

    public void verifyLogoIsVisible() {
        loginPage.waits().visible(loginPage.getLogo());
    }

    public void verifyPasswordFieldIsMasked() {
        loginPage.waits().until("password field type='password'",
                d -> "password".equals(d.findElement(loginPage.getPasswordField()).getDomAttribute("type")));
    }

    public void verifyLoginButtonIsDisabled() {
        loginPage.waits().until("login button disabled", d -> !d.findElement(loginPage.getLoginButton()).isEnabled());
    }

    public void verifyLoginButtonIsEnabled() {
        loginPage.waits().until("login button enabled", d -> d.findElement(loginPage.getLoginButton()).isEnabled());
    }
}
