package helpers;

import config.BrowserstackConfig;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BrowserstackApi {

    private final BrowserstackConfig cfg;

    public BrowserstackApi() {
        String platform = System.getProperty("platform", "android");
        this.cfg = ConfigFactory.create(
                BrowserstackConfig.class,
                System.getProperties(),
                Map.of("platform", platform)
        );
        RestAssured.baseURI = "https://api.browserstack.com";
    }

    public void attachVideo(String sessionId) {
        String videoUrl = given()
                .auth().preemptive().basic(getUser(), getKey())
                .when()
                .get("/app-automate/sessions/" + sessionId + ".json")
                .then()
                .statusCode(200)
                .extract().path("automation_session.video_url");

        if (videoUrl != null && !videoUrl.isBlank()) {
            String html = "<html><body><video width='100%' height='100%' controls autoplay>" +
                    "<source src='" + videoUrl + "' type='video/mp4'></video></body></html>";
            Allure.addAttachment("Video", "text/html", html, "html");
        }
    }

    private String getUser() {
        String u = cfg.user();
        if (u == null || u.isBlank()) u = System.getenv("BROWSERSTACK_USER");
        return u;
    }

    private String getKey() {
        String k = cfg.key();
        if (k == null || k.isBlank()) k = System.getenv("BROWSERSTACK_KEY");
        return k;
    }
}