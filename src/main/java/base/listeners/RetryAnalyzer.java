package base.listeners;

import base.anontations.TestTarget;
import base.driversManager.MobileManager;
import base.reports.testFilters.Reasons;
import base.reports.testFilters.TestCategory;
import base.repository.ReportTestRepository;
import com.aventstack.extentreports.Status;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.List;

public class RetryAnalyzer extends MobileManager implements IRetryAnalyzer {
    int counter = 0, retryLimit = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        String testId = iTestResult.getMethod().getMethod().getAnnotation(TestTarget.class).testId();
        List<Reasons> reasonsList = ReportTestRepository.getInstance().getAllObjects();
        if (counter < retryLimit) {
            for (Reasons reasons : reasonsList) {
                if (reasons.getTestId().equalsIgnoreCase(testId)
                        && reasons.getTestStatus().equals(Status.FAIL)
                        && reasons.getCategory().equals(TestCategory.DRIVER)) {
                    return true;
                }
            }
            counter++;
        }
        //counter++;

        //return value;
        return false;
    }
}
