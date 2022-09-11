package base.mobile;

import base.MobileWebDriverManager;
import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableList;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import static io.appium.java_client.touch.LongPressOptions.longPressOptions;

@Slf4j
public class ClickElementExtensions extends MobileWebDriverManager {

    private String step = "";
    private int timeOut = 15;
    private Status status = Status.FAIL;
    private boolean isReportFromClass = true;

    public ClickElementExtensions setElementTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public ClickElementExtensions setReportFromClass(boolean isReportFromClass) {
        this.isReportFromClass = isReportFromClass;
        return this;
    }

    public ClickElementExtensions setStep(String step) {
        this.step = step;
        return this;
    }

    public ClickElementExtensions setStatus(Status status) {
        this.status = status;
        return this;
    }

    /**
     * @param timeOut set an time out for flexible scenarios
     * @param by locator the actual element
     */
    public ReasonsStep clickElementBy(int timeOut, By by, String desc) {
        ReasonsStep step;
        try {
            new AppiumFluentWaitExtensions()
                    .withGeneralPollingWaitStrategy(Duration.ofSeconds(timeOut))
                    .pollingEvery(Duration.ofMillis(500))
                    .appiumFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(by))
                    .click();
            step = this.reasonsStep(Status.PASS, this.step, TestCategory.DRIVER, TestSeverity.NONE, desc);
        } catch (Exception exception) {
            step = this.reasonsStep(this.status, this.step, TestCategory.DRIVER, TestSeverity.NONE, desc + " fail " + exception.getMessage());
            if (this.isReportFromClass) {
                this.reportStepTest(step);
            }
        }
        return step;
    }

    /**
     * @param timeOut set an time out for flexible scenarios
     * @param element locator the actual element
     */
    public void clickElement(int timeOut, WebElement element, String desc) {
        try {
            AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
            appiumFluentWaitExtensions.withGeneralPollingWaitStrategy(Duration.ofSeconds(timeOut));
            if (appiumFluentWaitExtensions.isGetElement(element, desc)) {
                element.click();
            }
        } catch (Exception exception) {
            log.error("failed click at element ");
        }
    }

    public void tapElement(int timeOut, WebElement element) {
        try {
            AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
            appiumFluentWaitExtensions.withGeneralPollingWaitStrategy(Duration.ofSeconds(timeOut));
            if (appiumFluentWaitExtensions.isGetElement(element,"")) {
                Point point1 = this.getElementCords(element);
                this.tapAtPoint(point1);
            }
        } catch (Exception e) {
            log.error("failed tap at element");
        }
    }

    public void tapElement(int timeOut, By by) {
        try {
            WebElement element = new ElementsSearchExtensions()
                    .overRideWebDriverWait(Duration.ofSeconds(timeOut), Duration.ofMillis(500))
                    .findElementBy(by);
            Point point1 = this.getElementCords(element);
            this.tapAtPoint(point1);
        } catch (Exception e) {
            log.error("failed tap at element");
        }
    }

    public void tapAtPoint(Point point) {
        try {

            PointerInput input = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
            Sequence tap = new Sequence(input, 0);

            tap.addAction(input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), point.x, point.y));
            tap.addAction(input.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(new Pause(input, Duration.ofMillis(200)));
            tap.addAction(input.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            if (isAndroidClient()) {
                new InfraStructuresExtensions().androidDriver().perform(ImmutableList.of(tap));
            } else new InfraStructuresExtensions().iosDriver().perform(ImmutableList.of(tap));

        } catch (Exception e) {
            log.error("failed tap at element");
        }
    }

    @SuppressWarnings("rawtypes")
    public void clickByCords(WebElement element) {
        try {
            Rectangle rectangle1 = element.getRect();
            int refEleMidX1 = rectangle1.getX();
            int refEleMidY1 = rectangle1.getY();
            TouchAction action = (new TouchAction((AppiumDriver) getDriver()))
                    .press(PointOption.point(refEleMidX1, refEleMidY1))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(50)))
                    .release()
                    .perform();
        } catch (Exception e) {
            log.error("failed clickByCords");
        }
    }

    @SuppressWarnings("rawtypes")
    public void doubleClick(int timeOut, WebElement element) {
        try {
            AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
            appiumFluentWaitExtensions.withGeneralPollingWaitStrategy(Duration.ofSeconds(timeOut));
            if (appiumFluentWaitExtensions.isGetElement(element,"")) {
                Point point = this.getElementCords(element);
                TouchAction action = (new TouchAction((AppiumDriver) getDriver()))
                        .press(PointOption.point(point))
                        .release()
                        .perform()
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(50)))
                        .press(PointOption.point(point))
                        .release()
                        .perform();
            }
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException a) {
            log.error("failed doubleClick at element ");
        }
    }

    private Point getElementCords(WebElement element) {
        Point point = null;
        try {
            Rectangle rect = element.getRect();
            point = new Point(rect.x + (int) (rect.getHeight() / 2.0), rect.y + (int) (rect.getHeight() / 2.0));
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException a) {
            log.error("failed getElementCords");
        }
        return point;
    }

    @SuppressWarnings("rawtypes")
    public void longPressElement(WebElement element, long millis) {
        try {
            TouchAction action = (new TouchAction((AppiumDriver) getDriver()))
                    .longPress(longPressOptions()
                            .withElement(ElementOption.element(element))
                            .withDuration(Duration.ofMillis(millis)))
                    .release()
                    .perform();
        } catch (Exception var4) {
            log.error("failed longPressElement");
        }
    }

    public ReasonsStep clickByString(String name, int timeOut) {
        ReasonsStep step;
        try {
            String generateXpath = new ElementGetTextsExtensions().xpathWithOptions(name);
            WebElement element = new ElementsSearchExtensions()
                    .overRideWebDriverWait(Duration.ofSeconds(timeOut), Duration.ofMillis(500))
                    .findElementBy(By.xpath(generateXpath));
            this.clickElement(this.timeOut, element, name);
            step = this.reasonsStep(Status.PASS, this.step, TestCategory.DRIVER, TestSeverity.NONE, "pass click");
        } catch (Exception exception) {
            step = this.reasonsStep(Status.FAIL, this.step, TestCategory.DRIVER, TestSeverity.NONE, "fail click");
        }

        return step;
    }

    private ReasonsStep reasonsStep(Status status, String step, TestCategory category, TestSeverity severity, String description) {
        return new ReasonsStep(
                status,
                "",
                step,
                category,
                severity,
                description
        );
    }
}
