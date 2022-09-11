package base.listeners;

import base.MobileWebDriverManager;
import base.reports.extentManager.ExtentReportManager;
import base.repository.MongoConnection;
import base.repository.ReportStepRepository;
import base.repository.ReportTestRepository;
import base.repository.mongo.adapters.ReasonsDtoAdapter;
import base.repository.mongo.notReactive.MongoCollectionRepoImpl;
import base.reports.testFilters.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.mongodb.client.model.Filters;
import org.springframework.context.annotation.Description;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.util.*;
import static base.reports.extentManager.ExtentReportManager.extentTest;
import static base.staticData.MobileRegexConstants.SAVE_NUMERIC_CHARS;

@SuppressWarnings("deprecation")
public class MobileListener extends MobileWebDriverManager implements ITestListener {

    //before all
    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initReports();
        mongoInstance = new MongoCollectionRepoImpl(new MongoConnection(
                getProperty().getMongoDbStringConnection(),
                getProperty().getMongoDbName()),
                getProperty().getMongoDbCollectionName());
    }

    //before test
    @Override
    public void onTestStart(ITestResult iTestResult) {
        String testName = iTestResult.getMethod().getMethod().getName();
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        ExtentReportManager.createTest(testName);
        Reasons reasons = new Reasons(Status.PASS, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is started");
        mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
        ReportTestRepository.getInstance().save(reasons);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(Status.PASS, testId, methodTestName, TestCategory.PASS, TestSeverity.PASS, methodTestName + " is pass");
        String stepPrint = "test id " + reasons.getTestId() + ", test status " + reasons.getTestStatus().getName() + "".toUpperCase();
        extentTest.log(Status.INFO, MarkupHelper.createLabel(stepPrint, ExtentColor.BLUE));
        this.updateTestStatus(reasons, testId, methodTestName, Status.PASS);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(Status.FAIL, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is fail");
        String stepPrint = "test id " + reasons.getTestId() + ", test status " + reasons.getTestStatus().getName() + "".toUpperCase();
        extentTest.log(Status.FAIL, MarkupHelper.createLabel(stepPrint, ExtentColor.RED));
        this.updateTestStatus(reasons, testId, methodTestName, Status.FAIL);
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(Status.SKIP, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is skip");
        String stepPrint = "test id " + reasons.getTestId() + ", test status " + reasons.getTestStatus().getName() + "".toUpperCase();
        extentTest.log(Status.SKIP, MarkupHelper.createLabel(stepPrint, ExtentColor.GREEN));
        this.updateTestStatus(reasons, testId, methodTestName, Status.SKIP);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    //after all
    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReports();
    }

    public void updateTestStatus(Reasons reasons, String testId, String methodTestName, Status status) {
        Optional<Reasons> reasonsStep = ReportTestRepository.getInstance().findObjectById(testId);
        if (reasonsStep.isPresent()) {
            List<ReasonsStep> reasonsStepList = ReportStepRepository.getInstance().getAllObjects();
            if (!reasonsStepList.isEmpty()) {
                ReasonsStep last = ReportStepRepository.getInstance().getLastObject();
                reasons = new Reasons(status, testId, methodTestName, last.getCategory(), last.getSeverity(), methodTestName + " is " + status.getName(), reasonsStepList);
                mongoInstance.deleteElement(Filters.eq("_id", testId));
                mongoInstance.insertElement(ReasonsDtoAdapter.toDocumentWithSteps(reasons));
            } else {
                mongoInstance.deleteElement(Filters.eq("_id", testId));
                mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
            }
        } else {
            mongoInstance.deleteElement(Filters.eq("_id", testId));
            mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
        }
    }

}
