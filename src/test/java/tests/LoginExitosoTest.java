package tests;

import base.BaseTest;
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
        loginPage.login(getValidUser(), getValidPass());
        Assert.assertTrue(
            loginPage.isSuccessIndicatorVisible(),
            "Se esperaba un elemento visible de éxito tras login válido (ej. Bienvenido/Dashboard/Logout)."
        );
    }

    @AfterTest
    public void teardown() {
        cleanupTest();
    }
}
