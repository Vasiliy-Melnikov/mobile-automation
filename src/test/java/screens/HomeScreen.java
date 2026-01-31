package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomeScreen {

    private final AppiumDriver driver;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    public HomeScreen(AppiumDriver driver) {
        this.driver = driver;
    }

    public void skipOnboardingIfPresent() {
        Popups.closeBlockingPopupsIfPresent(driver);

        clickIfExists(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"SKIP\")"), 2);
        clickIfExists(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"NEXT\")"), 2);
        clickIfExists(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"GOT IT\")"), 2);
        clickIfExists(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"CONTINUE\")"), 2);
        clickIfExists(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"ACCEPT\")"), 2);
        clickIfExists(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"NOT NOW\")"), 2);

        Popups.closeBlockingPopupsIfPresent(driver);
    }

    public void openSearch() {
        Popups.closeBlockingPopupsIfPresent(driver);

        WebElement el = firstOrNull(driver.findElements(AppiumBy.accessibilityId("Search Wikipedia")));
        if (el != null) { el.click(); return; }

        el = firstOrNull(driver.findElements(AppiumBy.id("org.wikipedia.alpha:id/search_container")));
        if (el != null) { el.click(); return; }

        el = firstOrNull(driver.findElements(AppiumBy.id("org.wikipedia.alpha:id/search_container_view")));
        if (el != null) { el.click(); return; }

        var wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        el = wait.until(d -> {
            var list = d.findElements(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Search\")"));
            return list.isEmpty() ? null : list.get(0);
        });
        el.click();
    }

    private void clickIfExists(org.openqa.selenium.By by, int seconds) {
        try {
            var wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            var el = wait.until(d -> {
                var list = d.findElements(by);
                return list.isEmpty() ? null : list.get(0);
            });
            if (el != null) el.click();
        } catch (Exception ignored) {}
    }

    private static <T> T firstOrNull(java.util.List<T> list) {
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}

