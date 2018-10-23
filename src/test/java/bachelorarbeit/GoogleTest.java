package bachelorarbeit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoogleTest {

    @Test
    public void shouldAnswerWithTrue() {
        System.setProperty("webdriver.gecko.driver", "/home/xyntek/Java/geckodriver");
        WebDriver driver = new FirefoxDriver();
        driver.get("http://google.com");
        driver.findElement(By.id("lst-ib")).sendKeys("Goethe Uni");
        driver.findElement(By.xpath("//input[@value='Google-Suche']")).click();

        java.util.List<WebElement> elements = driver.findElements(By.className("iUh30"));

        String firstLink = elements.get(0).getText();
        assertEquals("https://www.uni-frankfurt.de/", firstLink);
        driver.quit();
    }
}