package base.listeners;

import base.MobileWebDriverManager;
import base.repository.MongoConnection;
import base.repository.mongo.adapters.ReasonsDtoAdapter;
import base.repository.mongo.notReactive.MongoCollectionRepoImpl;
import base.reports.testFilters.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.context.annotation.Description;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("deprecation")
public class MobileListener extends MobileWebDriverManager implements ITestListener {

    private static ExtentReports extentReports;
    private static ExtentSparkReporter sparkReporter;
    private static final String SAVE_NUMERIC_CHARS =                           "[^0-9]";

    //before all
    @Override
    public void onStart(ITestContext context) {
        extentReports = new ExtentReports();
        sparkReporter = new ExtentSparkReporter(getProperty().getExtentSparkPath())
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extentReports.attachReporter(sparkReporter);
        this.loadReportConfiguration(context.getStartDate().toString(), getProperty().getReportJsonTemplatePath());
        mongoInstance = new MongoCollectionRepoImpl(
                new MongoConnection(
                        getProperty().getMongoDbStringConnection(),
                        getProperty().getMongoDbName()),
                getProperty().getMongoDbCollectionName()
        );
    }

    //before test
    @Override
    public void onTestStart(ITestResult iTestResult) {
        String testName = iTestResult.getMethod().getMethod().getName();
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");

        extentTest = extentReports.createTest(testName);
        extentTest.assignDevice(getProperty().getDeviceName());
        Reasons reasons = new Reasons(Status.PASS, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is started");
        mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
        reportTestRepository().save(reasons);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");

        Reasons reasons = new Reasons(Status.PASS, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is pass");
        extentTest.log(Status.INFO, MarkupHelper.createLabel(reasons.toString(), ExtentColor.BLUE));
        mongoInstance.deleteElement(Filters.eq("_id", testId));
        mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
        reportTestRepository().save(reasons);
    }

    @SneakyThrows
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");
        Reasons reasons = new Reasons(Status.FAIL, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is fail");

        Optional<ReasonsStep> reasonsStep = reportStepRepository().findObjectById(testId);
        if (reasonsStep.isPresent()) {
            reasons.setCategory(reasonsStep.get().getCategory());
            reasons.setSeverity(reasonsStep.get().getSeverity());
            reasons.setSeverity(reasonsStep.get().getSeverity());
            reasons.setDescription(reasonsStep.get().getDescription());
        }


        if (getDriver() != null) {
            String path = "target/screenShots/image.png";
            File source = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File("target/screenShots/image.png"));
            extentTest.log(reasons.getTestStatus(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }
        extentTest.log(Status.INFO, MarkupHelper.createLabel(reasons.toString(), ExtentColor.RED));
        reportTestRepository().save(reasons);
        mongoInstance.deleteElement(Filters.eq("_id", testId));
        mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");

        Reasons reasons = new Reasons(Status.SKIP, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is skipped");
        Optional<ReasonsStep> reasonsStep = reportStepRepository().findObjectById(testId);
        if (reasonsStep.isPresent()) {
            reasons.setCategory(reasonsStep.get().getCategory());
            reasons.setSeverity(reasonsStep.get().getSeverity());
            reasons.setSeverity(reasonsStep.get().getSeverity());
            reasons.setDescription(reasonsStep.get().getDescription());
        }

        extentTest.log(Status.INFO, MarkupHelper.createLabel(reasons.toString(), ExtentColor.RED));
        reportTestRepository().save(reasons);
        mongoInstance.deleteElement(Filters.eq("_id", testId));
        mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Description.class).value();
        String testId = methodTestName.replaceAll(SAVE_NUMERIC_CHARS,"");

        Reasons reasons = new Reasons(Status.SKIP, testId, methodTestName, TestCategory.NONE, TestSeverity.NONE, methodTestName + " is skipped");
        Optional<ReasonsStep> reasonsStep = reportStepRepository().findObjectById(testId);
        if (reasonsStep.isPresent()) {
            reasons.setCategory(reasonsStep.get().getCategory());
            reasons.setSeverity(reasonsStep.get().getSeverity());
            reasons.setSeverity(reasonsStep.get().getSeverity());
            reasons.setDescription(reasonsStep.get().getDescription());
        }

        extentTest.log(Status.INFO, MarkupHelper.createLabel(reasons.toString(), ExtentColor.RED));
        reportTestRepository().save(reasons);
        mongoInstance.deleteElement(Filters.eq("_id", testId));
        mongoInstance.insertElement(ReasonsDtoAdapter.toDocument(reasons));
    }

    //after all
    @Override
    public void onFinish(ITestContext context) {
        ExtentSparkReporter sparkFailReporter = new ExtentSparkReporter("target/SparkFail.html").filter().statusFilter().as(new Status[] {Status.FAIL}).apply();
        ExtentSparkReporter sparkSkipReporter = new ExtentSparkReporter("target/SparkSkip.html").filter().statusFilter().as(new Status[] {Status.SKIP}).apply();
        ExtentSparkReporter sparkPassReporter = new ExtentSparkReporter("target/SparkPass.html").filter().statusFilter().as(new Status[] {Status.PASS}).apply();
        extentReports.attachReporter(sparkFailReporter);
        extentReports.attachReporter(sparkSkipReporter);
        extentReports.attachReporter(sparkPassReporter);
        extentReports.flush();
    }

    private void loadReportConfiguration(String date, String path) {
        try {
            sparkReporter.loadJSONConfig(new File(path));
        } catch (IOException ioException) {
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setReportName("Test Report");
            sparkReporter.config().setDocumentTitle("Test report " + date);
        }
    }
}
