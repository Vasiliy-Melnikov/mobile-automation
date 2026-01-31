package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import steps.WikiSteps;

@Tag("android")
public class AndroidWikipediaTests extends TestBase {

    @Test
    void openAnyArticleTest() {
        WikiSteps steps = new WikiSteps(driver);

        steps.skipOnboardingIfPresent();
        steps.openSearch();
        steps.typeSearchQuery("Selenide");
        steps.openSearchResultByTitle("Selenide");

        Assertions.assertEquals("Selenide", steps.getOpenedArticleTitle());
    }
}







