package tests;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SeleniumConfig {

    private static List<String> firefoxDrivers = List.of("E:\\Java\\geckodriver.exe",
            "/home/xyntek/Java/geckodriver");
    private static List<String> chromeDrivers = List.of("E:\\Java\\chromedriver.exe",
            "/home/xyntek/Java/chromedriver");


    private Optional<Path> findFirstDriver(Collection<String> possibleDriver) {
        return possibleDriver.stream()
                .map(Paths::get)
                .filter(Files::isReadable)
                .findFirst();
    }

    private Optional<Path> findChromeDriver() {
        return findFirstDriver(chromeDrivers);
    }

    private Optional<Path> findFirefoxDriver() {
        return findFirstDriver(firefoxDrivers);
    }

    private WebDriver loadFirefoxDriver(Path location) {
        Optional<Path> d = findFirefoxDriver();
        System.setProperty("webdriver.gecko.driver", d.orElseThrow().toString());
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
        Capabilities capabilities = DesiredCapabilities.firefox();
        return new FirefoxDriver(capabilities);
    }

    private WebDriver loadChromeDriver(Path location) {
        Optional<Path> d = findChromeDriver();
        System.setProperty("webdriver.chrome.driver", d.orElseThrow().toString());
        Capabilities capabilities = DesiredCapabilities.chrome();
        return new ChromeDriver(capabilities);
    }

    public WebDriver getChromeDriver() throws IOException {
        return loadChromeDriver(findChromeDriver().orElseThrow(() -> new IOException("no Chrome Driver found")));
    }

    public WebDriver getFireFoxDriver() throws IOException {
        return loadFirefoxDriver(findFirefoxDriver().orElseThrow(() -> new IOException("no Chrome Driver found")));
    }
}
