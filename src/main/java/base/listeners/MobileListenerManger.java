package base.listeners;

import base.anontations.TestTarget;
import base.driversManager.MobileManager;
import base.reports.extentManager.ExtentLogger;
import base.reports.testFilters.Reasons;
import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import base.repository.MongoExtensionsManager;
import base.repository.ReportStepRepository;
import base.repository.ReportTestRepository;
import base.repository.mongo.adapters.ReasonsDtoAdapter;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.testng.ITestResult;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static base.staticData.MobileRegexConstants.SAVE_NUMERIC_CHARS;
import static com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String;

public class MobileListenerManger extends MobileManager {

    static Reasons insertTestStartData(ITestResult iTestResult, String methodTestName) {
        ExtentLogger.addAuthors(iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestTarget.class).author());
        ExtentLogger.addCategories(iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestTarget.class).category());
        ExtentLogger.loggerPrint(Status.INFO, "test " + methodTestName + " start");
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        return new Reasons(Status.PASS, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is started");
    }

    static Reasons insertTestPassData(String methodTestName, String screenShot) {
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(Status.PASS, testId, methodTestName, TestCategory.PASS, TestSeverity.PASS, methodTestName + " is pass");
        String stepPrint = "test id " + reasons.getTestId() + ", test " + reasons.getTestStatus().getName();
        if (screenShot != null && !screenShot.isEmpty())
            ExtentLogger.loggerPrint(Status.PASS, createScreenCaptureFromBase64String(screenShot, stepPrint).build());
        return reasons;
    }

    static Reasons insertTestFailData(ITestResult iTestResult, String methodTestName, String screenShot, Status status, ExtentColor extentColor) {
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(status, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is " + status.toString().toUpperCase());
        String stepPrint = "test id " + reasons.getTestId() + ", test " + reasons.getTestStatus().getName() + "".toUpperCase();
        if (screenShot != null && !screenShot.isEmpty())
            ExtentLogger.loggerPrint(status, createScreenCaptureFromBase64String(screenShot, stepPrint).build());
        ExtentLogger.loggerPrint(status, ExtentLogger.createExpend("Exception", Arrays.toString(iTestResult.getThrowable().getStackTrace()), extentColor));
        return reasons;
    }
    static void updateTestStatus(Reasons reasons, String testId, String methodTestName, Status status) {
        Document document = null;

        Optional<Reasons> reasonsStep = ReportTestRepository.getInstance().findObjectById(testId);
        if (reasonsStep.isPresent()) {
            List<ReasonsStep> reasonsStepList = ReportStepRepository.getInstance().getAllObjects();
            if (!reasonsStepList.isEmpty()) {
                ReasonsStep last = ReportStepRepository.getInstance().getLastObject();
                document = ReasonsDtoAdapter.toDocumentWithSteps(new Reasons(status, testId, methodTestName, last.getCategory(), last.getSeverity(), methodTestName + " is " + status.getName(), reasonsStepList));
            }
        }

        if (document == null) document = ReasonsDtoAdapter.toDocument(reasons);
        MongoExtensionsManager.getMongoInstance().deleteElement(Filters.eq("_id", testId));
        MongoExtensionsManager.getMongoInstance().insertElement(document);
    }
}
