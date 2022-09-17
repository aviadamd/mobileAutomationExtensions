package base.driversManager;

import base.reports.testFilters.Reasons;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import base.repository.ReportTestRepository;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Description;

import java.net.URL;

@Slf4j
@Description("use as a class that extends DriverManager abstract class template")
public class AndroidWebDriverManager extends MobileManager {

    private DesiredCapabilities capabilities;
    public AndroidWebDriverManager addCapabilitiesExtra(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public AndroidDriver<WebElement> initAndroidDriver(URL url) {
        AndroidDriver<WebElement> androidDriver = null;
        try {
            androidDriver = new AndroidDriver<>(url, this.initCapabilities());
            ReportTestRepository.getInstance().save(new Reasons(Status.PASS,"init","init" ,TestCategory.APPIUM, TestSeverity.HIGH,"fail init android driver"));
        } catch (Exception appiumEx) {
            ReportTestRepository.getInstance().save(new Reasons(Status.FAIL,"init","init" ,TestCategory.APPIUM, TestSeverity.HIGH,"fail init android driver"));
        }
        return androidDriver;
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
