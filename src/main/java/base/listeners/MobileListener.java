package base.listeners;

import base.driversManager.MobileWebDriverManager;
import base.reports.extentManager.ExtentLogger;
import base.repository.MongoConnection;
import base.repository.ReportStepRepository;
import base.repository.ReportTestRepository;
import base.repository.mongo.adapters.ReasonsDtoAdapter;
import base.repository.mongo.notReactive.MongoCollectionRepoImpl;
import base.reports.testFilters.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.mongodb.client.model.Filters;
import org.springframework.context.annotation.Description;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.util.*;
import static base.staticData.MobileRegexConstants.SAVE_NUMERIC_CHARS;
import static com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String;

@SuppressWarnings("deprecation")
public class MobileListener extends MobileWebDriverManager implements ITestListener {

    //before all
    @Override
    public void onStart(ITestContext context) {
        ExtentLogger.initReports();
        mongoInstance = new MongoCollectionRepoImpl(new MongoConnection(
                getProperty().getMongoDbStringConnection(),
                getProperty().getMongoDbName()),
                getProperty().getMongoDbCollectionName());
    }

    //before test
    @Override
    public void onTestStart(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        ExtentLogger.createTest(methodTestName, getProperty().getDeviceName());
        ExtentLogger.loggerPrint(Status.INFO, "test " + methodTestName + " start");
        Reasons reasons = new Reasons(Status.PASS, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is started");
        mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
        ReportTestRepository.getInstance().save(reasons);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(Status.PASS, testId, methodTestName, TestCategory.PASS, TestSeverity.PASS, methodTestName + " is pass");
        String stepPrint = "test id " + reasons.getTestId() + ", test " + reasons.getTestStatus().getName();
        ExtentLogger.loggerPrint(Status.PASS, createScreenCaptureFromBase64String(screenshot(), stepPrint).build());
        this.updateTestStatus(reasons, testId, methodTestName, Status.PASS);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(Status.FAIL, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is fail");
        String stepPrint = "test id " + reasons.getTestId() + ", test " + reasons.getTestStatus().getName() + "".toUpperCase();
        ExtentLogger.loggerPrint(Status.FAIL, createScreenCaptureFromBase64String(screenshot(), stepPrint).build());
        this.updateTestStatus(reasons, testId, methodTestName, Status.FAIL);
        ExtentLogger.loggerPrint(Status.FAIL,  ExtentLogger.createExpend("Exception", Arrays.toString(iTestResult.getThrowable().getStackTrace()), ExtentColor.BLUE));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(Status.SKIP, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is skip");
        String stepPrint = "test id " + reasons.getTestId() + ", test " + reasons.getTestStatus().getName() + "".toUpperCase();
        ExtentLogger.loggerPrint(Status.SKIP, createScreenCaptureFromBase64String(screenshot(), stepPrint).build());
        this.updateTestStatus(reasons, testId, methodTestName, Status.SKIP);
        ExtentLogger.loggerPrint(Status.SKIP, ExtentLogger.createExpend("Exception", Arrays.toString(iTestResult.getThrowable().getStackTrace()), ExtentColor.BLUE));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    //after all
    @Override
    public void onFinish(ITestContext context) {
        ExtentLogger.setExtraReports(Map.of(
                "target/SparkFail.html", Status.FAIL,
                "target/SparkSkip.html", Status.SKIP,
                "target/SparkPass.html", Status.PASS));
        ExtentLogger.flushReports();
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
