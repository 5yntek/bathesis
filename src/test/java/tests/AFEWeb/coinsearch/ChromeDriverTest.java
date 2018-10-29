package tests.AFEWeb.coinsearch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import tests.SeleniumConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ChromeDriverTest extends AbstractGeneralTests {

    @BeforeEach
    public void setUp() throws IOException {
        driver = new SeleniumConfig().getChromeDriver();
        driver.navigate().to("http://pecunia2.zaw.uni-heidelberg.de/AFE_HD/coin_search_detailed");
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        //Thread.sleep(2000);
        driver.quit();
    }

    @Test
    public void dropDownMenu() {
        WebElement function = driver.findElement(By.name("function"));
        Select select = new Select(function);
        //List<String> list = select.getOptions().stream().map(WebElement::getText).collect(Collectors.toList());
        select.selectByVisibleText("Casting mould (Gußform)");
        assertEquals("7",
                select.getFirstSelectedOption()
                        .getAttribute("value"),
                "First Select");
        select.selectByVisibleText("");
        assertNotEquals("Casting mould (Gußform)",
                select.getFirstSelectedOption()
                        .getAttribute("value"),
                "clear select");
    }

    @Test
    public void dropDownMenuAutocomplete() throws InterruptedException {
        WebElement function = driver.findElement(By.name("function"));
        Select select = new Select(function);
        function.click();
        function.sendKeys("c");
        function.sendKeys(Keys.RETURN);
        assertEquals("7",
                select.getFirstSelectedOption()
                        .getAttribute("value"),
                "First Select");
        function.sendKeys("c");
        function.sendKeys(Keys.RETURN);
        assertEquals("13",
                select.getFirstSelectedOption()
                        .getAttribute("value"),
                "Second Select");

    }
}