package base.reports.extentManager;

import base.anontations.CategoryType;
import base.reports.ReportStepRepository;
import base.reports.ReportTestRepository;
import base.reports.testFilters.Reasons;
import base.reports.testFilters.ReasonsStep;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
public final class ExtentLogger {

    public static void reportTest(Reasons reportTestDto) {
        ReportTestRepository.getInstance().save(reportTestDto);
        ExtentLogger.loggerPrint(reportTestDto.getTestStatus(), "report description: " + reportTestDto.getDescription());
        if (reportTestDto.getTestStatus() == Status.FAIL || reportTestDto.getTestStatus() == Status.SKIP) {
            Assert.fail(reportTestDto.getTestStatus().toString() + "," + reportTestDto);
        }
    }

    public static void reportTest(Reasons reportTestDto, CodeLanguage codeLanguage) {
        ReportTestRepository.getInstance().save(reportTestDto);
        ExtentLogger.loggerPrint(reportTestDto.getTestStatus(), MarkupHelper.createCodeBlock(reportTestDto.getDescription(), codeLanguage));
        if (reportTestDto.getTestStatus() == Status.FAIL || reportTestDto.getTestStatus() == Status.SKIP) {
            Assert.fail(reportTestDto.getTestStatus().toString() + "," + reportTestDto);
        }
    }

    public static void reportStepTest(ReasonsStep reportStepDto) {
        ReportStepRepository.getInstance().save(reportStepDto);
        String step = reportStepDto.getStepId();
        String desc = reportStepDto.getDescription();
        ExtentLogger.loggerPrint(reportStepDto.getStatus(), step + " " + desc);
        if (reportStepDto.getStatus() == Status.FAIL || reportStepDto.getStatus() == Status.SKIP) {
            Assert.fail(reportStepDto.getStatus().toString() + " , " + reportStepDto);
        }
    }
    public static void reportStepTest(ReasonsStep reportStepDto, CodeLanguage codeLanguage) {
        ReportStepRepository.getInstance().save(reportStepDto);
        ExtentLogger.loggerPrint(reportStepDto.getStatus(), MarkupHelper.createCodeBlock(reportStepDto.getDescription(), codeLanguage));
        if (reportStepDto.getStatus() == Status.FAIL || reportStepDto.getStatus() == Status.SKIP) {
            Assert.fail(reportStepDto.getStatus().toString() + " , " + reportStepDto);
        }
    }

    public static void initReports(String extentSparkPath, String jsonTemplate) {
        ExtentReportManager.setExtentReports(new ExtentReports());
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(extentSparkPath);
        ExtentReportManager.getExtentReports().get().attachReporter(sparkReporter);
        try {
            sparkReporter.loadJSONConfig(new File(jsonTemplate));
        } catch (IOException ioException) {
            log.error("loadReportConfiguration error " +ioException.getMessage());
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("TestReport");
        }
    }

    public static void createTest(String testCase, String deviceName) {
        ExtentReportManager.setExtentTest(ExtentReportManager.getExtentReports().get().createTest(testCase));
        ExtentReportManager.getExtentTest().get().assignDevice(deviceName);
    }

    public static void loggerPrint(Status status, String description) {
        ExtentReportManager.getExtentTest().get().log(status, description);
    }

    public static void loggerPrint(Status status, Markup markup) {
        ExtentReportManager.getExtentTest().get().log(status, markup);
    }

    public static void loggerPrint(Status status, Media media) {
        ExtentReportManager.getExtentTest().get().log(status, media);
    }
    public static void flushReports() {
        ExtentReportManager.getExtentReports().get().flush();
        ExtentReportManager.getExtentTest().remove();
    }
    public static void setExtraReports(Map<String,Status> statusReport) {
        statusReport.forEach((fileLocation, status) -> {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileLocation);
            sparkReporter
                    .filter()
                    .statusFilter()
                    .as(new Status[]{status})
                    .apply();
            ExtentReportManager.getExtentReports().get().attachReporter(sparkReporter);
        });
    }
    public static void setExtraReports(String reportLocation, Status statusBy) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportLocation);
        sparkReporter.filter()
                .statusFilter()
                .as(new Status[]{statusBy})
                .apply();
        ExtentReportManager.getExtentReports().get().attachReporter(sparkReporter);
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
    public static void addAuthors(String[] authors) {
        for (String author : authors) {
            ExtentReportManager.getExtentTest().get().assignAuthor(author);
        }
    }
    public static void addCategories(CategoryType[] categories) {
        for (CategoryType category : categories) {
            ExtentReportManager.getExtentTest().get().assignCategory(category.toString());
        }
    }
    public static void addSuite(String suite) {
        ExtentReportManager.getExtentTest().get().assignCategory(suite);
    }
}
