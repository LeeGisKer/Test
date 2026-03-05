package base;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = System.getProperty("browser", "firefox").trim().toLowerCase();
        boolean requestedHeadless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        boolean headless = forceHeadlessIfNeeded(requestedHeadless);

        WebDriver driver;
        try {
            driver = switch (browser) {
                case "chrome" -> createChromeDriver(headless);
                case "firefox" -> createFirefoxDriver(headless);
                default -> throw new IllegalArgumentException(
                    "Browser no soportado: " + browser + ". Usa -Dbrowser=firefox o -Dbrowser=chrome."
                );
            };
        } catch (WebDriverException e) {
            if (!"chrome".equals(browser)) {
                throw e;
            }
            System.out.println("[WARN] Chrome no pudo iniciar. Causa: " + firstLine(e.getMessage()));
            System.out.println("[WARN] Se intentara fallback automatico a Firefox.");
            driver = createFirefoxDriver(true);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
        return driver;
    }

    private static WebDriver createChromeDriver(boolean headless) {
        setupWebDriverManagerIfAvailable("chrome");
        ChromeOptions options = new ChromeOptions();
        String chromeBinary = System.getProperty("chrome.binary", "").trim();
        if (!chromeBinary.isEmpty()) {
            options.setBinary(chromeBinary);
        }
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
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
        String firefoxBinary = System.getProperty("firefox.binary", "").trim();
        if (!firefoxBinary.isEmpty()) {
            options.setBinary(firefoxBinary);
        }
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
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

    private static boolean forceHeadlessIfNeeded(boolean requestedHeadless) {
        if (requestedHeadless) {
            return true;
        }
        String display = System.getenv("DISPLAY");
        if (display == null || display.isBlank()) {
            System.out.println("[WARN] No se detecto DISPLAY. Se forza ejecucion headless.");
            return true;
        }
        return false;
    }

    private static String firstLine(String message) {
        if (message == null || message.isBlank()) {
            return "sin detalle";
        }
        int idx = message.indexOf('\n');
        return idx > 0 ? message.substring(0, idx).trim() : message.trim();
    }
}
