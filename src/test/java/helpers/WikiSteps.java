package helpers;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

public class WikiSteps {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    public static void skipOnboardingIfPresent(AppiumDriver driver) {
        closeBlockingPopupsIfPresent(driver);

        clickIfExists(driver, AppiumBy.androidUIAutomator("new UiSelector().textContains(\"SKIP\")"), 2);
        clickIfExists(driver, AppiumBy.androidUIAutomator("new UiSelector().textContains(\"NEXT\")"), 2);
        clickIfExists(driver, AppiumBy.androidUIAutomator("new UiSelector().textContains(\"GOT IT\")"), 2);
        clickIfExists(driver, AppiumBy.androidUIAutomator("new UiSelector().textContains(\"CONTINUE\")"), 2);
        clickIfExists(driver, AppiumBy.androidUIAutomator("new UiSelector().textContains(\"ACCEPT\")"), 2);
        clickIfExists(driver, AppiumBy.androidUIAutomator("new UiSelector().textContains(\"NOT NOW\")"), 2);

        closeBlockingPopupsIfPresent(driver);
    }

    public static void openSearch(AppiumDriver driver) {
        closeBlockingPopupsIfPresent(driver);

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

    public static WebElement waitForSearchInput(AppiumDriver driver) {
        closeBlockingPopupsIfPresent(driver);

        var wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        return wait.until(d -> {
            var list = d.findElements(AppiumBy.id("org.wikipedia.alpha:id/search_src_text"));
            if (!list.isEmpty()) return list.get(0);

            list = d.findElements(AppiumBy.className("android.widget.EditText"));
            return list.isEmpty() ? null : list.get(0);
        });
    }

    public static void openSearchResultByTitle(AppiumDriver driver, String title) {
        closeBlockingPopupsIfPresent(driver);

        var wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);

        By titleText = AppiumBy.androidUIAutomator(
                "new UiSelector().className(\"android.widget.TextView\").text(\"" + title + "\")"
        );

        WebElement tv = wait.until(d -> {
            closeBlockingPopupsIfPresent((AppiumDriver) d); // <-- FIX
            var list = d.findElements(titleText);
            return list.isEmpty() ? null : list.get(0);
        });

        try {
            tv.click();
            return;
        } catch (Exception ignored) {
        }

        WebElement clickableParent = findClickableParent(driver, tv, 6);
        if (clickableParent != null) {
            try {
                clickableParent.click();
                return;
            } catch (Exception ignored) {
            }
            tapCenter(driver, clickableParent);
            return;
        }

        tapCenter(driver, tv);
    }

    public static WebElement waitForArticleOpened(AppiumDriver driver) {
        closeBlockingPopupsIfPresent(driver);

        By title = By.xpath("//android.webkit.WebView//android.widget.TextView[1]");

        return new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(d -> d.findElement(title));
    }

    private static void closeBlockingPopupsIfPresent(AppiumDriver driver) {
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
            new WebDriverWait(driver, Duration.ofSeconds(6))
                    .until(d -> d.findElements(AppiumBy.accessibilityId("Close")).isEmpty());
        } catch (Exception ignored) {
        }
    }

    private static void closeWikipediaGamesIfPresent(AppiumDriver driver) {
        By closeButton = AppiumBy.id("org.wikipedia.alpha:id/closeButton");
        WebElement btn = firstOrNull(driver.findElements(closeButton));
        if (btn == null) return;

        try { btn.click(); } catch (Exception ignored) { tapCenter(driver, btn); }

        try {
            new WebDriverWait(driver, Duration.ofSeconds(6))
                    .until(d -> d.findElements(closeButton).isEmpty());
        } catch (Exception ignored) {
        }
    }

    private static WebElement findClickableParent(AppiumDriver driver, WebElement el, int maxHops) {
        WebElement cur = el;
        for (int i = 0; i < maxHops; i++) {
            try {
                cur = cur.findElement(AppiumBy.xpath(".."));
                String clickable = cur.getAttribute("clickable");
                if ("true".equalsIgnoreCase(clickable)) return cur;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
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

    private static void clickIfExists(AppiumDriver driver, By by, int seconds) {
        try {
            var wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            var el = wait.until(d -> {
                var list = d.findElements(by);
                return list.isEmpty() ? null : list.get(0);
            });
            if (el != null) el.click();
        } catch (Exception ignored) {
        }
    }

    private static <T> T firstOrNull(List<T> list) {
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}






