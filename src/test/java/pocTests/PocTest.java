package pocTests;

import base.MobileWebDriverManager;
import base.listeners.MobileListener;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(MobileListener.class)
public class PocTest extends MobileWebDriverManager {

    @BeforeTest
    public void init() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        this.addCapabilitiesExtra(capabilities).getDriver();
    }

    @Test
    @Description("123456 test first pass")
    public void testFirstPass() {
        stepFlowExtensions()
                .setTestId("123456")

                .setStep("1","get current url", Status.INFO)
                .setStepCategory(TestCategory.DRIVER, TestSeverity.NONE)
                .step(this, action -> extentTest.log(Status.INFO, "driver to string " + action.getDriver().findElement(By.id("jj"))))

                .setStep("2","get page source", Status.FAIL)
                .setStepCategory(TestCategory.APPIUM, TestSeverity.HIGH)
                .step(this, action -> extentTest.log(Status.INFO, "page source " + action.getDriver().getPageSource()));
    }

    @Test
    @Description("1234561 test first fail")
    public void testFirstFail() {
        stepFlowExtensions()
                .setTestId("1234561")

                .setStep("1","get current url", Status.INFO)
                .setStepCategory(TestCategory.DRIVER, TestSeverity.NONE)
                .step(this, action -> extentTest.log(Status.INFO, "driver to string " + action.getDriver().findElement(By.id("jj"))))

                .setStep("2","get page source", Status.FAIL)
                .setStepCategory(TestCategory.APPIUM, TestSeverity.HIGH)
                .step(this, action -> extentTest.log(Status.INFO, "page source " + action.getDriver().findElement(By.id("sjdncksjdcn"))));
    }

    @Test
    @Description("1234562 test first skip")
    public void testFirstSkip() {
        stepFlowExtensions()
                .setTestId("1234562")

                .setStep("1","get current url", Status.INFO)
                .setStepCategory(TestCategory.DRIVER, TestSeverity.NONE)
                .step(this, action -> extentTest.log(Status.INFO, "driver to string " + action.getDriver().findElement(By.id("jj"))))

                .setStep("2","get page source", Status.SKIP)
                .setStepCategory(TestCategory.APPIUM, TestSeverity.HIGH)
                .step(this, action -> extentTest.log(Status.INFO, "page source " + action.getDriver().findElement(By.id("sjdncksjdcn"))));
    }
}
