package tests;

import helpers.WikiSteps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("android")
public class AndroidWikipediaTests extends TestBase {

    @Test
    void openAnyArticleTest() {
        WikiSteps.skipOnboardingIfPresent(driver);
        WikiSteps.openSearch(driver);

        WikiSteps.waitForSearchInput(driver).sendKeys("Selenide");
        WikiSteps.openSearchResultByTitle(driver, "Selenide");

        String title = WikiSteps.waitForArticleOpened(driver).getText();
        Assertions.assertEquals("Selenide", title);
    }
}






