package base.driversManager;

import base.listeners.DriverEventListener;
import base.mobile.AppiumFluentWaitExtensions;
import base.propertyConfig.PropertyConfig;
import base.reports.testFilters.*;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static base.driversManager.DriverManager.unloadLocalEvent;
import static base.reports.extentManager.ExtentLogger.reportTest;

@Slf4j
public class MobileManager {

    private static DesiredCapabilities capabilities = new DesiredCapabilities();

    public boolean isAndroidClient() {
        return getProperty().getPlatformType().equals("ANDROID");
    }

    public static PropertyConfig getProperty() {
        return new PropertyConfig();
    }

    public AppiumFluentWaitExtensions appiumFluentWaitExtensions(int elementTo, int pollingEvery) {
        return new AppiumFluentWaitExtensions()
                .withGeneralPollingWaitStrategy(Duration.ofSeconds(elementTo))
                .pollingEvery(Duration.ofMillis(pollingEvery));
    }

    public MobileManager addCapabilitiesExtra(DesiredCapabilities capabilities) {
        MobileManager.capabilities = capabilities;
        return this;
    }

    /**
     * first android/ios driver init
     * @return appium driver
     */
    public static WebDriver getDriver() {
        if (AppiumServerManager.getServer() == null) {
            AppiumServerManager.setServer(AppiumServerManager.initServer(getProperty().getNodeJs(), getProperty().getAppiumMainJsPath()));
            AppiumServerManager.getServer().start();
        }

        if (DriverManager.getLocalDriver() == null) {
            switch (getProperty().getPlatformType()) {
                case MobilePlatformType.ANDROID:
                    DriverManager.setLocalDriver(new AndroidWebDriverManager()
                            .addCapabilitiesExtra(capabilities)
                            .initAndroidDriver(AppiumServerManager.getServer().getUrl()));
                    break;
                case MobilePlatformType.IOS:
                    DriverManager.setLocalDriver(new IosWebDriverManager()
                            .addCapabilitiesExtra(capabilities)
                            .initIosDriver(AppiumServerManager.getServer().getUrl()));
                    break;
            }
        }

        if (DriverManager.getLocalDriver() == null) {
            reportTest(new Reasons(Status.FAIL,"init","init" ,TestCategory.DRIVER, TestSeverity.HIGH,"fail init driver"));
        }

        if (DriverManager.getEventFiringWebDriver() == null) {
            DriverManager.setEventFiringWebDriver(new EventFiringWebDriver(DriverManager.getLocalDriver()));
            DriverManager.getEventFiringWebDriver().register(new DriverEventListener());
        }
        return DriverManager.getEventFiringWebDriver();
    }

    /*** on after all or after each */
    public static void tearDown() {
        if (DriverManager.getLocalDriver() != null) {
            DriverManager.getLocalDriver().quit();
            DriverManager.unloadLocalDriver();
        }
        if (AppiumServerManager.getServer() != null) {
            AppiumServerManager.getServer().stop();
            AppiumServerManager.flush();
        }

        if (DriverManager.getEventFiringWebDriver() != null) {
            DriverManager.unloadLocalEvent();
        }
    }

    public void sleepSeconds(int timeOut) {
        this.sleep(timeOut, TimeUnit.SECONDS);
    }

    public void sleep(int timeOut, TimeUnit timeUnit) {
        try {
            switch (timeUnit) {
                case DAYS: TimeUnit.DAYS.sleep(timeOut); break;
                case HOURS: TimeUnit.HOURS.sleep(timeOut); break;
                case MINUTES: TimeUnit.MINUTES.sleep(timeOut); break;
                case SECONDS: TimeUnit.SECONDS.sleep(timeOut); break;
                case NANOSECONDS: TimeUnit.NANOSECONDS.sleep(timeOut); break;
                case MICROSECONDS: TimeUnit.MICROSECONDS.sleep(timeOut); break;
                case MILLISECONDS: TimeUnit.MILLISECONDS.sleep(timeOut); break;
            }
        } catch (InterruptedException var3) {
            log.error("sleep error " + var3.getMessage());
        }
    }

    public static String screenshot() {
        try {
            if (DriverManager.getLocalDriver() != null) {
                return ((TakesScreenshot) DriverManager
                        .getLocalDriver())
                        .getScreenshotAs(OutputType.BASE64);
            }
        } catch (Exception exception) {
            log.error("screen shot error " + exception.getMessage());
        }
        return "";
    }
}
