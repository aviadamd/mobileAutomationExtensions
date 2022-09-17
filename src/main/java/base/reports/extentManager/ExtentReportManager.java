package base.reports.extentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ThreadLocal<ExtentReports> extentReports = new ThreadLocal<>();
    private static final ThreadLocal<ExtentSparkReporter> sparkReporter = new ThreadLocal<>();

    static ThreadLocal<ExtentTest> getExtentTest() { return extentTest;}
    static void setExtentTest(ExtentTest test) { extentTest.set(test); }
    static void unloadExtentTest() { extentTest.remove(); }

    static ThreadLocal<ExtentReports> getExtentReports() { return extentReports; }
    static void setExtentReports(ExtentReports reports) { extentReports.set(reports); }
    static void unloadExtentReport() { extentReports.remove(); }

}
