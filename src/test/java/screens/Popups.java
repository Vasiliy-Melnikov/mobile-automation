package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Popups {

    private static final Duration SHORT = Duration.ofSeconds(6);

    private Popups() {}

    public static void closeBlockingPopupsIfPresent(AppiumDriver driver) {
        closeYearInReviewIfPresent(driver);
        closeWikipediaGamesIfPresent(driver);
    }

    private static void closeYearInReviewIfPresent(AppiumDriver driver) {
        By closeParent = AppiumBy.xpath("//*[@content-desc='Close']/..");
        WebElement container = firstOrNull(driver.findElements(closeParent));
        if (container == null) return;

        try { container.click(); } catch (Exception ignored) {}

        if (!driver.findElements(AppiumBy.accessibilityId("Close")).isEmpty()) {
            tapCenter(driver, container);
        }

        try {
            new WebDriverWait(driver, SHORT)
                    .until(d -> d.findElements(AppiumBy.accessibilityId("Close")).isEmpty());
        } catch (Exception ignored) {}
    }

    private static void closeWikipediaGamesIfPresent(AppiumDriver driver) {
        By closeButton = AppiumBy.id("org.wikipedia.alpha:id/closeButton");
        WebElement btn = firstOrNull(driver.findElements(closeButton));
        if (btn == null) return;

        try { btn.click(); } catch (Exception ignored) { tapCenter(driver, btn); }

        try {
            new WebDriverWait(driver, SHORT)
                    .until(d -> d.findElements(closeButton).isEmpty());
        } catch (Exception ignored) {}
    }

    private static void tapCenter(AppiumDriver driver, WebElement el) {
        int x = el.getRect().getX() + el.getRect().getWidth() / 2;
        int y = el.getRect().getY() + el.getRect().getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(tap));
    }

    private static <T> T firstOrNull(List<T> list) {
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}
