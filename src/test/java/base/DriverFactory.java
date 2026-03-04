package base;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = System.getProperty("browser", "firefox").trim().toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        WebDriver driver = switch (browser) {
            case "chrome" -> createChromeDriver(headless);
            case "firefox" -> createFirefoxDriver(headless);
            default -> throw new IllegalArgumentException(
                "Browser no soportado: " + browser + ". Usa -Dbrowser=firefox o -Dbrowser=chrome."
            );
        };
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }

    private static WebDriver createChromeDriver(boolean headless) {
        setupWebDriverManagerIfAvailable("chrome");
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        setupWebDriverManagerIfAvailable("firefox");
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static void setupWebDriverManagerIfAvailable(String browser) {
        try {
            Class<?> webDriverManagerClass = Class.forName("io.github.bonigarcia.wdm.WebDriverManager");
            String managerMethod = "firefox".equals(browser) ? "firefoxdriver" : "chromedriver";
            Object manager = webDriverManagerClass
                .getMethod(managerMethod)
                .invoke(null);
            webDriverManagerClass
                .getMethod("setup")
                .invoke(manager);
        } catch (Exception e) {
            System.out.println("[INFO] WebDriverManager no disponible; usando Selenium Manager por defecto.");
            System.out.println("[INFO] Si falla la descarga automática del driver, revisa conectividad o agrega/configura WebDriverManager.");
        }
    }
}
