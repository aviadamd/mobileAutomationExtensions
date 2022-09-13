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
            this.reportTest(this.failReportDto(Status.FAIL, TestCategory.DRIVER, "fail init android driver " + appiumEx.getMessage()));
        }
        return null;
    }


    private DesiredCapabilities initCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:app", getProperty().getAppPath());
        capabilities.setCapability("appium:appPackage", getProperty().getAppNamePackage());
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("appium:platformVersion", getProperty().getDeviceVersion());
        capabilities.setCapability("avd", getProperty().getDeviceName());
        capabilities.setCapability("udid", getProperty().getDeviceName());
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:noReset", true);
        if (this.capabilities != null) {
            capabilities.merge(this.capabilities);
        }
        return capabilities;
    }
}
