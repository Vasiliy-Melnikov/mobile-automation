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

public class SearchScreen {

    private final AppiumDriver driver;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    public SearchScreen(AppiumDriver driver) {
        this.driver = driver;
    }

    public WebElement searchInput() {
        Popups.closeBlockingPopupsIfPresent(driver);

        var wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        return wait.until(d -> {
            var list = d.findElements(AppiumBy.id("org.wikipedia.alpha:id/search_src_text"));
            if (!list.isEmpty()) return list.get(0);

            list = d.findElements(AppiumBy.className("android.widget.EditText"));
            return list.isEmpty() ? null : list.get(0);
        });
    }

    public void openResultByTitle(String title) {
        Popups.closeBlockingPopupsIfPresent(driver);

        By titleText = AppiumBy.androidUIAutomator(
                "new UiSelector().className(\"android.widget.TextView\").text(\"" + title + "\")"
        );

        var wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        WebElement tv = wait.until(d -> {
            Popups.closeBlockingPopupsIfPresent((AppiumDriver) d);
            var list = d.findElements(titleText);
            return list.isEmpty() ? null : list.get(0);
        });

        try { tv.click(); return; } catch (Exception ignored) {}

        WebElement clickableParent = findClickableParent(tv, 6);
        if (clickableParent != null) {
            try { clickableParent.click(); return; } catch (Exception ignored) {}
            tapCenter(clickableParent);
            return;
        }

        tapCenter(tv);
    }

    private WebElement findClickableParent(WebElement el, int maxHops) {
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

    private void tapCenter(WebElement el) {
        int x = el.getRect().getX() + el.getRect().getWidth() / 2;
        int y = el.getRect().getY() + el.getRect().getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(tap));
    }
}

