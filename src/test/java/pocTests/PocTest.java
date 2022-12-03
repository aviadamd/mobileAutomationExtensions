package pocTests;

import base.CreateReportExtra;
import base.StepFlowExtensions;
import base.anontations.CategoryType;
import base.anontations.TestTarget;
import base.driversManager.MobileManager;
import base.listeners.OnFailRetry;
import base.listeners.MobileAnnotationRetryListener;
import base.listeners.MobileListener;
import base.listeners.MobileRetryAnalyzer;
import base.reports.testFilters.TestCategory;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.annotations.*;

@Listeners(value = { MobileListener.class, MobileAnnotationRetryListener.class})
@CreateReportExtra(
        reportStatus = { Status.FAIL },
        filterBy = { TestCategory.DRIVER })
public class PocTest extends StepFlowExtensions {

    private StepFlowExtensions stepFlow;
    public static final String ANDROID_ID = "com.ideomobile.hapoalim:id/";
    private final String loginEle = ANDROID_ID + "login_user_name_view_automation";

    @BeforeMethod
    public void init() {
        this.stepFlow = new StepFlowExtensions();
    }

    @AfterClass
    public void tearDownTests() {
        MobileManager.tearDown();
    }

    @OnFailRetry(2)
    @Test(priority = 1, description = "123456 first test poc", retryAnalyzer = MobileRetryAnalyzer.class)
    @TestTarget(testId = "123456", author = {"aviad"}, category = { CategoryType.SANITY, CategoryType.REGRESSION }, suiteName = "login")
    public void firstTestPoc() {
        this.stepFlow.steps("1-2", "click on user name edit text", (action) -> {
                    action.clickElementExtensions
                            .setStepNumber("1")
                            .clickElement(By.id(loginEle), "login user name")
                            .proceed()
                            .clickElementExtensions
                            .setStepNumber("2")
                            .clickElement(By.id(loginEle), "login user name");
                })
                .steps("3-4", "click on user name edit text", (action) -> {
                    action.clickElementExtensions
                            .setStepNumber("3")
                            .clickElement(By.id(loginEle), "login user name")
                            .proceed()
                            .clickElementExtensions
                            .setStepNumber("4")
                            .clickElement(By.id(loginEle), "login user name");
                }).steps("5-6","click on element", action -> {
                    extensions.clickElementExtensions.clickElement(By.id(loginEle),"login user name");
                    extensions.clickElementExtensions.clickElement(By.id(loginEle),"login user name");
                    }, Exception.class
                );
    }
    @OnFailRetry(1)
    @Test(priority = 2, description = "123457 first test poc", retryAnalyzer = MobileRetryAnalyzer.class)
    @TestTarget(testId = "123457", author = {"aviad"}, category = { CategoryType.SANITY, CategoryType.REGRESSION}, suiteName = "post_login")
    public void secondTestPoc() {
        this.stepFlow.steps("1-2", "click on user name edit text", (action) -> {
                    action.clickElementExtensions
                            .setStepNumber("1")
                            .clickElement(By.id(loginEle), "login user name")
                            .proceed()
                            .clickElementExtensions
                            .setStepNumber("2")
                            .clickElement(By.id(loginEle), "login user name");
                })
                .steps("3-4", "click on user name edit text", (action) -> {
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
