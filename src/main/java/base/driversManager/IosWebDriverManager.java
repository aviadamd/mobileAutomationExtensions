package base.driversManager;

import base.MobileWebDriverManager;
import base.reports.testFilters.TestCategory;
import com.aventstack.extentreports.Status;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;

@Slf4j
public class IosWebDriverManager extends MobileWebDriverManager {

    private DesiredCapabilities capabilities;
    public IosWebDriverManager addCapabilitiesExtra(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public IOSDriver<WebElement> initIosDriver(URL url) {
        try {
            return new IOSDriver<>(url, this.initCapabilities());
        } catch (Exception appiumEx) {
            this.reportTest(this.failReportDto(Status.FAIL, TestCategory.DRIVER, appiumEx.getMessage()));
        }
        return null;
    }

    private DesiredCapabilities initCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:app", getProperty().getAppPath());
        capabilities.setCapability("appium:platformVersion", "14.2");
        capabilities.setCapability("appium:platformName", "iOS");
        capabilities.setCapability("appium:noReset", "true");
        capabilities.setCapability("appium:deviceName", "iPhone 11 Pro Max");
        capabilities.setCapability("appium:automationName", "XCUITest");
        capabilities.setCapability("appium:allowTouchIdEnroll", true);
        capabilities.setCapability("appium:useJSONSource",true);
        capabilities.setCapability("appium:clearDeviceLogsOnStart",true);
        capabilities.setCapability("appium:skipUnlock", true);
        capabilities.setCapability("appium:skipServerInstallation", true);
        capabilities.setCapability("appium:ignoreUnimportantViews", true);
        capabilities.setCapability("appium:autoDismissAlerts",true);
        capabilities.setCapability(IOSMobileCapabilityType.ACCEPT_SSL_CERTS,true);
        if (this.capabilities != null) {
            capabilities.merge(this.capabilities);
        }

        return capabilities;
    }
}
