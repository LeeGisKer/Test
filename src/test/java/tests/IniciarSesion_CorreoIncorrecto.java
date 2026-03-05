package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * Universidad: <PENDIENTE_COMPLETAR>
 * Estudiante: <PENDIENTE_COMPLETAR>
 * Matricula: <PENDIENTE_COMPLETAR>
 * Asesor(a): <PENDIENTE_COMPLETAR>
 * Modulo: <PENDIENTE_COMPLETAR>
 * Reto: Reto 5. Automatizacion de una pantalla login
 * Fecha de elaboracion: <PENDIENTE_COMPLETAR>
 */
public class IniciarSesion_CorreoIncorrecto extends BaseTest {

    @BeforeTest
    @Parameters({"browser", "headless", "baseUrl", "validUser", "validPass", "invalidUser", "invalidPass"})
    public void setup(
        @Optional("firefox") String browser,
        @Optional("true") String headless,
        @Optional("https://practice.automationtesting.in/my-account/") String baseUrl,
        @Optional("") String validUser,
        @Optional("") String validPass,
        @Optional("correo_incorrecto@example.com") String invalidUser,
        @Optional("password_incorrecto") String invalidPass
    ) {
        configureFromSuite(browser, headless, baseUrl, validUser, validPass, invalidUser, invalidPass);
        initializeTest(BASE_URL_DEFAULT);
    }

    @Test(description = "Iniciar sesion con correo/credenciales incorrectas")
    public void iniciarSesionCorreoIncorrecto() {
        loginPage.login(getInvalidUser(), getInvalidPass());

        Assert.assertTrue(
            loginPage.isErrorMessageVisible(),
            "Se esperaba un mensaje de error visible con credenciales invalidas."
        );

        Assert.assertFalse(
            loginPage.getErrorMessageText().isBlank(),
            "Se esperaba texto en el mensaje de error."
        );
    }

    @AfterTest
    public void teardown() {
        cleanupTest();
    }
}
