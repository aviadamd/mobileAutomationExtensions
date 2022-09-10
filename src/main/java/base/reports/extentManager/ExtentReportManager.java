package base.reports.extentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExtentReportManager {

    private ExtentTest extentTest;
    private boolean reOrderReportCategories;
    private ExtentSparkReporter sparkReporter;
    private List<ViewName> viewNamesArray;
    private final ExtentReports extentReports;

    public ExtentTest getExtentTest() {
        return this.extentTest;
    }

    public ExtentReportManager() {
        this.extentReports = new ExtentReports();
    }

    public ExtentReportManager setViewReportCategoriesOrder(List<ViewName> viewNamesArray) {
        this.viewNamesArray = viewNamesArray;
        return this;
    }

    public ExtentReportManager setDeviceName(String deviceName) {
        this.extentTest.assignDevice(deviceName);
        return this;
    }

    public ExtentReportManager setReportConfiguration(String date, String path) {
        try {
            sparkReporter.loadJSONConfig(new File(path));
        } catch (IOException ioException) {
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setReportName("Test Report");
            sparkReporter.config().setDocumentTitle("Test report " + date);
        }
        return this;
    }

    public ExtentReportManager setReOrderCategories(boolean reOrderCategories) {
        this.reOrderReportCategories = reOrderCategories;
        return this;
    }

    public ExtentReportManager createTest(String testName) {
        this.extentTest = extentReports.createTest(testName);
        return this;
    }

    public ExtentReportManager initSparkReporter(String reportPath) {
        this.sparkReporter = new ExtentSparkReporter(reportPath);
        if (this.reOrderReportCategories) {
            this.sparkReporter
                    .viewConfigurer()
                    .viewOrder()
                    .as(this.viewNamesArray)
                    .apply();
        }
        this.extentReports.attachReporter(this.sparkReporter);
        return this;
    }
}
