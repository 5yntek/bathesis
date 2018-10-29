package tests.AFEWeb.encoding;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractEncodingTest {
    static WebDriver driver = null;


    public void login() {
        String login = "http://141.2.2.130:8080/AFE_HD/j_security_check?j_username=karsten&j_password=karsten&x=0&y=0";
        driver.navigate().to(login);
    }

    /**
     * tries to enter findspot utf-8 legal cyrillic name of Moskau
     */
    @Test
    public void encodingTest() {
        //cyrillic Moskau
        final String name = "\u041c\u043e\u0441\u043a\u0432\u0430\u0301";
        String urlEnterFindspot = "http://141.2.2.130:8080/AFE_HD/enter_findspot";
        String urlShowFindSpot = "http://141.2.2.130:8080/AFE_HD/show_findspot?id=";
        driver.navigate().to(urlEnterFindspot);
        driver.findElement(By.id("findspot_name")).sendKeys(name);
        driver.findElement(By.xpath("//input[@type='submit' and @value='Enter Findspot']")).click();
        String foundName;
        foundName = driver.findElement(By.id("findspot_name")).getAttribute("value");
        assertEquals(name, foundName);
        final String id = driver.findElement(By.id("findspot_id")).getAttribute("value");
        driver.navigate().to(urlShowFindSpot + id);
        foundName = driver.findElement(By.id("findspot_name"))
                .getAttribute("value");
        assertEquals(name, foundName);
    }

    @AfterEach
    public void tearDown() {
        Assumptions.assumeTrue(Objects.nonNull(driver));
        driver.quit();
    }
}
