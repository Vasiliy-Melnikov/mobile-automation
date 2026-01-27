package tests;

import com.codeborne.selenide.Configuration;
import drivers.MobileDriver;
import helpers.AllureAttachments;
import helpers.BrowserstackApi;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    protected AppiumDriver driver;

    @BeforeAll
    static void setup() {
        Configuration.timeout = 30000;
        Configuration.browserSize = null;
    }

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
