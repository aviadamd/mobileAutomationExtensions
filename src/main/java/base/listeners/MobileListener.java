 package base.listeners;

import base.anontations.TestTarget;
import base.driversManager.MobileManager;
import base.reports.extentManager.ExtentLogger;
import base.repository.MongoConnection;
import base.repository.MongoExtensionsManager;
import base.repository.ReportTestRepository;
import base.repository.mongo.adapters.ReasonsDtoAdapter;
import base.repository.mongo.notReactive.MongoCollectionRepoImpl;
import base.reports.testFilters.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import org.testng.*;
import org.testng.annotations.Test;
import java.util.*;

@SuppressWarnings("deprecation")
public class MobileListener extends MobileManager implements ITestListener {

    //before all
    @Override
    public void onStart(ITestContext context) {
        ExtentLogger.initReports(getProperty().getExtentSparkPath(), getProperty().getReportJsonTemplatePath());
        String testCollectionName = getProperty().getMongoDbCollectionName() + "_" + UUID.randomUUID();
        MongoExtensionsManager.setMongoInstance(new MongoCollectionRepoImpl(new MongoConnection(
                getProperty().getMongoDbStringConnection(),
                getProperty().getMongoDbName()),
                testCollectionName)
        );
    }

    //before test
    @Override
    public void onTestStart(ITestResult iTestResult) {
        String testId = iTestResult.getMethod().getMethod().getAnnotation(TestTarget.class).testId();
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Test.class).description();
        ExtentLogger.createTest(methodTestName, getProperty().getDeviceName());
        Reasons reasons = MobileListenerManger.insertTestStartData(testId, iTestResult, methodTestName);
        MongoExtensionsManager.getMongoInstance().insertElement(ReasonsDtoAdapter.toDocument(reasons));
        ReportTestRepository.getInstance().save(reasons);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Test.class).description();
        String testId = iTestResult.getMethod().getMethod().getAnnotation(TestTarget.class).testId();
        Reasons reasons = MobileListenerManger.insertTestPassData(testId, methodTestName, screenshot());
        MobileListenerManger.updateTestStatus(reasons, testId, methodTestName, Status.PASS);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Test.class).description();
        String testId = iTestResult.getMethod().getMethod().getAnnotation(TestTarget.class).testId();
        Reasons reasons = MobileListenerManger.insertTestFailData(testId, iTestResult, methodTestName, screenshot(), Status.FAIL, ExtentColor.BLUE);
        MobileListenerManger.updateTestStatus(reasons, testId, methodTestName, Status.FAIL);
        ExtentLogger.loggerPrint(Status.FAIL, reasons.toString());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Test.class).description();
        String testId = iTestResult.getMethod().getMethod().getAnnotation(TestTarget.class).testId();
        Reasons reasons = MobileListenerManger.insertTestFailData(testId, iTestResult, methodTestName, screenshot(), Status.SKIP, ExtentColor.ORANGE);
        MobileListenerManger.updateTestStatus(reasons, testId, methodTestName, Status.SKIP);
        ExtentLogger.loggerPrint(Status.SKIP, reasons.toString());
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
        MongoExtensionsManager.getMongoInstance().close();
    }
}
