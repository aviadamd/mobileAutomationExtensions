package base.reports.extentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import static base.driversManager.MobileWebDriverManager.getProperty;

@Slf4j
public final class ExtentLogger {
    public static void createTest(String testCase, String deviceName) {
        ExtentReportManager.setExtentTest(ExtentReportManager.getExtentReports().createTest(testCase));
        ExtentReportManager.getExtentTest().assignDevice(deviceName);
    }

    public static void loggerPrint(Status status, String description) {
        ExtentReportManager.getExtentTest().log(status, description);
    }

    public static void loggerPrint(Status status, Markup markup) {
        ExtentReportManager.getExtentTest().log(status, markup);
    }

    public static void loggerPrint(Status status, Media media) {
        ExtentReportManager.getExtentTest().log(status, media);
    }

    public static void initReports() {
        ExtentReportManager.setExtentReports(new ExtentReports());
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(getProperty().getExtentSparkPath())
                .viewConfigurer()
                .viewOrder()
                .as(new ArrayList<>(Arrays.asList(
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                )))
                .apply();
        ExtentReportManager.getExtentReports().attachReporter(sparkReporter);
        try {
            sparkReporter.loadJSONConfig(new File(getProperty().getReportJsonTemplatePath()));
        } catch (IOException ioException) {
            log.error("loadReportConfiguration error " +ioException.getMessage());
        }
    }
    public static void flushReports() {
        ExtentReportManager.getExtentReports().flush();
    }
    public static void setExtraReports(Map<String,Status> statusReport) {
        statusReport.forEach((fileLocation, status) -> {
            ExtentSparkReporter reporter = new ExtentSparkReporter(fileLocation)
                    .filter()
                    .statusFilter()
                    .as(new Status[]{status})
                    .apply();
            ExtentReportManager.getExtentReports().attachReporter(reporter);
        });
    }
    public static void setExtraReports(String reportLocation, Status statusBy) {
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportLocation)
                .filter()
                .statusFilter()
                .as(new Status[]{statusBy})
                .apply();
        ExtentReportManager.getExtentReports().attachReporter(reporter);
    }

    public static Markup createExpend(String titleExpend, String bodyDesc, ExtentColor extentColor) {
        return MarkupHelper.createLabel(
                "<details>"
                        + "<summary>"
                        + "<b>"
                        + "<font color="
                        + "white>"
                        + ""+titleExpend+": click to open for more details "
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
