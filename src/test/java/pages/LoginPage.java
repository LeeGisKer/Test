package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import utils.PasosEstandar;
import utils.WaitUtils;

public class LoginPage {

    private final WaitUtils waitUtils;
    private final PasosEstandar pasosEstandar;

    // TODO: Ajustar con los locators reales inspeccionando el DOM.
    public static final By USERNAME_INPUT = LoginLocators.USERNAME_INPUT;
    public static final By PASSWORD_INPUT = LoginLocators.PASSWORD_INPUT;
    public static final By LOGIN_BUTTON = LoginLocators.LOGIN_BUTTON;
    public static final By LOGIN_BUTTON_ALT = LoginLocators.LOGIN_BUTTON_ALT;
    public static final By LOGIN_FORM = LoginLocators.LOGIN_FORM;

    public static final By REGISTER_EMAIL_INPUT = LoginLocators.REGISTER_EMAIL_INPUT;
    public static final By REGISTER_PASSWORD_INPUT = LoginLocators.REGISTER_PASSWORD_INPUT;
    public static final By REGISTER_BUTTON = LoginLocators.REGISTER_BUTTON;
    public static final By REGISTER_FORM = LoginLocators.REGISTER_FORM;

    // TODO: Ajustar al elemento real mostrado cuando el login es exitoso.
    public static final By SUCCESS_INDICATOR = LoginLocators.SUCCESS_INDICATOR;
    public static final By LOGOUT_LINK = LoginLocators.LOGOUT_LINK;

    // TODO: Ajustar al elemento real de error cuando las credenciales son inválidas.
    public static final By ERROR_MESSAGE = LoginLocators.ERROR_MESSAGE;

    public LoginPage(WebDriver driver) {
        this.waitUtils = new WaitUtils(driver, Duration.ofSeconds(20));
        this.pasosEstandar = new PasosEstandar(driver, waitUtils);
    }

    public void login(String username, String password) {
        pasosEstandar.ingresarTexto(USERNAME_INPUT, username);
        pasosEstandar.ingresarTexto(PASSWORD_INPUT, password);
        pasosEstandar.enviarFormulario(LOGIN_FORM, LOGIN_BUTTON, LOGIN_BUTTON_ALT);
    }

    public void register(String email, String password) {
        pasosEstandar.ingresarTexto(REGISTER_EMAIL_INPUT, email);
        pasosEstandar.ingresarTexto(REGISTER_PASSWORD_INPUT, password);
        pasosEstandar.enviarFormulario(REGISTER_FORM, REGISTER_BUTTON, null);
    }

    public void logoutIfPresent() {
        try {
            pasosEstandar.click(LOGOUT_LINK);
        } catch (TimeoutException e) {
            // Si no hay logout visible, se asume que no hay sesión activa.
        }
    }

    public boolean isSuccessIndicatorVisible() {
        return isVisible(SUCCESS_INDICATOR) || isVisible(LOGOUT_LINK);
    }

    public boolean isErrorMessageVisible() {
        return isVisible(ERROR_MESSAGE);
    }

    public String getErrorMessageText() {
        return pasosEstandar.obtenerTexto(ERROR_MESSAGE);
    }

    private boolean isVisible(By locator) {
        try {
            return waitUtils.waitUntilVisibleAndCheckDisplayed(locator);
        } catch (TimeoutException e) {
            return false;
        }
    }

}
