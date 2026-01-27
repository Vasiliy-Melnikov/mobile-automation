package drivers;

import config.BrowserstackConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.MutableCapabilities;

import java.net.URL;
import java.util.Map;

public class MobileDriver {

    public static AppiumDriver createDriver() {
        String platform = System.getProperty("platform", "android").toLowerCase();
        System.setProperty("platform", platform);

        String res = "config/" + platform + ".properties";
        System.out.println("Looking for resource: " + res);
        System.out.println("Resource URL: " + MobileDriver.class.getClassLoader().getResource(res));

        try (var is = MobileDriver.class.getClassLoader().getResourceAsStream(res)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + res);
            }
            var p = new java.util.Properties();
            p.load(is);
            System.out.println("===== RAW PROPS FROM FILE =====");
            p.forEach((k, v) -> System.out.println(k + " = " + v));
            System.out.println("================================");
        } catch (Exception e) {
            throw new RuntimeException("Can't read " + res, e);
        }
        BrowserstackConfig cfg = ConfigFactory.create(
                BrowserstackConfig.class,
                System.getProperties(),
                Map.of("platform", platform)
        );

        System.out.println("platform=" + platform);
        System.out.println("cfg.platformName=" + cfg.platformName());
        System.out.println("cfg.automationName=" + cfg.automationName());
        System.out.println("cfg.device=" + cfg.device());
        System.out.println("cfg.osVersion=" + cfg.osVersion());
        System.out.println("cfg.app=" + cfg.app());
        System.out.println("cfg.url=" + cfg.url());

        requireNotBlank(cfg.platformName(), "platformName");
        requireNotBlank(cfg.automationName(), "automationName");
        requireNotBlank(cfg.device(), "device");
        requireNotBlank(cfg.osVersion(), "osVersion");
        requireNotBlank(cfg.app(), "bs.app");
        requireNotBlank(cfg.url(), "bs.url");

        MutableCapabilities caps = BrowserstackCapabilities.build(cfg);

        try {
            URL hub = new URL(cfg.url());
            return platform.equalsIgnoreCase("ios")
                    ? new IOSDriver(hub, caps)
                    : new AndroidDriver(hub, caps);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Appium driver", e);
        }
    }

    private static void requireNotBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing config value: " + name +
                    ". Проверь файл config/${platform}.properties и @Key(...)");
        }
    }
}


