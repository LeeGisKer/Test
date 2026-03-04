package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginFallidoTest extends BaseTest {

    @BeforeTest
    public void setup() {
        initializeTest(BASE_URL_DEFAULT);
    }

    @Test(description = "Caso 2: login inválido debe mostrar mensaje de error")
    public void loginInvalidoDebeMostrarError() {
        loginPage.login(getInvalidUser(), getInvalidPass());

        Assert.assertTrue(
            loginPage.isErrorMessageVisible(),
            "Se esperaba un mensaje de error visible tras login inválido."
        );

        Assert.assertFalse(
            loginPage.getErrorMessageText().isBlank(),
            "El mensaje de error no debería estar vacío."
        );
    }

    @AfterTest
    public void teardown() {
        cleanupTest();
    }
}
