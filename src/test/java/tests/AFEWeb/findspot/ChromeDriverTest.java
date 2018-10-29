package tests.AFEWeb.findspot;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import tests.SeleniumConfig;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ChromeDriverTest extends AbstractFindSpotTest {

    @BeforeEach
    public void setUp() throws IOException {
        driver = new SeleniumConfig().getChromeDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assumptions.assumeTrue(Objects.nonNull(driver), "ChromeDriver could not be created");
        super.login();
    }
}