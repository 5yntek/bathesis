package tests.AFEWeb.findspot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public abstract class AbstractFindSpotTest {
    static WebDriver driver = null;
    private String urlEnterFindspot = "http://141.2.2.130:8080/AFE_HD/enter_findspot";
    private String urlFindspotEntered = "http://141.2.2.130:8080/AFE_HD/findspot_entered";
    private String urlAllFindspot = "http://141.2.2.130:8080/AFE_HD/fs_all";
    private String login = "http://141.2.2.130:8080/AFE_HD/j_security_check?j_username=karsten&j_password=karsten&x=0&y=0";

    public void login() {
        driver.navigate().to(login);
    }

    @AfterEach
    public void tearDown() {
        Assumptions.assumeTrue(Objects.nonNull(driver));
        driver.quit();
    }

    /**
     * Ein einfacher Test, welcher überprüft ob Werte in das Feld für den Namen des Findspot eingetragen werden können.
     */
    @Test
    public void enterNameField() {
        driver.navigate().to(urlEnterFindspot);
        WebElement name = driver.findElement(By.id("findspot_name"));
        name.sendKeys("test");
        assertEquals("test", name.getAttribute("value"));
    }


    @Test
    public void enterAltNameField() {
        driver.navigate().to(urlEnterFindspot);
        WebElement altNames = driver.findElement(By.id("findspot_alt_names"));
        altNames.sendKeys("test");
        assertEquals("test", altNames.getAttribute("value"));
    }


    @Test
    public void chooseFindCategory() {
        driver.navigate().to(urlEnterFindspot);
        WebElement findCategory = driver.findElement(By.name("findspotfindcategory"));
        Select select = new Select(findCategory);
        select.selectByVisibleText("Frankfurt");
        assertEquals("Frankfurt", select.getFirstSelectedOption().getText());
    }

    @Test
    public void chooseDiscoveryType() {
        driver.navigate().to(urlEnterFindspot);
        WebElement discoverytype = driver.findElement(By.name("discoverytype"));
        Select select = new Select(discoverytype);
        select.selectByIndex(1);
        assertEquals("big discovery(Großartiger Fund)", select.getFirstSelectedOption().getText());
    }


    @Test
    public void chooseDepositionType() {
        driver.navigate().to(urlEnterFindspot);
        WebElement depositiontype = driver.findElement(By.name("depositiontype"));
        Select select = new Select(depositiontype);
        select.selectByIndex(1);
        assertEquals("test english(test)", select.getFirstSelectedOption().getText());
    }

    @Test
    public void enterDescription() {
        driver.navigate().to(urlEnterFindspot);
        WebElement name = driver.findElement(By.id("findspot_description"));
        name.sendKeys("test");
        assertEquals("test", name.getAttribute("value"));
    }


    @Test
    public void enterLatitude() {
        driver.navigate().to(urlEnterFindspot);
        WebElement latitude = driver.findElement(By.id("findspot_latitude"));
        latitude.sendKeys("123.456");
        assertEquals("123.456", latitude.getAttribute("value"));
    }

    @Test
    public void enterLatitudeFail() {
        driver.navigate().to(urlEnterFindspot);
        WebElement latitude = driver.findElement(By.id("findspot_latitude"));
        latitude.sendKeys("Fail");
        latitude.sendKeys(Keys.TAB); //to lose focus on latitude
        new WebDriverWait(driver, 2).until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertTrue(alert.getText().contains("You used the following character 'F'"));
        alert.accept();
        //check if no alert is thrown
        Assertions.assertThrows(org.openqa.selenium.TimeoutException.class, () -> {
            new WebDriverWait(driver, 2).until(ExpectedConditions.alertIsPresent());
        });
        assertTrue(latitude.getAttribute("value").isEmpty());
        assertTrue(latitude.getAttribute("style").contains("background-color"));
    }

    @Test
    public void checkPrecise() {
        driver.navigate().to(urlEnterFindspot);
        WebElement precise = driver.findElement(By.name("georef_precise"));
        assertTrue(precise.isSelected()); //should be selected at beginning.
        precise.click();
        assertFalse(precise.isSelected());
    }

    @Test
    public void enterLongitude() {
        driver.navigate().to(urlEnterFindspot);
        WebElement longitude = driver.findElement(By.id("findspot_longitude"));
        longitude.sendKeys("123.456");
        assertEquals("123.456", longitude.getAttribute("value"));
    }

    @Test
    public void enterLongitudeFail() {
        driver.navigate().to(urlEnterFindspot);
        WebElement longitude = driver.findElement(By.id("findspot_longitude"));
        longitude.sendKeys("Fail");
        longitude.sendKeys(Keys.TAB); //to lose focus o// n latitude
        new WebDriverWait(driver, 2).until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertTrue(alert.getText().contains("You used the following character 'F'"));
        alert.accept();
        //check if no alert is thrown
        Assertions.assertThrows(TimeoutException.class, () -> {
            new WebDriverWait(driver, 2).until(ExpectedConditions.alertIsPresent());
        });
    }


    @Test
    public void enterFilter() {
        driver.navigate().to(urlEnterFindspot);
        WebElement filter = driver.findElement(By.id("autocomplete_adminDivisionFilter"));
        filter.sendKeys("a");
        assertEquals("a", filter.getAttribute("value"));
        WebElement latitudeParent = filter.findElements(By.xpath("..")).get(0);
        new WebDriverWait(driver, 2).until(webDriver ->
                !latitudeParent.findElement(By.id("autocomplete_adminDivisionFilter_choices"))
                        .getText()
                        .isEmpty()
        );
        WebElement choices = driver.findElement(By.id("autocomplete_adminDivisionFilter_choices"));
        assertTrue(choices.getText().contains("bitte mindestens 3 Zeichen eingeben ..."));
        filter.sendKeys("bc");
        String error = choices.findElement(By.tagName("h1")).getText();
        assertTrue(error.contains("HTTP Status 500 - Internal Server Error"));
    }


    @Test
    public void enterFilterClickX() {
        driver.navigate().to(urlEnterFindspot);
        WebElement latitude = driver.findElement(By.id("autocomplete_adminDivisionFilter"));
        latitude.sendKeys("a");
        assertEquals("a", latitude.getAttribute("value"));
        driver.findElement(By.id("delete_adminDivisionFilter")).click();
        assertTrue(latitude.getAttribute("value").isEmpty());
    }


    @Test
    public void enterAdminDivision() {
        driver.navigate().to(urlEnterFindspot);
        WebElement adminDivision = driver.findElement(By.id("autocomplete_adminDivision"));
        adminDivision.sendKeys("a");
        assertEquals("a", adminDivision.getAttribute("value"));
        WebElement adminDivisionParent = adminDivision.findElements(By.xpath("..")).get(0);
        new WebDriverWait(driver, 2).until(webDriver ->
                !adminDivisionParent.findElement(By.id("autocomplete_adminDivision_choices"))
                        .getText()
                        .isEmpty()
        );
        WebElement choices = driver.findElement(By.id("autocomplete_adminDivision_choices"));
        assertTrue(choices.getText().contains("bitte mindestens 3 Zeichen eingeben ..."));
        adminDivision.sendKeys("bc");
    }

    @Test
    public void enterAdminDivisionSpinner() {
        driver.navigate().to(urlEnterFindspot);
        WebElement adminDivision = driver.findElement(By.id("autocomplete_adminDivision"));
        adminDivision.sendKeys("abc");
        //check spinning loading img
        WebElement spinner = adminDivision.findElements(By.xpath("..")).get(0).findElement(By.id("indicator3"));
        //spinner becomes visible after a short time
        new WebDriverWait(driver, 2).until(webDriver -> isVisible(spinner));
        //spins a few times then disappears again
        new WebDriverWait(driver, 2).until(webDriver -> !isVisible(spinner));
    }

    private boolean isVisible(WebElement webElement) {
        return !webElement.getAttribute("style").contains("display: none");
    }


    @Test
    public void enterPlace() {
        driver.navigate().to(urlEnterFindspot);
        WebElement place = driver.findElement(By.id("autocomplete_place"));
        place.sendKeys("a");
        assertEquals("a", place.getAttribute("value"));
        WebElement placeParent = place.findElements(By.xpath("..")).get(0);
        new WebDriverWait(driver, 2).until(webDriver ->
                isVisible(driver.findElement(By.id("autocomplete_place_choices"))));
        WebElement choices = driver.findElement(By.id("autocomplete_place_choices"));
        assertTrue(choices.getText().contains("bitte mindestens 3 Zeichen eingeben ..."));

        WebElement spinner = placeParent.findElement(By.id("indicator4"));
        place.sendKeys("bc");
        new WebDriverWait(driver, 2).until(webDriver -> isVisible(spinner));
        //spins a few times then disappears again
        new WebDriverWait(driver, 2).until(webDriver -> !isVisible(spinner));
    }

    @Test
    public void enterLink() {
        driver.navigate().to(urlEnterFindspot);
        WebElement link = driver.findElement(By.id("link"));
        link.sendKeys("abcd");
        assertEquals(link.getAttribute("value"), "abcd");
    }

    @Test
    public void enterBibliographySingle() {
        driver.navigate().to(urlEnterFindspot);
        WebElement bibliographyId0 = driver.findElement(By.id("autocomplete_bibliography_id_0"));
        bibliographyId0.sendKeys("abcd");
        assertEquals(bibliographyId0.getAttribute("value"), "abcd");
        WebElement firstListElement = bibliographyId0.findElement(By.xpath(".."));
        //enter pageText
        WebElement pageText = firstListElement.findElement(By.name("pageText"));
        pageText.sendKeys("bcde");
        assertEquals(pageText.getAttribute("value"), "bcde");
        //click x
        firstListElement.findElement(By.id("autocomplete_deleteButton_id_0")).click();
        assertTrue(bibliographyId0.getAttribute("value").isEmpty());
        assertEquals(pageText.getAttribute("value"), "bcde");
    }


    @Test
    public void enterBibliographyDouble() {
        driver.navigate().to(urlEnterFindspot);

        //no second bibliography yet.
        assertThrows(org.openqa.selenium.NoSuchElementException.class,
                () -> driver.findElement(By.id("autocomplete_bibliography_id_1")));


        WebElement bibliographyId0 = driver.findElement(By.id("autocomplete_bibliography_id_0"));
        bibliographyId0.sendKeys("abcd");

        WebElement bibliographyId1 = driver.findElement(By.id("autocomplete_bibliography_id_1"));
        bibliographyId1.sendKeys("cdef");
        assertEquals("cdef", bibliographyId1.getAttribute("value"));
        driver.findElement(By.id("autocomplete_deleteButton_id_1")).click();

        //second bibliography deleted.
        assertThrows(org.openqa.selenium.NoSuchElementException.class,
                () -> driver.findElement(By.id("autocomplete_bibliography_id_1")));
    }

    @Test
    public void enterBibliographyTriple() {
        driver.navigate().to(urlEnterFindspot);

        //no second bibliography yet.
        assertThrows(org.openqa.selenium.NoSuchElementException.class,
                () -> driver.findElement(By.id("autocomplete_bibliography_id_1")));
        assertThrows(org.openqa.selenium.NoSuchElementException.class,
                () -> driver.findElement(By.id("autocomplete_bibliography_id_2")));


        WebElement bibliographyId0 = driver.findElement(By.id("autocomplete_bibliography_id_0"));
        bibliographyId0.sendKeys("abcd");

        assertThrows(org.openqa.selenium.NoSuchElementException.class,
                () -> driver.findElement(By.id("autocomplete_bibliography_id_2")));

        WebElement bibliographyId1 = driver.findElement(By.id("autocomplete_bibliography_id_1"));
        bibliographyId1.sendKeys("cdef");

        WebElement bibliographyId2 = driver.findElement(By.id("autocomplete_deleteButton_id_2"));


        driver.findElement(By.id("autocomplete_deleteButton_id_1")).click();
        //second bibliography deleted.
        assertThrows(org.openqa.selenium.NoSuchElementException.class,
                () -> driver.findElement(By.id("autocomplete_bibliography_id_1")));

        //should still be there
        bibliographyId2 = driver.findElement(By.id("autocomplete_deleteButton_id_2"));

        driver.findElement(By.id("autocomplete_deleteButton_id_2")).click();
        //third (now second) bibliography deleted.
        assertThrows(org.openqa.selenium.NoSuchElementException.class,
                () -> driver.findElement(By.id("autocomplete_bibliography_id_2")));
    }

    @Test
    public void enterBibliographyAutocomplete() {
        driver.navigate().to(urlEnterFindspot);

        WebElement autocompleteBibliographyChoicesId0 = driver.findElement(By.id("autocomplete_bibliography_choices_id_0"));
        assertFalse(isVisible(autocompleteBibliographyChoicesId0));
        WebElement bibliographyId0 = driver.findElement(By.id("autocomplete_bibliography_id_0"));
        bibliographyId0.sendKeys("wigg");
        new WebDriverWait(driver, 2).until(webDriver ->
                isVisible(autocompleteBibliographyChoicesId0));
        List<WebElement> choices = autocompleteBibliographyChoicesId0.findElements(By.tagName("li"));
        assertEquals(2, choices.size());
        assertTrue(choices.get(0).getText().contains("Das Ende der keltischen Münzgeldwirtschaft am Mittelrhein."));
        assertTrue(choices.get(1).getText().contains("Ein römerzeitlicher Münzschatzfund aus dem Lahntal bei Wetzlar."));
        choices.get(0).click();

        bibliographyId0 = driver.findElement(By.id("autocomplete_bibliography_id_0"));
        assertTrue(bibliographyId0.getAttribute("value").contains("Das Ende der keltischen Münzgeldwirtschaft am Mittelrhein."));
    }

    @Test
    public void createFindspot() {

        final String name = "testFindspot";
        final String altname = "altname";
        final String description = "this is a test";
        final String latidtude = "50.116784";
        final String longitude = "8.649813";
        final String division = "Frankfurt am Main";
        final String link = "link";
        final String bib = "Wigg";
        final String findspotfindcategory = "Frankfurt";

        // currently count of spots with name "testFindspot";
        final int countSpots = this.countFindSpot(name);


        driver.navigate().to(urlEnterFindspot);
        driver.findElement(By.id("findspot_name")).sendKeys(name);

        driver.findElement(By.id("findspot_alt_names")).sendKeys(altname);
        driver.findElement(By.name("findspotfindcategory")).sendKeys(findspotfindcategory);
        driver.findElement(By.name("discoverytype"));
        driver.findElement(By.name("depositiontype"));

        driver.findElement(By.id("findspot_description")).sendKeys(description);

        driver.findElement(By.id("findspot_latitude")).sendKeys(latidtude);

        driver.findElement(By.id("findspot_longitude")).sendKeys(longitude);
        driver.findElement(By.id("autocomplete_adminDivisionFilter")); //no filter

        driver.findElement(By.id("autocomplete_adminDivision"))
                .sendKeys(division);
        new WebDriverWait(driver, 5).until(webDriver ->
                isVisible(driver.findElement(By.id("autocomplete_adminDivision_choices")))
        );
        driver.findElement(By.id("DE_06_412_000")).click();

        driver.findElement(By.id("link")).sendKeys(link);

        driver.findElement(By.id("autocomplete_bibliography_id_0"))
                .sendKeys(bib);
        new WebDriverWait(driver, 2).until(webDriver ->
                isVisible(driver.findElement(By.id("autocomplete_bibliography_choices_id_0"))));
        driver.findElement(By.id("2")).click();
        driver.findElement(By.xpath("//input[@type='submit' and @value='Enter Findspot']")).click();


        assertEquals(this.urlFindspotEntered,
                driver.getCurrentUrl());
        //tests
        assertEquals(
                name,
                driver.findElement(By.id("findspot_name")).getAttribute("value"));
        assertEquals(
                altname,
                driver.findElement(By.id("findspot_alt_names")).getAttribute("value"));


        assertEquals(findspotfindcategory,
                new Select(driver.findElement(By.name("findspotfindcategory"))).getFirstSelectedOption().getText());

        assertEquals(description,
                driver.findElement(By.id("findspot_description")).getAttribute("value"));

        assertEquals(latidtude,
                driver.findElement(By.id("findspot_latitude")).getAttribute("value"));


        assertEquals(longitude,
                driver.findElement(By.id("findspot_longitude")).getAttribute("value"));

        assertTrue(driver.findElement(By.id("autocomplete_adminDivision"))
                .getAttribute("value")
                .contains(division));

        assertEquals(link,
                driver.findElement(By.id("link")).getAttribute("value"));

        assertTrue(driver.findElement(By.id("autocomplete_bibliography_id_0"))
                .getAttribute("value")
                .contains(bib));

        //Count of Spot with name "testSpot" should be increased by 1
        assertEquals(countSpots + 1,
                countFindSpot(name));
    }

    public int countFindSpot(String name) {
        driver.navigate().to(urlAllFindspot);
        return driver.findElement(By.tagName("tbody"))
                .findElements(By.xpath("//tr/td[2 and text()='" + name + "']"))
                .size();

    }
}