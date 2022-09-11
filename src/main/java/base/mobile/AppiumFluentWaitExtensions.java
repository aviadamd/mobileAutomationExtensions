package base.mobile;

import base.MobileWebDriverManager;
import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumFluentWait;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Sleeper;
import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import static java.util.Arrays.asList;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@Slf4j
public class AppiumFluentWaitExtensions extends MobileWebDriverManager {

    private final Clock clock;
    private final Sleeper sleeper;
    private Duration pollingEvery;
    private Duration withPollingStrategy;
    private Status status = Status.FAIL;

    public AppiumFluentWait<WebDriver> appiumFluentWait() {
        AppiumFluentWait<WebDriver> appiumFluentWait = new AppiumFluentWait<>(getDriver());
        appiumFluentWait
                .withTimeout(this.withPollingStrategy)
                .pollingEvery(this.pollingEvery)
                .ignoreAll(this.errors);
        return appiumFluentWait;
    }

    private ArrayList<Class<? extends Exception>> errors = new ArrayList<>(asList(
            Exception.class,
            WebDriverException.class,
            TimeoutException.class,
            java.util.concurrent.TimeoutException.class,
            ConcurrentModificationException.class
    ));

    public AppiumFluentWaitExtensions() {
        this.pollingEvery = Duration.ofMillis(500);
        this.withPollingStrategy = Duration.ofSeconds(2);
        this.clock = Clock.systemDefaultZone();
        this.sleeper = duration -> this.sleepSeconds(1);
    }

    public AppiumFluentWaitExtensions(Clock clock, Sleeper sleeper) {
        this.pollingEvery = Duration.ofMillis(500);
        this.withPollingStrategy = Duration.ofSeconds(2);
        this.clock = clock;
        this.sleeper = sleeper;
    }

    public AppiumFluentWaitExtensions setLogStatus(Status status) {
        this.status = status;
        return this;
    }

    public AppiumFluentWaitExtensions pollingEvery(Duration pollingEvery) {
        this.pollingEvery = pollingEvery;
        return this;
    }

    public AppiumFluentWaitExtensions withGeneralPollingWaitStrategy(Duration withPollingStrategy) {
        this.withPollingStrategy = withPollingStrategy;
        return this;
    }

    public AppiumFluentWaitExtensions setIgnoreClasses(ArrayList<Class<? extends Exception>> errors) {
        this.errors = errors;
        return this;
    }

    /**
     * time out is sync in automation test
     * so until page is loaded with with element to be clickable or or visible
     * @param element single element
     * @return true/false if get the element
     */
    public boolean isGetElement(WebElement element, String desc) {
        String eleName = "", error = "";
        try {
            eleName = element.toString() != null ? element.toString() : "";
            appiumFluentWait().until(elementToBeClickable(element));
            log.info("isGetPageWith: " + eleName + ", element name. with description " + desc + " pass ");
            return true;
        } catch (RuntimeException rtEx) {
            error = "RuntimeException";
        } catch (Exception ex) {
            if (ex instanceof ConcurrentModificationException) {
                error = "ConcurrentModificationException";
            } else if(ex instanceof NullPointerException) {
                error = "NullPointerException";
            } else error = "Exception";
        } finally {
            if (!error.isEmpty()) {
                this.errorTable(error, eleName);
            }
        }

        return false;
    }

    /**
     * time out is sync in automation test
     * so until page is loaded with with element to be clickable or or visible
     * @param element single element
     * @return true/false if get the element
     */
    public boolean isGetElement(By element, String desc) {
        String eleName = "", error = "";
        try {
            eleName = element.toString() != null ? element.toString() : "";
            appiumFluentWait().until(elementToBeClickable(element));
            log.info("isGetPageWith: " + eleName + ", element name. with description " + desc + " pass ");
            return true;
        } catch (RuntimeException rtEx) {
            error = "RuntimeException";
        } catch (Exception ex) {
            if (ex instanceof ConcurrentModificationException) {
                error = "ConcurrentModificationException";
            } else if(ex instanceof NullPointerException) {
                error = "NullPointerException";
            } else error = "Exception";
        } finally {
            if (!error.isEmpty()) {
                this.errorTable(error, eleName);
            }
        }

        return false;
    }

    private void errorTable(String eleName, String desc) {
        final String logPrint = desc + " wait to "+ eleName + " fail";
        if (this.status == Status.FAIL || this.status == Status.SKIP) {
           reportStepTest(new ReasonsStep(this.status,"","", TestCategory.NONE, TestSeverity.NONE, logPrint));
        } else log.debug(desc + logPrint);
    }
}
