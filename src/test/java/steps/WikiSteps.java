package steps;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import screens.ArticleScreen;
import screens.HomeScreen;
import screens.SearchScreen;

public class WikiSteps {

    private final HomeScreen home;
    private final SearchScreen search;
    private final ArticleScreen article;

    public WikiSteps(AppiumDriver driver) {
        this.home = new HomeScreen(driver);
        this.search = new SearchScreen(driver);
        this.article = new ArticleScreen(driver);
    }

    @Step("Skip onboarding")
    public void skipOnboardingIfPresent() {
        home.skipOnboardingIfPresent();
    }

    @Step("Open search")
    public void openSearch() {
        home.openSearch();
    }

    @Step("Type query: {query}")
    public void typeSearchQuery(String query) {
        search.searchInput().sendKeys(query);
    }

    @Step("Open search result by title: {title}")
    public void openSearchResultByTitle(String title) {
        search.openResultByTitle(title);
    }

    @Step("Get opened article title")
    public String getOpenedArticleTitle() {
        return article.title().getText();
    }
}







