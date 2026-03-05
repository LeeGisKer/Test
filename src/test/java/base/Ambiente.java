package base;

import org.openqa.selenium.WebDriver;

public class Ambiente {

    public WebDriver abrirNavegador() {
        return DriverFactory.createDriver();
    }

    public void cerrarNavegador(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}
