package base.reports.extentManager;

import base.MobileWebDriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ExtentReportManager extends MobileWebDriverManager {

    public static ExtentTest extentTest;
    public static ExtentColor extentFailColor = ExtentColor.RED;
    public static ExtentColor extentSkipColor = ExtentColor.GREEN;
    public static ExtentColor extentPassColor = ExtentColor.BLUE;
    private static ExtentReports extentReports;
    private static ExtentSparkReporter sparkReporter;

    private static List<ViewName> setViewNames = new ArrayList<>(Arrays.asList(
            ViewName.DASHBOARD,
            ViewName.TEST,
            ViewName.AUTHOR,
            ViewName.DEVICE,
            ViewName.EXCEPTION,
            ViewName.LOG
    ));

    public static void setReportOrder(List<ViewName> viewNames) {
        setViewNames = new ArrayList<>();
        setViewNames.addAll(viewNames);
    }

    public static void initReports() {
        extentReports = new ExtentReports();
        sparkReporter = new ExtentSparkReporter(getProperty().getExtentSparkPath())
                .viewConfigurer()
                .viewOrder()
                .as(setViewNames)
                .apply();
        extentReports.attachReporter(sparkReporter);
        try {
            sparkReporter.loadJSONConfig(new File(getProperty().getReportJsonTemplatePath()));
        } catch (IOException ioException) {
            log.error("loadReportConfiguration error " +ioException.getMessage());
        }
    }

    public static void createTest(String testName) {
        extentTest = extentReports.createTest(testName);
        extentTest.assignDevice(getProperty().getDeviceName());
    }

    public static void flushReports() {
        extentReports.flush();
    }

    public static void setExtraReports(String reportLocation, Status statusBy) {
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportLocation)
                .filter()
                .statusFilter()
                .as(new Status[]{statusBy})
                .apply();
        extentReports.attachReporter(reporter);
    }

    public static String screenshot(WebDriver driver) {
        if (driver != null) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            String source = ts.getScreenshotAs(OutputType.BASE64);
            return "data:image/jpg;base64, " + source;
        }
        return "";
    }

    public static Markup createExpend(String titleExpend, String bodyDesc, ExtentColor extentColor) {
        return MarkupHelper.createLabel(
                "<details>"
                        + "<summary>"
                        + "<b>"
                        + "<font color="
                        + "red>"
                        + ""+titleExpend+" :click to open details"
                        + "</font>"
                        + "</b> \n"
                        + "</summary>"
                        + bodyDesc.replace(",", "<br>")
                        + "</details> \n"
                        + "</summary>",
                extentColor
        );
    }
}
