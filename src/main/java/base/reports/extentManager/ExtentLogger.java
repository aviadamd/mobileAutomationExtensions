package base.reports.extentManager;

import base.anontations.CategoryType;
import base.reports.ReportStepRepository;
import base.reports.ReportTestRepository;
import base.reports.testFilters.Reasons;
import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public final class ExtentLogger {

    public static void reportTest(Status status, String desc) {
        reportTest(new Reasons(status,"","", TestCategory.NONE, TestSeverity.NONE, desc));
    }
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
        ExtentReportManager.unloadExtentReport();
        ExtentReportManager.unloadExtentTest();
    }
    public static void setExtraReports(Map<String,Status> statusReport) {
        Optional.ofNullable(statusReport).ifPresent(stringStatusMap -> {
            stringStatusMap.forEach((fileLocation, status) -> {
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileLocation);
                sparkReporter
                        .filter()
                        .statusFilter()
                        .as(new Status[]{status})
                        .apply();
                ExtentReportManager.getExtentReports().get().attachReporter(sparkReporter);
            });
        });
    }

    public static void setExtraReports(String reportLocation, TestCategory testCategory) {
        if (Optional.ofNullable(reportLocation).isPresent()) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportLocation + "/" + testCategory.getText() + ".html");
            ExtentReportManager.getExtentReports().get().attachReporter(sparkReporter);
        }
    }

    public static void setExtraReports(String reportLocation, Status[] statusBy) {
        if (Optional.ofNullable(reportLocation).isPresent()) {
            Optional.ofNullable(statusBy).ifPresent(statuses -> {
                for (Status status : statuses) {
                    ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportLocation + "/" + status.toString() + ".html");
                    sparkReporter
                            .filter()
                            .statusFilter()
                            .as(new Status[]{status})
                            .apply();
                    ExtentReportManager.getExtentReports().get().attachReporter(sparkReporter);
                }
            });
        }
    }

    public static void assignTestCategory(List<TestCategory> filterByList) {
        if (filterByList.size() > 0) {
            Reasons reasons = ReportTestRepository.getInstance().getLastObject();
            for (TestCategory testCategory: filterByList) {
                if (reasons.getTestStatus() == Status.FAIL && reasons.getCategory() == testCategory) {
                    ExtentLogger.addCategory(reasons.getCategory().getText());
                }
            }
        }
    }
    public static void setExtraReports(String reportLocation, List<Status> statusBy) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportLocation);
        sparkReporter.filter()
                .statusFilter()
                .as(statusBy)
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
        Optional.ofNullable(authors).ifPresent(authorsE -> {
            for (String author : authorsE) {
                ExtentReportManager.getExtentTest().get().assignAuthor(author);
            }
        });
    }

    public static void addCategory(String testCategory) {
        Optional.ofNullable(testCategory).ifPresent(category -> {
            ExtentReportManager.getExtentTest().get().assignCategory(testCategory);
        });
    }
    public static void addCategories(CategoryType[] categories) {
        Optional.of(categories).ifPresent(categoryTypes -> {
            for (CategoryType category : categoryTypes) {
                ExtentReportManager.getExtentTest().get().assignCategory(category.toString());
            }
        });
    }
    public static void addSuite(String suite) {
        Optional.ofNullable(suite).ifPresent(s -> {
            ExtentReportManager.getExtentTest().get().assignCategory(s);
        });
    }
}
