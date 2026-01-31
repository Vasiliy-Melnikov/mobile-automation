package tests;

import drivers.MobileDriver;
import helpers.AllureAttachments;
import helpers.BrowserstackApi;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    protected AppiumDriver driver;
    @BeforeEach
    void startDriver() {
        driver = MobileDriver.createDriver();
    }

    @AfterEach
    void tearDown() {
        System.out.println(driver.getPageSource());
        String sessionId = driver.getSessionId().toString();

        AllureAttachments.screenshot(driver);
        AllureAttachments.pageSource(driver);

        driver.quit();

        new BrowserstackApi().attachVideo(sessionId);
    }
}
