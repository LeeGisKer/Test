package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PasosEstandar {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    public PasosEstandar(WebDriver driver, WaitUtils waitUtils) {
        this.driver = driver;
        this.waitUtils = waitUtils;
    }

    public void ingresarTexto(By locator, String texto) {
        WebElement element = waitUtils.waitUntilVisible(locator);
        element.clear();
        element.sendKeys(texto);
    }

    public void click(By locator) {
        waitUtils.waitUntilClickable(locator).click();
    }

    public boolean esVisible(By locator) {
        try {
            return waitUtils.waitUntilVisibleAndCheckDisplayed(locator);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String obtenerTexto(By locator) {
        try {
            return waitUtils.waitUntilVisible(locator).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public void enviarFormulario(By formLocator, By preferredButton, By fallbackButton) {
        try {
            click(preferredButton);
            return;
        } catch (TimeoutException | NoSuchElementException | ElementClickInterceptedException ignored) {
        }

        if (fallbackButton != null) {
            try {
                click(fallbackButton);
                return;
            } catch (TimeoutException | NoSuchElementException | ElementClickInterceptedException ignored) {
            }
        }

        WebElement form = waitUtils.waitUntilVisible(formLocator);
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);
        } catch (WebDriverException e) {
            throw new RuntimeException("No se pudo enviar el formulario por click ni submit JS.", e);
        }
    }
}
