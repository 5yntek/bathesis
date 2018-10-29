package tests.AFEWeb.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractSecurityTest {
    //http://141.2.2.130:8080/AFE_HD/show_coin_admin?coin_id=10%27--
    //http://141.2.2.130:8080/AFE_HD/show_coin_admin?coin_id=10;%20DROP%20TABLE%20members%20/*
    WebDriver driver = null;
    private String urlEnterFindspot = "http://141.2.2.130:8080/AFE_HD/enter_findspot";
    private String urlFindspotEntered = "http://141.2.2.130:8080/AFE_HD/findspot_entered";
    private String urlAllFindspot = "http://141.2.2.130:8080/AFE_HD/fs_all";
    private String login = "http://141.2.2.130:8080/AFE_HD/j_security_check?j_username=karsten&j_password=karsten&x=0&y=0";
    private String loginInject = "http://141.2.2.130:8080/AFE_HD/j_security_check?j_username=karsten'--";
    //admin'‐‐

    @AfterEach
    public void tearDown() {
        Assumptions.assumeTrue(Objects.nonNull(driver));
        driver.quit();
    }

    private boolean isLogin(int waitInSeconds) {
        try {
            new WebDriverWait(driver, waitInSeconds).until(webDriver ->
                    driver.findElement(By.xpath("//a[@href='./logout']")));
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }

    private String loginUrl(String name, String password) {
        String baseUrl = "http://141.2.2.130:8080/AFE_HD/j_security_check?";
        return baseUrl + "j_username=" + name + "&j_password=" + password;
    }


    @Test
    public void loginManuallyTest() {
        driver.navigate().to("http://141.2.2.130:8080/AFE_HD/login");
        driver.findElement(By.name("j_username")).sendKeys("karsten");
        driver.findElement(By.name("j_password")).sendKeys("karsten");
        WebElement loginBox = driver.findElement(By.id("login-box"));
        loginBox.findElement(By.xpath("//input[@src='./images/login-btn.png']")).click();
        assertTrue(isLogin(5));
    }

    @Test
    public void loginTest() {
        driver.navigate().to(loginUrl("karsten", "karsten"));
        assertTrue(isLogin(2));
    }

    @Test
    public void loginInjection() {
        //tries to bypass login with comment
        driver.navigate().to(loginUrl("karsten'--", ""));
        assertFalse(isLogin(2));
    }

    @Test
    public void loginInjections2() {
        driver.navigate().to(loginUrl("karsten'/*", ""));
        assertFalse(isLogin(2));
    }

    @Test
    public void loginInjections3() {
        driver.navigate().to(loginUrl("karsten", "' or '1'='1"));
        assertFalse(isLogin(2));
    }

    @Test
    public void loginInjections4() {
        driver.navigate().to(loginUrl("karsten", "' or 1='1"));
        assertFalse(isLogin(2));
    }

    /**
     * Tests injection for SQL-Statement like
     * SELECT * FROM Users WHERE Username='$username' AND Password='$password'
     * A successful injection would login to the site and you should be able to logout.
     */
    @Test
    public void loginInjectionsBypassPassword() {
        String baseUrl = "http://141.2.2.130:8080/AFE_HD/j_security_check?";
        driver.navigate().to(baseUrl + "j_username=" + "karsten" + "&j_password=" + "' or '1'='1");
        boolean result;
        try {
            new WebDriverWait(driver, 2).until(webDriver ->
                    driver.findElement(By.xpath("//a[@href='./logout']")));
            result = true;
        } catch (TimeoutException ex) {
            result = false;
        }
        assertFalse(result);
    }


    @Test
    public void loginInjections5() {
        driver.navigate().to(loginUrl("' or '1'='1", "' or '1'='1"));
        assertFalse(isLogin(2));
    }

    @Test
    public void loginInjections6() {
        driver.navigate().to(loginUrl("' or ' 1=1", "' or ' 1=1"));
        assertFalse(isLogin(2));
    }


    @Test
    public void loginInjections7() {
        driver.navigate().to(loginUrl("1' or 1=1 --", ""));
        assertFalse(isLogin(2));
    }

    @Test
    void showCoinInjection() {
        String base = "http://141.2.2.130:8080/AFE_HD/show_coin?coin_id=10";
        driver.navigate().to(base + "someString");
        List<WebElement> error = driver.findElements(By.xpath("//h1[text()='HTTP Status 500 - Internal Server Error']"));
        assertFalse(error.isEmpty());
    }

    @Test
    void showPlaceInjection() {
        String base = "http://141.2.2.130:8080/AFE_HD/show_place?id=1";
        driver.navigate().to(loginUrl("karsten", "karsten"));
        driver.navigate().to(base + "someString");
        String errorMsg = driver.findElement(By.id("message")).getText();
        assertTrue(errorMsg.contains("someString"));
    }

    @Test
    void showFindSpotInjection() {
        String base = "http://141.2.2.130:8080/AFE_HD/show_findspot?id=2";
        driver.navigate().to(loginUrl("karsten", "karsten"));
        driver.navigate().to(base + "someString");
        String errorMsg = driver.findElement(By.id("message")).getText();
        assertTrue(errorMsg.contains("someString"));
    }


    @Test
    void showBibliographyInjection() {
        String base = "http://141.2.2.130:8080/AFE_HD/show_bibliography?id=1";
        driver.navigate().to(loginUrl("karsten", "karsten"));
        driver.navigate().to(base + "someString");
        List<WebElement> error = driver.findElements(By.xpath("//h1[text()='HTTP Status 500 - Internal Server Error']"));
        assertFalse(error.isEmpty());
    }

    @Test
    void showContextInjection() {
        String base = "http://141.2.2.130:8080/AFE_HD/show_context?id=1";
        driver.navigate().to(loginUrl("karsten", "karsten"));
        driver.navigate().to(base + "someString");
        List<WebElement> error = driver.findElements(By.xpath("//h1[text()='HTTP Status 500 - Internal Server Error']"));
        assertFalse(error.isEmpty());
    }
}
