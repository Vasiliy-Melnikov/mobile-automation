package tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class IOSWikipediaTests extends TestBase {

    @Test
    @Tag("ios")
    void openAnyArticleTestios() {
        $(AppiumBy.accessibilityId("Search Wikipedia")).click();
        $(AppiumBy.accessibilityId("Search")).setValue("Selenide");
        $(AppiumBy.accessibilityId("Selenide")).shouldBe(visible).click();
        $(AppiumBy.accessibilityId("Selenide")).shouldBe(visible);
    }
}

