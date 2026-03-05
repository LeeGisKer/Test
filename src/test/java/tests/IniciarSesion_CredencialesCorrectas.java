package tests;

import base.BaseTest;
import java.time.Instant;
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
public class IniciarSesion_CredencialesCorrectas extends BaseTest {

    @BeforeTest
    @Parameters({"browser", "headless", "baseUrl", "validUser", "validPass", "invalidUser", "invalidPass", "autoRegister"})
    public void setup(
        @Optional("firefox") String browser,
        @Optional("true") String headless,
        @Optional("https://practice.automationtesting.in/my-account/") String baseUrl,
        @Optional("") String validUser,
        @Optional("") String validPass,
        @Optional("baduser") String invalidUser,
        @Optional("badpass") String invalidPass,
        @Optional("false") String autoRegister
    ) {
        configureFromSuite(browser, headless, baseUrl, validUser, validPass, invalidUser, invalidPass);
        setIfPresent("auto.register", autoRegister);
        initializeTest(BASE_URL_DEFAULT);
    }

    @Test(description = "Iniciar sesion con credenciales correctas")
    public void iniciarSesionCredencialesCorrectas() {
        boolean success = false;
        boolean autoRegister = Boolean.parseBoolean(System.getProperty("auto.register", "false"));
        String configuredUser = getValidUser();
        String configuredPass = getValidPass();

        if (!autoRegister && (
            configuredUser.isBlank() || configuredPass.isBlank()
                || configuredUser.contains("<VALID_USER>")
                || configuredPass.contains("<VALID_PASS>")
        )) {
            Assert.fail(
                "Configura validUser y validPass en el suite XML o por -Dvalid.user/-Dvalid.pass para ejecutar el caso positivo."
            );
        }

        for (int attempt = 1; attempt <= 2 && !success; attempt++) {
            String user = configuredUser;
            String pass = configuredPass;

            if (autoRegister || user.contains("<VALID_USER>") || pass.contains("<VALID_PASS>") || user.isBlank() || pass.isBlank()) {
                user = buildUniqueEmail();
                pass = "AutoQa#12345";
                loginPage.register(user, pass);

                if (loginPage.isSuccessIndicatorVisible()) {
                    success = true;
                    break;
                }
            }

            driver.get(baseUrl);
            loginPage.login(user, pass);
            success = loginPage.isSuccessIndicatorVisible();
        }

        Assert.assertTrue(
            success,
            "Se esperaba un indicador visible de inicio de sesion exitoso."
        );
    }

    @AfterTest
    public void teardown() {
        cleanupTest();
    }

    private String buildUniqueEmail() {
        return "qa.autogen." + Instant.now().toEpochMilli() + "@mailinator.com";
    }
}
