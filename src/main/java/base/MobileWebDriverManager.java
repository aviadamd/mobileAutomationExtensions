package base;

import base.driversManager.AndroidWebDriverManager;
import base.driversManager.IosWebDriverManager;
import base.driversManager.MobilePlatformType;
import base.repository.ReportStepRepository;
import base.repository.ReportTestRepository;
import base.reports.testFilters.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static base.reports.extentManager.ExtentReportManager.extentTest;

@Slf4j
public class MobileWebDriverManager extends MobileExtensionContext {

    private static WebDriver setDriver;
    private static AppiumDriverLocalService server;
    private DesiredCapabilities capabilities = new DesiredCapabilities();

    public MobileWebDriverManager addCapabilitiesExtra(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public boolean isAndroidClient() {
        return getProperty()
                .getPlatformType().equals("ANDROID");
    }

    /**
     * first android/ios driver init
     * @return appium driver
     */
    public WebDriver getDriver() {
        if (server == null) {
            server = this.initServer();
            server.start();
        }

        if (setDriver == null) {
            switch (getProperty().getPlatformType()) {
                case MobilePlatformType.ANDROID:
                    setDriver = new AndroidWebDriverManager()
                            .addCapabilitiesExtra(this.capabilities)
                            .initAndroidDriver(server.getUrl());
                    break;
                case MobilePlatformType.IOS:
                    setDriver = new IosWebDriverManager()
                            .addCapabilitiesExtra(this.capabilities)
                            .initIosDriver(server.getUrl());
                    break;
            }
        }
        return setDriver;
    }

    /*** on after all or after each */
    public void tearDown() {
        if (this.getDriver() != null) this.getDriver().quit();
        if (server != null) server.stop();
    }

    public void reportTest(Reasons reportTestDto) {
        ReportTestRepository.getInstance().save(reportTestDto);
        extentTest.log(reportTestDto.getTestStatus(), "report description: " + reportTestDto.getDescription());
        if (reportTestDto.getTestStatus() == Status.FAIL || reportTestDto.getTestStatus() == Status.SKIP) {
            Assert.fail(reportTestDto.getTestStatus().toString() + "," + reportTestDto);
        }
    }

    public void reportTest(Reasons reportTestDto, CodeLanguage codeLanguage) {
        ReportTestRepository.getInstance().save(reportTestDto);
        extentTest.log(reportTestDto.getTestStatus(), MarkupHelper.createCodeBlock(reportTestDto.getDescription(), codeLanguage));
        if (reportTestDto.getTestStatus() == Status.FAIL || reportTestDto.getTestStatus() == Status.SKIP) {
            Assert.fail(reportTestDto.getTestStatus().toString() + "," + reportTestDto);
        }
    }

    public void reportStepTest(ReasonsStep reportStepDto) {
        ReportStepRepository.getInstance().save(reportStepDto);
        extentTest.log(reportStepDto.getStatus(), "description: " + reportStepDto.getDescription());
        if (reportStepDto.getStatus() == Status.FAIL || reportStepDto.getStatus() == Status.SKIP) {
            Assert.fail(reportStepDto.getStatus().toString() + " , " + reportStepDto);
        }
    }

    public void reportStepTest(ReasonsStep reportStepDto, CodeLanguage codeLanguage) {
        ReportStepRepository.getInstance().save(reportStepDto);
        extentTest.log(reportStepDto.getStatus(), MarkupHelper.createCodeBlock(reportStepDto.getDescription(), codeLanguage));
        if (reportStepDto.getStatus() == Status.FAIL || reportStepDto.getStatus() == Status.SKIP) {
            Assert.fail(reportStepDto.getStatus().toString() + " , " + reportStepDto);
        }
    }

    public Reasons failReportDto(Status status, TestCategory category, String desc) {
        return new Reasons(
                status,
                "Init",
                "Init",
                category,
                TestSeverity.HIGH,
                desc
        );
    }

    public HashMap<String,String> environment() {
        HashMap<String, String> environment = new HashMap<>();
        environment.put("PATH", getProperty().getLocalBin() + System.getenv("PATH"));
        environment.put("ANDROID_HOME", getProperty().getAndroidSdk());
        environment.put("JAVA_HOME", getProperty().getJavaHome());
        return environment;
    }

    private AppiumDriverLocalService initServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        try {
            builder.usingDriverExecutable(new File(getProperty().getNodeJs()))
                    .withAppiumJS(new File(getProperty().getAppiumMainJsPath()))
                    .usingAnyFreePort()
                    .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                    .withStartUpTimeOut(15, TimeUnit.SECONDS);
        } catch (Exception appiumEx) {
            reportTest(this.failReportDto(Status.FAIL, TestCategory.APPIUM,"fail launch appium server"));
        }
        return AppiumDriverLocalService.buildService(builder);
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
            if (setDriver != null) {
                TakesScreenshot ts = (TakesScreenshot) setDriver;
                String source = ts.getScreenshotAs(OutputType.BASE64);
                return "data:image/jpg;base64, " + source;
            }
        } catch (Exception exception) {
            log.error("screen shot error " + exception.getMessage());
        }
        return "";
    }
}
