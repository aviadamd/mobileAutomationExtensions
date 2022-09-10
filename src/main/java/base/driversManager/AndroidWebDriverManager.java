package base.driversManager;

import base.MobileWebDriverManager;
import base.reports.testFilters.TestCategory;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Description;

import java.net.URL;

@Slf4j
@Description("use as a class that extends DriverManager abstract class template")
public class AndroidWebDriverManager extends MobileWebDriverManager {

    private DesiredCapabilities capabilities;
    public AndroidWebDriverManager addCapabilitiesExtra(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public AndroidDriver<WebElement> initAndroidDriver(URL url) {
        try {
            return new AndroidDriver<>(url, this.initCapabilities());
        } catch (Exception appiumEx) {
            this.reportTest(this.failReportDto(Status.FAIL, TestCategory.DRIVER, appiumEx.getMessage()));
        }
        return null;
    }


    private DesiredCapabilities initCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:app", "C:/Users/Lenovo/IdeaProjects/mobileExtensions/src/main/resources/application.apk");
        capabilities.setCapability("appium:appPackage", "com.ideomobile.hapoalim");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        //capabilities.setCapability("appium:deviceName", getProperty.getDeviceName());
        capabilities.setCapability("appium:platformVersion","11");
        capabilities.setCapability("avd", "Pixel_4_API_30");
        capabilities.setCapability("udid", "Pixel_4_API_30");
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:noReset", true);
        if (this.capabilities != null) {
            capabilities.merge(this.capabilities);
        }
        return capabilities;
    }
}
