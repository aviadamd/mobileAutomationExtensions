package base.driversManager;

import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import base.reports.ReportStepRepository;
import base.staticData.MobileStringsUtilities;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

@Slf4j
public class AndroidWebDriverManager extends MobileManager {
    private DesiredCapabilities capabilities = new DesiredCapabilities();

    public AndroidWebDriverManager addCapabilitiesExtra(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public AndroidDriver<WebElement> initAndroidDriver(URL url) {
        AndroidDriver<WebElement> androidDriver = null;
        try {
            androidDriver = new AndroidDriver<>(url, this.initCapabilities());
            ReportStepRepository.getInstance().save(new ReasonsStep(Status.PASS,"init" ,TestCategory.APPIUM, TestSeverity.HIGH,"pass init android driver"));
        } catch (Exception appiumEx) {
            ReportStepRepository.getInstance().save(new ReasonsStep(Status.FAIL,"init" ,TestCategory.APPIUM, TestSeverity.HIGH,"fail init android driver " + appiumEx.getMessage()));
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
        capabilities.setCapability("newCommandTimeout", MobileStringsUtilities.toInt("15000", 15000));
        capabilities.setCapability("androidDeviceReadyTimeout", 30);
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:noReset", true);

        if (this.capabilities != null && !this.capabilities.asMap().isEmpty()) {
            capabilities.merge(this.capabilities);
        }

        return capabilities;
    }
}
