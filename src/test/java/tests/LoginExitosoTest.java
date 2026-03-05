package tests;

import base.BaseTest;
import java.time.Instant;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginExitosoTest extends BaseTest {

    @BeforeTest
    public void setup() {
        initializeTest(BASE_URL_DEFAULT);
    }

    @Test(description = "Caso 1: login válido debe mostrar indicador de éxito")
    public void loginValidoDebeMostrarIndicadorExitoso() {
        String user = getValidUser();
        String pass = getValidPass();

        boolean autoRegister = Boolean.parseBoolean(System.getProperty("auto.register", "false"));
        if (autoRegister || user.contains("<VALID_USER>") || pass.contains("<VALID_PASS>")) {
            user = buildUniqueEmail();
            pass = "AutoQa#12345";
            loginPage.register(user, pass);
            Assert.assertTrue(
                loginPage.isSuccessIndicatorVisible(),
                "Se esperaba sesión iniciada luego de registrar usuario nuevo."
            );
            loginPage.logoutIfPresent();
        }

        loginPage.login(user, pass);
        Assert.assertTrue(
            loginPage.isSuccessIndicatorVisible(),
            "Se esperaba un elemento visible de éxito tras login válido (ej. Bienvenido/Dashboard/Logout)."
        );
    }

    @AfterTest
    public void teardown() {
        cleanupTest();
    }

    private String buildUniqueEmail() {
        long suffix = Instant.now().toEpochMilli();
        return "qa.autogen." + suffix + "@mailinator.com";
    }
}
