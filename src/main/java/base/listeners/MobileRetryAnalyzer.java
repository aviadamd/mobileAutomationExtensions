package base.listeners;

import base.driversManager.MobileManager;
import base.reports.testFilters.ReasonsStep;
import base.reports.ReportStepRepository;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.List;

import static base.listeners.MobileListenerManger.getOnFailAnnotation;

@Slf4j
public class MobileRetryAnalyzer extends MobileManager implements IRetryAnalyzer {
    int counter = 1;
    @Override
    public boolean retry(ITestResult iTestResult) {
        if (getOnFailAnnotation(iTestResult).isPresent()) {
            OnFailRetry failRetry = getOnFailAnnotation(iTestResult).get();
            if (failRetry.value() != 0) {
                List<ReasonsStep> reasonsList = ReportStepRepository.getInstance().getAllObjects();
                reasonsList.forEach(e -> log.info(e.toString()));
                if (counter <= failRetry.value()) {
                    counter++;
                    for (ReasonsStep reasons : reasonsList) {
                        if (reasons.getStatus().toString().equals(Status.FAIL.toString()) || reasons.getStatus().toString().equals(Status.SKIP.toString())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
