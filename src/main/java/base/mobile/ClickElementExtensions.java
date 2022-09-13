package base.mobile;

import base.IntegrateReport;
import base.MobileWebDriverManager;
import base.mobile.elementsData.ElementsConstants;
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

    private AppiumFluentWaitExtensions appiumFluentWaitExtensions() {
        return new AppiumFluentWaitExtensions()
                .withGeneralPollingWaitStrategy(Duration.ofSeconds(this.timeOut))
                .pollingEvery(Duration.ofMillis(500));
    }

    public ClickElementExtensions setStep(String step) {
        this.step = step;
        return this;
    }

    public ClickElementExtensions setStatus(Status status) {
        this.status = status;
        return this;
    }

    public ClickElementExtensions setElementTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public IntegrateReport<ClickElementExtensions> clickElementBy(By by, String desc) {
        ReasonsStep step;
        try {
           this.appiumFluentWaitExtensions()
                    .appiumFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(by))
                    .click();
            step = this.reasonsStep(Status.PASS, this.step, TestCategory.DRIVER, TestSeverity.NONE, "pass click on " + desc);
        } catch (Exception exception) {
            step = this.reasonsStep(
                    this.status,
                    this.step,
                    TestCategory.DRIVER,
                    TestSeverity.NONE, desc + " fail to click on element " + exception.getMessage());
        }

        this.reportStepTest(step);
        return new IntegrateReport<>(step, this);
    }

    public IntegrateReport<ClickElementExtensions> clickElement(WebElement element, String desc) {
        ReasonsStep step;
        try {
            new AppiumFluentWaitExtensions()
                    .withGeneralPollingWaitStrategy(Duration.ofSeconds(timeOut))
                    .pollingEvery(Duration.ofMillis(500))
                    .appiumFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(element))
                    .click();
            step = this.reasonsStep(Status.PASS, this.step, TestCategory.DRIVER, TestSeverity.NONE, "pass click on " + desc);
        } catch (Exception exception) {
            step = this.reasonsStep(
                    this.status,
                    this.step,
                    TestCategory.DRIVER,
                    TestSeverity.NONE, desc + " fail to click on element " + exception.getMessage());
        }
        this.reportStepTest(step);
        return new IntegrateReport<>(step, this);
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
            (new TouchAction((AppiumDriver) getDriver()))
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
                (new TouchAction((AppiumDriver) getDriver()))
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
    public IntegrateReport<ClickElementExtensions> longPressElement(WebElement element, long millis) {
        ReasonsStep step;
        try {
            (new TouchAction((AppiumDriver) getDriver()))
                    .longPress(longPressOptions()
                            .withElement(ElementOption.element(element))
                            .withDuration(Duration.ofMillis(millis)))
                    .release()
                    .perform();
            step = this.reasonsStep(Status.PASS, this.step, TestCategory.DRIVER, TestSeverity.NONE, "pass tap");
        } catch (Exception var4) {
            step = this.reasonsStep(this.status, this.step, TestCategory.DRIVER, TestSeverity.NONE, "fail tap");
        }
        return new IntegrateReport<>(step, this);
    }

    public IntegrateReport<ClickElementExtensions> clickByString(String name, int timeOut) {
        ReasonsStep step;
        try {
            String generateXpath = ElementsConstants.xpathWithOptions(name);
            WebElement element = new ElementsSearchExtensions()
                    .overRideWebDriverWait(Duration.ofSeconds(timeOut), Duration.ofMillis(500))
                    .findElementBy(By.xpath(generateXpath));
            this.clickElement(element, name);
            step = this.reasonsStep(Status.PASS, this.step, TestCategory.DRIVER, TestSeverity.NONE, "pass click");
        } catch (Exception exception) {
            step = this.reasonsStep(Status.FAIL, this.step, TestCategory.DRIVER, TestSeverity.NONE, "fail click");
        }

        return new IntegrateReport<>(step, this);
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
