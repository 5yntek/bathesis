package tests.AFEWeb.security;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import tests.SeleniumConfig;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class FirefoxSecurityTest extends AbstractSecurityTest {


    @BeforeEach
    public void setUp() throws IOException {
        driver = new SeleniumConfig().getFireFoxDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assumptions.assumeTrue(Objects.nonNull(driver), "FirefoxDriver could not be created");
    }


}
