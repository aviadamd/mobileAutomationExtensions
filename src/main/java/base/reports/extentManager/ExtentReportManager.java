package base.reports.extentManager;

import base.MobileWebDriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ExtentReportManager extends MobileWebDriverManager {

    public static ExtentTest extentTest;
    private static ExtentReports extentReports;
    private static ExtentSparkReporter sparkReporter;

    public static void initReports() {
        extentReports = new ExtentReports();
        sparkReporter = new ExtentSparkReporter(getProperty().getExtentSparkPath())
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();
        extentReports.attachReporter(sparkReporter);
        loadReportConfiguration(getProperty().getReportJsonTemplatePath());
    }

    public static void createTest(String testName) {
        extentTest = extentReports.createTest(testName);
        extentTest.assignDevice(getProperty().getDeviceName());
    }

    public static void flushReports() {
        ExtentSparkReporter sparkFailReporter = new ExtentSparkReporter("target/SparkFail.html").filter().statusFilter().as(new Status[]{Status.FAIL}).apply();
        ExtentSparkReporter sparkSkipReporter = new ExtentSparkReporter("target/SparkSkip.html").filter().statusFilter().as(new Status[]{Status.SKIP}).apply();
        ExtentSparkReporter sparkPassReporter = new ExtentSparkReporter("target/SparkPass.html").filter().statusFilter().as(new Status[]{Status.PASS}).apply();
        extentReports.attachReporter(sparkFailReporter);
        extentReports.attachReporter(sparkSkipReporter);
        extentReports.attachReporter(sparkPassReporter);
        extentReports.flush();
    }

    public static void screenScreenShot(WebDriver driver, Status status) {
        if (driver != null) {
            try {
                String path = "target/screenShots/image.png";
                File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(source, new File("target/screenShots/image.png"));
                extentTest.log(status, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private static void loadReportConfiguration(String path) {
        try {
            sparkReporter.loadJSONConfig(new File(path));
        } catch (IOException ioException) {
           //
        }
    }

}
