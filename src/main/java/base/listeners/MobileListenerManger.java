package base.listeners;

import base.anontations.TestTarget;
import base.driversManager.MobileManager;
import base.reports.extentManager.ExtentLogger;
import base.reports.testFilters.Reasons;
import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import base.repository.MongoExtensionsManager;
import base.reports.ReportStepRepository;
import base.reports.ReportTestRepository;
import base.repository.mongo.adapters.ReasonsDtoAdapter;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.testng.ITestResult;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String;
@Slf4j
public class MobileListenerManger extends MobileManager {

    static Reasons insertTestStartData(String testId, ITestResult iTestResult, String methodTestName) {
        ExtentLogger.addAuthors(iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestTarget.class).author());
        ExtentLogger.addCategories(iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestTarget.class).category());
        ExtentLogger.loggerPrint(Status.INFO, "test " + methodTestName + " start");
        return new Reasons(Status.PASS, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is started");
    }

    static Reasons insertTestPassData(String testId,String methodTestName, String screenShot) {
        Reasons reasons = new Reasons(Status.PASS, testId, methodTestName, TestCategory.PASS, TestSeverity.PASS, methodTestName + " is pass");
        String stepPrint = "test id " + reasons.getTestId() + ", test " + reasons.getTestStatus().getName();
        if (screenShot != null && !screenShot.isEmpty())
            ExtentLogger.loggerPrint(Status.PASS, createScreenCaptureFromBase64String(screenShot, stepPrint).build());
        else ExtentLogger.loggerPrint(Status.PASS, stepPrint);
        return reasons;
    }

    static Reasons insertTestFailData(String testId,ITestResult iTestResult, String methodTestName, String screenShot, Status status, ExtentColor extentColor) {
        Reasons reasons = new Reasons(status, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is " + status.toString().toUpperCase());
        String stepPrint = "test id " + reasons.getTestId() + ", test " + reasons.getTestStatus().getName() + "".toUpperCase();
        if (screenShot != null && !screenShot.isEmpty())
            ExtentLogger.loggerPrint(status, createScreenCaptureFromBase64String(screenShot, stepPrint).build());
        ExtentLogger.loggerPrint(status, ExtentLogger.createExpend("Exception", Arrays.toString(iTestResult.getThrowable().getStackTrace()), extentColor));
        return reasons;
    }
    static void updateTestStatus(Reasons reasons, String testId, String methodTestName, Status status) {
        Document document = null;

        Optional<Reasons> reasonsTest = ReportTestRepository.getInstance().findObjectById(testId);
        if (reasonsTest.isPresent()) {
            List<ReasonsStep> reasonsStepList = ReportStepRepository.getInstance().getAllObjects();
            if (!reasonsStepList.isEmpty()) {
                ReasonsStep last = ReportStepRepository.getInstance().getLastObject();
                document = ReasonsDtoAdapter.toDocumentWithSteps(new Reasons(status, testId, methodTestName, last.getCategory(), last.getSeverity(), methodTestName + " is " + status.getName(), reasonsStepList));
            }
        }

        if (document == null) document = ReasonsDtoAdapter.toDocument(reasons);
        ExtentLogger.loggerPrint(status, ExtentLogger.createExpend("Test Json Info", document.toJson(), ExtentColor.BLUE));
        MongoExtensionsManager.getMongoInstance().deleteElement(Filters.eq("_id", testId));
        MongoExtensionsManager.getMongoInstance().insertElement(document);
    }

    static Optional<ITestResult> getTestResult(ITestResult iTestResult) {
        return Optional.ofNullable(iTestResult);
    }
    static Optional<OnFailRetry> getOnFailAnnotation(ITestResult iTestResult) {
        return Optional.ofNullable(iTestResult
                .getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(OnFailRetry.class));
    }
}
