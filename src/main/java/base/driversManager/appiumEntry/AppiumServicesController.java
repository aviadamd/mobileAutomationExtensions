package base.driversManager.appiumEntry;

import base.MobileWebDriverManager;
import base.reports.testFilters.Reasons;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;

@Slf4j
public class AppiumServicesController extends MobileWebDriverManager {

    public static String aOkCallback = "aOkCallback";
    public static String okHttp = "OkHttp";

    public static String unauthorized = "401 Unauthorized";
    public static String notFound = "404 Not Found";
    public static String methodNotAllowed = "405 Method Not Allowed";
    public static String requestTimeout = "408 Request Timeout";
    public static String badGateway = "502 Bad Gateway";
    public static String gatewayTimeout = "504 Gateway Timeout";

    public static String caa_aOkInterceptor = "caa-aOkInterceptor";
    public static String activityManager = "ActivityManager";
    public static String activityTaskManager = "ActivityTaskManager";
    public static String androidRunTime = "AndroidRuntime";
    public static String networkMonitor = "NetworkMonitor";
    public static String artManagerInternalImpl = "ArtManagerInternalImpl";
    public static String mediaProvider = "MediaProvider";
    public static String serviceManager = "ServiceManager";
    public static String systemServiceManager = "SystemServiceManager";
    public static String packageManager = "PackageManager";
    public static String fatalException = "FATAL EXCEPTION";

    public static String w3C = "W3C";
    public static String baseDriver = "BaseDriver";
    public static String wDProxy = "WD Proxy";

    private List<String> collector = new ArrayList<>();
    private List<LogEntry> entries = new ArrayList<>();
    public List<LogEntry> getEntries() { return entries; }

    public List<String> getCollector() {
        return collector;
    }

    /**
     * @param getMainEntryLogBy
     *   BROWSER("browser"),
     *   CLIENT("client"),
     *   DRIVER("driver"),
     *   PERFORMANCE("performance"),
     *   PROFILER("profiler"),
     *   SERVER("server"),
     *   LOG_CAT_OR_SYS_LOG(isAndroidClient() ? "logcat" : "syslog");
     * @return this and than you can call getCollector();
     */
    public AppiumServicesController getMainEntryLog(LogTypeBy.GetMainEntryLogBy getMainEntryLogBy) {
        return getMainEntryLog(false, getMainEntryLogBy);
    }

    /**
     * @param getMainEntryLogBy
     *   BROWSER("browser"),
     *   CLIENT("client"),
     *   DRIVER("driver"),
     *   PERFORMANCE("performance"),
     *   PROFILER("profiler"),
     *   SERVER("server"),
     *   LOG_CAT_OR_SYS_LOG(isAndroidClient() ? "logcat" : "syslog");
     * @return this and than you can call getCollector();
     */
    public AppiumServicesController getMainEntryLog(
            boolean cleanCollectorMessages,
            LogTypeBy.GetMainEntryLogBy getMainEntryLogBy) {

        if (cleanCollectorMessages) {
            this.collector = new ArrayList<>();
        }

        if (this.getDriver() != null) {
            this.collector = this.logEntries(getMainEntryLogBy)
                    .getAll()
                    .stream()
                    .filter(e -> !e.getMessage().isEmpty())
                    .map(e -> e.toJson().toString())
                    .collect(toList());
            this.entries = this.logEntries(getMainEntryLogBy).getAll();
        } else reportTest(new Reasons(Status.FAIL, "","", TestCategory.NONE, TestSeverity.NONE, ""));
        return this;
    }

    /**
     * @param predicatesBy filter by the str list
     * @return collectLogsBy
     */
    public List<String> filterBy(List<String> predicatesBy) {
        return this.collector
                .stream()
                .filter(predicatesBy::contains)
                .collect(toList());
    }

    /**
     * @param predicateBy filter by the str
     * @return collectLogsBy
     */
    public List<String> filterBy(Predicate<String> predicateBy) {
        return this.collector
                .stream()
                .filter(predicateBy)
                .collect(toList());
    }

    /**
     * @param androidLogCatTags filter by the str
     * @return collectLogsBy
     */
    public List<String> filterBy(LogTypeBy.AndroidLogCatTags androidLogCatTags) {
        return this.collector
                .stream()
                .filter(tag -> tag.contains(androidLogCatTags.getValue()))
                .collect(toList());
    }

    /**
     * @param iosSysLogTags filter by the str
     * @return collectLogsBy
     */
    public List<String> filterBy(LogTypeBy.IosSysLogTags iosSysLogTags) {
        return this.collector
                .stream()
                .filter(tag -> tag.contains(iosSysLogTags.getValue()))
                .collect(toList());
    }

    /**
     * @param getMainEntryLogBy
     *   BROWSER("browser"),
     *   CLIENT("client"),
     *   DRIVER("driver"),
     *   PERFORMANCE("performance"),
     *   PROFILER("profiler"),
     *   SERVER("server"),
     *   LOG_CAT_OR_SYS_LOG(isAndroidClient() ? "logcat" : "syslog");
     * @return logEntries
     */
    public LogEntries logEntries(LogTypeBy.GetMainEntryLogBy getMainEntryLogBy) {
        return getDriver()
                .manage()
                .logs()
                .get(getMainEntryLogBy.getValue());
    }

    /*** @return true/false */
    public boolean checkServiceErrors(String error) {
        return error.contains("408 Request Timeout")
                || error.contains("500 Internal Server Error")
                || error.contains("400 Bad Request")
                || error.contains("401 Unauthorized")
                || error.contains("403 Forbidden")
                || error.contains("404 Not Found")
                || error.contains("405 Method Not Allowed")
                || error.contains("502 Bad Gateway")
                || error.contains("504 Gateway Timeout")
                || error.contains("204 No Content");
    }

    /*** @return true/false */
    public Predicate<String> checkServiceErrors() {
        return by -> by.contains("400 Bad Request")
                || by.contains("401 Unauthorized")
                || by.contains("404 Not Found")
                || by.contains("403 Forbidden")
                || by.contains("405 Method Not Allowed")
                || by.contains("408 Request Timeout")
                || by.contains("500 Internal Server Error")
                || by.contains("502 Bad Gateway")
                || by.contains("504 Gateway Timeout")
                || by.contains("204 No Content");
    }

    /*** @return true/false */
    public Predicate<String> checkServiceOk() {
        return by -> by.contains("200 OK")
                || by.contains("201 Created")
                || by.contains("202 Accepted")
                || by.contains("204 No Content")
                || by.contains("301 Moved Permanently")
                || by.contains("304 Not Modified");
    }

    /*** @return filterByAppiumErrors */
    public Predicate<String> filterByAppiumErrors() {
        return name -> name.contains("appium")
                && name.contains("AppiumResponse")
                && name.contains("common.exceptions.ElementNotFoundException")
                || name.contains("Got response with status 404");
    }

    /*** @return filterByAppiumErrors */
    public Predicate<String> filterByAppium() {
        return name -> name.contains("appium")
                || name.contains("AppiumResponse")
                || name.contains("common.exceptions.ElementNotFoundException")
                || name.contains("Got response with status 404")
                || name.contains("WebDriverAgent");
    }

    /*** @return filterByAppiumErrors */
    public Predicate<String> filterByActivity() {
        return name -> name.contains(fatalException)
                || name.contains(activityManager)
                || name.contains(activityTaskManager)
                || name.contains(androidRunTime)
                || name.contains(networkMonitor)
                || name.contains(artManagerInternalImpl)
                || name.contains(mediaProvider)
                || name.contains(serviceManager)
                || name.contains(systemServiceManager)
                || name.contains(packageManager);
    }

    /*** @return filterByAppiumErrors */
    public Predicate<String> filterByActivityApplication() {
        return name -> name.contains(fatalException)
                || name.contains(activityManager)
                || name.contains(activityTaskManager)
                || name.contains(androidRunTime)
                || name.contains(networkMonitor)
                || name.contains(artManagerInternalImpl)
                || name.contains(mediaProvider)
                || name.contains(serviceManager)
                || name.contains(systemServiceManager)
                || name.contains(packageManager)
                && name.contains(System.getProperty("app.name.package"));
    }

    /**
     * generateLogEntryDto
     * @param stringsList run on strings list and generate the List<LogEntryDto>
     * @param nameKey for the  String logType,String logMessages;
     * @return List<LogEntryDto> or empty list
     */
    public List<LogEntryDto> generateLogEntryDto(List<String> stringsList, String nameKey) {
        List<LogEntryDto> logListDto = new ArrayList<>();

        try {
            if (stringsList != null && !stringsList.isEmpty()) {
                for (String name : stringsList) {
                    logListDto.add(new LogEntryDto(nameKey, name));
                }
                return logListDto;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }

        return new ArrayList<>();
    }
}
