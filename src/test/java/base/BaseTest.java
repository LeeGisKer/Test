package base;

import org.openqa.selenium.WebDriver;
import pages.LoginPage;

public abstract class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;

    protected static final String BASE_URL_DEFAULT = "<PON_AQUI_URL_LOGIN>";
    protected static final String VALID_USER_DEFAULT = "<VALID_USER>";
    protected static final String VALID_PASS_DEFAULT = "<VALID_PASS>";
    protected static final String INVALID_USER_DEFAULT = "<INVALID_USER>";
    protected static final String INVALID_PASS_DEFAULT = "<INVALID_PASS>";

    protected String baseUrl;
    protected String validUser;
    protected String validPass;
    protected String invalidUser;
    protected String invalidPass;

    protected void initializeTest(String fallbackUrl) {
        baseUrl = resolveConfig("base.url", fallbackUrl);
        validUser = resolveConfig("valid.user", VALID_USER_DEFAULT);
        validPass = resolveConfig("valid.pass", VALID_PASS_DEFAULT);
        invalidUser = resolveConfig("invalid.user", INVALID_USER_DEFAULT);
        invalidPass = resolveConfig("invalid.pass", INVALID_PASS_DEFAULT);

        if (baseUrl.contains("<PON_AQUI_URL_LOGIN>")) {
            throw new IllegalStateException(
                "Configura la URL real con -Dbase.url=https://tu-login o ajusta BASE_URL_DEFAULT."
            );
        }

        driver = DriverFactory.createDriver();
        driver.get(baseUrl);
        loginPage = new LoginPage(driver);
    }

    protected void cleanupTest() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected String getValidUser() {
        return validUser;
    }

    protected String getValidPass() {
        return validPass;
    }

    protected String getInvalidUser() {
        return invalidUser;
    }

    protected String getInvalidPass() {
        return invalidPass;
    }

    private String resolveConfig(String property, String defaultValue) {
        String value = System.getProperty(property);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value.trim();
    }
}
