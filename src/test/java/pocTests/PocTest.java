package pocTests;

import base.StepFlowExtensions;
import base.listeners.MobileListener;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Description;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(MobileListener.class)
public class PocTest extends StepFlowExtensions {

    private StepFlowExtensions stepFlow;
    public static final String ANDROID_ID = "com.ideomobile.hapoalim:id/";

    @BeforeTest
    public void init() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        this.addCapabilitiesExtra(capabilities).getDriver();
        this.stepFlow = new StepFlowExtensions();
    }

    @AfterTest
    public void tearDownTests() {
        this.tearDown();
    }

    @Test
    @Description("123456 first test poc")
    public void firstTestPoc() {
        this.stepFlow.setTestId("123456")
                .setStep("1","load login page", Status.FAIL)
                .step(action -> {
                    action.clickBy(10, By.id(ANDROID_ID + "login_user_name_view_automation"),"login base edit text");
                })
                .setStep("2","click on edit text", Status.FAIL)
                .step(action -> {
                    action.clickBy(10, By.id(ANDROID_ID + "login_user_name_view_automation"),"login base edit text");
                });
    }
}
