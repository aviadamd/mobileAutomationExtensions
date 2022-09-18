package pocTests;

import base.StepFlowExtensions;
import base.anontations.CategoryType;
import base.anontations.TestTarget;
import base.driversManager.MobileManager;
import base.listeners.MobileListener;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
@Listeners(value = MobileListener.class)
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

    @AfterClass
    public void tearDownTests() {
        MobileManager.tearDown();
    }

    @Test(priority = 1, description = "123456 first test poc")
    @TestTarget(testId = "123456", author = {"aviad"}, category = { CategoryType.SANITY, CategoryType.REGRESSION})
    public void firstTestPoc() {
        this.stepFlow.steps("1-2", "click on user name edit text", action -> {
                    action.clickElementExtensions
                            .setStepNumber("1")
                            .clickElement(By.id(loginEle), "login user name")
                            .proceed()
                            .clickElementExtensions
                            .setStepNumber("2")
                            .clickElement(By.id(loginEle), "login user name");
                })
                .steps("3-4", "click on user name edit text", action -> {
                    action.clickElementExtensions
                            .setStepNumber("3")
                            .clickElement(By.id(loginEle), "login user name")
                            .proceed()
                            .clickElementExtensions
                            .setStepNumber("4")
                            .clickElement(By.id(loginEle), "login user name");
                });
    }
    @Test(priority = 2, description = "123457 first test poc")
    @TestTarget(testId = "123457", author = {"aviad"}, category = { CategoryType.SANITY, CategoryType.REGRESSION})
    public void secondTestPoc() {
        this.stepFlow.steps("1-2", "click on user name edit text", action -> {
                    action.clickElementExtensions
                            .setStepNumber("1")
                            .clickElement(By.id(loginEle), "login user name")
                            .proceed()
                            .clickElementExtensions
                            .setStepNumber("2")
                            .clickElement(By.id(loginEle), "login user name");
                })
                .steps("3-4", "click on user name edit text", action -> {
                    action.clickElementExtensions
                            .setStepNumber("3")
                            .clickElement(By.id(loginEle), "login user name")
                            .proceed()
                            .clickElementExtensions
                            .setStepNumber("4")
                            .clickElement(By.id(loginEle), "login user name");
                });
    }
}
