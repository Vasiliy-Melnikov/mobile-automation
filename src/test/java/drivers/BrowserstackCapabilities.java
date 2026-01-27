package drivers;

import config.BrowserstackConfig;
import org.openqa.selenium.MutableCapabilities;

import java.util.HashMap;
import java.util.Map;

public class BrowserstackCapabilities {

    public static MutableCapabilities build(BrowserstackConfig cfg) {
        MutableCapabilities caps = new MutableCapabilities();

        caps.setCapability("platformName", cfg.platformName());
        caps.setCapability("appium:automationName", cfg.automationName());
        caps.setCapability("appium:deviceName", cfg.device());
        caps.setCapability("appium:platformVersion", cfg.osVersion());
        caps.setCapability("appium:app", cfg.app());

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", cfg.user());
        bstackOptions.put("accessKey", cfg.key());
        bstackOptions.put("projectName", cfg.project());
        bstackOptions.put("buildName", cfg.build());
        bstackOptions.put("sessionName", cfg.name());

        caps.setCapability("bstack:options", bstackOptions);

        return caps;
    }
}

