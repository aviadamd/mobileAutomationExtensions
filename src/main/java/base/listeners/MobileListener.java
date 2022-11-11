package base.listeners;

import base.CreateReportExtra;
import base.anontations.TestTarget;
import base.driversManager.MobileManager;
import base.reports.extentManager.ExtentLogger;
import base.repository.MongoConnection;
import base.repository.MongoExtensionsManager;
import base.reports.ReportStepRepository;
import base.reports.ReportTestRepository;
import base.repository.mongo.adapters.ReasonsDtoAdapter;
import base.repository.mongo.notReactive.MongoCollectionRepoImpl;
import base.reports.testFilters.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.*;
import org.testng.annotations.Test;
import java.util.*;

@Slf4j
@SuppressWarnings("deprecation")
public class MobileListener extends MobileManager implements ITestListener {
    private static final List<TestCategory> filterByList = new ArrayList<>();
    private static Pair<Boolean, Status[]> reportStatus = Pair.of(false, new Status[]{Status.FAIL});

    //before all
    @Override
    public void onStart(ITestContext context) {
        ExtentLogger.initReports(getProperty().getExtentSparkPath(), getProperty().getReportJsonTemplatePath());
        String testCollectionName = getProperty().getMongoDbCollectionName() + "_" + UUID.randomUUID();
        MongoExtensionsManager.setMongoInstance(new MongoCollectionRepoImpl(new MongoConnection(getProperty().getMongoDbStringConnection(), getProperty().getMongoDbName()), testCollectionName));
    }

    //before test
    @Override
    public void onTestStart(ITestResult iTestResult) {
        addCreateReportExtra(iTestResult);
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
        MobileListenerManger.updateMongoTestStatus(reasons, testId, methodTestName, Status.PASS);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Test.class).description();
        String testId = iTestResult.getMethod().getMethod().getAnnotation(TestTarget.class).testId();
        Reasons reasons = MobileListenerManger.insertTestFailData(testId, iTestResult, methodTestName, screenshot(), Status.FAIL, ExtentColor.BLUE);
        MobileListenerManger.updateMongoTestStatus(reasons, testId, methodTestName, Status.FAIL);
        ExtentLogger.loggerPrint(Status.FAIL, reasons.toString());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Test.class).description();
        String testId = iTestResult.getMethod().getMethod().getAnnotation(TestTarget.class).testId();
        Reasons reasons = MobileListenerManger.insertTestFailData(testId, iTestResult, methodTestName, screenshot(), Status.SKIP, ExtentColor.ORANGE);
        MobileListenerManger.updateMongoTestStatus(reasons, testId, methodTestName, Status.SKIP);
        ExtentLogger.loggerPrint(Status.SKIP, reasons.toString());
    }
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        String methodTestName = iTestResult.getMethod().getMethod().getAnnotation(Test.class).description();
        String testId = iTestResult.getMethod().getMethod().getAnnotation(TestTarget.class).testId();
        Reasons reasons = MobileListenerManger.insertTestFailData(testId, iTestResult, methodTestName, screenshot(), Status.FAIL, ExtentColor.BLUE);
        MobileListenerManger.updateMongoTestStatus(reasons, testId, methodTestName, Status.FAIL);
        ExtentLogger.loggerPrint(Status.FAIL, reasons.toString());
    }

    //after all
    @Override
    public void onFinish(ITestContext context) {
        Set<ITestResult> failTests = context.getFailedTests().getAllResults();
        Set<ITestResult> passTests = context.getPassedTests().getAllResults();
        Set<ITestResult> skipTests = context.getSkippedTests().getAllResults();
        log.info("pass tests:" + passTests.size() + ", fail tests: " + failTests.size() + ", skip tests: " + skipTests.size());
        ExtentLogger.setExtraReports("target", reportStatus.getRight());
        ExtentLogger.assignTestCategory(filterByList);
        ExtentLogger.flushReports();
        ReportStepRepository.getInstance().deleteAll();
        ReportTestRepository.getInstance().deleteAll();
        MongoExtensionsManager.getMongoInstance().close();
    }
    private static void addCreateReportExtra(ITestResult iTestResult) {
        if (!reportStatus.getLeft()) {
            Optional.ofNullable(iTestResult.getTestClass().getRealClass().getAnnotation(CreateReportExtra.class)).ifPresent(reportExtra -> {
                reportStatus = Pair.of(true, reportExtra.reportStatus());
                if (reportExtra.filterBy().length != 0) {
                    for (TestCategory filterBy : reportExtra.filterBy()) {
                        if (filterBy == TestCategory.NONE) break;
                        filterByList.add(filterBy);
                    }
                }
            });
        }
    }
}
