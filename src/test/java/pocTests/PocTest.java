package pocTests;

import base.StepFlowExtensions;
import base.listeners.MobileListener;
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
    private final String loginEle = ANDROID_ID + "login_user_name_view_automation";

    @BeforeTest
    public void init() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        this.addCapabilitiesExtra(capabilities);
        this.getDriver();
        this.stepFlow = new StepFlowExtensions();
    }

    @AfterTest
    public void tearDownTests() {
        this.tearDown();
    }

    @Test
    @Description("123456 first test poc")
    public void firstTestPoc() {
        this.stepFlow
                .step("1", "load login page", action -> {
                    action.clickElementExtensions
                            .setStep("1")
                            .clickElementBy(By.id(loginEle), "login user name")
                            .proceed()
                            .setStep("2")
                            .clickElementBy(By.id(loginEle),"login user");
                }).step("3", "load login page", action -> {
                    action.clickElementExtensions
                            .setStep("3")
                            .clickElementBy(By.id(loginEle), "login user name");
                });

    }
}
