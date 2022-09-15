package base.reports.extentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentReportManager {
    private ExtentReportManager() {}
    private final static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private final static ThreadLocal<ExtentReports> extentReports = new ThreadLocal<>();
    static ExtentTest getExtentTest() { return extentTest.get();}
    static void setExtentTest(ExtentTest test) { extentTest.set(test); }
    static void unloadExtentTest() { extentTest.remove(); }

    static ExtentReports getExtentReports() { return extentReports.get(); }
    static void setExtentReports(ExtentReports reports) { extentReports.set(reports); }
    static void unloadExtentReport() { extentReports.remove(); }

}
