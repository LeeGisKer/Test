package base;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;

public abstract class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;
    protected Ambiente ambiente;

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

        ambiente = new Ambiente();
        driver = ambiente.abrirNavegador();
        try {
            driver.get(baseUrl);
        } catch (TimeoutException e) {
            // En sitios externos, algunos recursos pueden demorar y bloquear el evento "load".
            ((JavascriptExecutor) driver).executeScript("window.stop();");
        }
        dumpPageIfEnabled();
        loginPage = new LoginPage(driver);
    }

    protected void cleanupTest() {
        if (ambiente != null) {
            ambiente.cerrarNavegador(driver);
            driver = null;
            ambiente = null;
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

    protected void configureFromSuite(
        String browser,
        String headless,
        String suiteBaseUrl,
        String suiteValidUser,
        String suiteValidPass,
        String suiteInvalidUser,
        String suiteInvalidPass
    ) {
        setIfPresent("browser", browser);
        setIfPresent("headless", headless);
        setIfPresent("base.url", suiteBaseUrl);
        setIfPresent("valid.user", suiteValidUser);
        setIfPresent("valid.pass", suiteValidPass);
        setIfPresent("invalid.user", suiteInvalidUser);
        setIfPresent("invalid.pass", suiteInvalidPass);
    }

    protected void setIfPresent(String property, String value) {
        String existingValue = System.getProperty(property);
        if ((existingValue == null || existingValue.isBlank()) && value != null && !value.isBlank()) {
            System.setProperty(property, value.trim());
        }
    }

    private void dumpPageIfEnabled() {
        if (!Boolean.parseBoolean(System.getProperty("debug.dumpPage", "false"))) {
            return;
        }
        try {
            Path dir = Path.of("target", "debug-pages");
            Files.createDirectories(dir);
            String fileName = "page-" + Instant.now().toEpochMilli() + ".html";
            Path output = dir.resolve(fileName);
            Files.writeString(output, driver.getPageSource(), StandardCharsets.UTF_8);
            System.out.println("[DEBUG] Page dump: " + output.toAbsolutePath());
            System.out.println("[DEBUG] Current URL: " + driver.getCurrentUrl());
            System.out.println("[DEBUG] Title: " + driver.getTitle());
        } catch (IOException e) {
            System.out.println("[WARN] No se pudo guardar page dump: " + e.getMessage());
        }
    }
}
