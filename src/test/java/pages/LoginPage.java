package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class LoginPage {

    private final WaitUtils waitUtils;

    // TODO: Ajustar con los locators reales inspeccionando el DOM.
    // Preferir id o name; usar CSS/XPath solo si no existe alternativa estable.
    public static final By USERNAME_INPUT = By.id("username");
    public static final By PASSWORD_INPUT = By.id("password");
    public static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");

    // TODO: Ajustar al elemento real mostrado cuando el login es exitoso.
    public static final By SUCCESS_INDICATOR = By.xpath("//*[contains(text(),'Bienvenido') or contains(text(),'Dashboard') or contains(text(),'Logout')]");

    // TODO: Ajustar al elemento real de error cuando las credenciales son inválidas.
    public static final By ERROR_MESSAGE = By.cssSelector(".error, .alert-danger, [role='alert']");

    public LoginPage(WebDriver driver) {
        this.waitUtils = new WaitUtils(driver, Duration.ofSeconds(10));
    }

    public void login(String username, String password) {
        WebElement userInput = waitUtils.waitUntilVisible(USERNAME_INPUT);
        userInput.clear();
        userInput.sendKeys(username);

        WebElement passInput = waitUtils.waitUntilVisible(PASSWORD_INPUT);
        passInput.clear();
        passInput.sendKeys(password);

        waitUtils.waitUntilClickable(LOGIN_BUTTON).click();
    }

    public boolean isSuccessIndicatorVisible() {
        return isVisible(SUCCESS_INDICATOR);
    }

    public boolean isErrorMessageVisible() {
        return isVisible(ERROR_MESSAGE);
    }

    public String getErrorMessageText() {
        try {
            return waitUtils.waitUntilVisible(ERROR_MESSAGE).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    private boolean isVisible(By locator) {
        try {
            return waitUtils.waitUntilVisibleAndCheckDisplayed(locator);
        } catch (TimeoutException e) {
            return false;
        }
    }
}
