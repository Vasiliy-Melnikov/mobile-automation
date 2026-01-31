package screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ArticleScreen {

    private final AppiumDriver driver;

    public ArticleScreen(AppiumDriver driver) {
        this.driver = driver;
    }

    public WebElement title() {
        Popups.closeBlockingPopupsIfPresent(driver);

        By title = By.xpath("//android.webkit.WebView//android.widget.TextView[1]");
        return new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(d -> d.findElement(title));
    }
}
