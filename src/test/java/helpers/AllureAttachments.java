package helpers;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;

public class AllureAttachments {

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] screenshot(AppiumDriver driver) {
        return driver.getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page source", type = "text/xml")
    public static String pageSource(AppiumDriver driver) {
        return driver.getPageSource();
    }
}
