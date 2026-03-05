package pages;

import org.openqa.selenium.By;

public final class LoginLocators {

    private LoginLocators() {
    }

    public static final By USERNAME_INPUT = By.id("username");
    public static final By PASSWORD_INPUT = By.id("password");
    public static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    public static final By LOGIN_BUTTON_ALT = By.cssSelector("input[name='login']");
    public static final By LOGIN_FORM = By.cssSelector("form.login");

    public static final By REGISTER_EMAIL_INPUT = By.id("reg_email");
    public static final By REGISTER_PASSWORD_INPUT = By.id("reg_password");
    public static final By REGISTER_BUTTON = By.cssSelector("input[name='register'], button[name='register']");
    public static final By REGISTER_FORM = By.cssSelector("form.register");

    public static final By SUCCESS_INDICATOR = By.xpath(
        "//*[contains(text(),'Bienvenido') or contains(text(),'Dashboard') or contains(text(),'Logout')]"
    );
    public static final By LOGOUT_LINK = By.cssSelector("a[href*='customer-logout'], a[href*='logout']");

    public static final By ERROR_MESSAGE = By.cssSelector(".woocommerce-error, .error, .alert-danger, [role='alert']");
}
