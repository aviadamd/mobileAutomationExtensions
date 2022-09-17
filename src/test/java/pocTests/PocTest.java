package pocTests;

import base.StepFlowExtensions;
import base.anontations.CategoryType;
import base.anontations.TestTarget;
import base.driversManager.MobileManager;
import base.listeners.MobileListener;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;

@Slf4j
@Listeners(MobileListener.class)
public class PocTest extends StepFlowExtensions {

    private StepFlowExtensions stepFlow;
    public static final String ANDROID_ID = "com.ideomobile.hapoalim:id/";
    private final String loginEle = ANDROID_ID + "login_user_name_view_automation";

    @BeforeClass
    public void init() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        this.addCapabilitiesExtra(capabilities);
        MobileManager.getDriver();
        this.stepFlow = new StepFlowExtensions();
    }

    @AfterTest
    public void tearDownTests() {
        this.tearDown();
    }

    @Test(priority = 1, description = "123456 first test poc")
    @TestTarget(author = {"aviad"}, category = { CategoryType.SANITY, CategoryType.REGRESSION})
    public void firstTestPoc() {
        this.stepFlow.steps("1-2", "click on user name edit text", action -> {
                    action.setStep("1")
                            .clickElementExtensions.clickElement(ExpectedConditions.elementToBeClickable(By.id(loginEle)), "login user name")
                            .proceed()
                            .setStep("2")
                            .clickElementExtensions.clickElement(ExpectedConditions.elementToBeClickable(By.id(loginEle)), "login user name");
                })
                .steps("3-4", "click on user name edit text", action -> {
                    action.setStep("3")
                            .clickElementExtensions.clickElement(ExpectedConditions.elementToBeClickable(By.id(loginEle)), "login user name")
                            .proceed()
                            .setStep("4")
                            .clickElementExtensions.clickElement(ExpectedConditions.elementToBeClickable(By.id(loginEle)), "login user name");
                });
    }
}
