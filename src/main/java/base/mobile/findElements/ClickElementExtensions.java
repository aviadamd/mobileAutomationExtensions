package base.mobile.findElements;

import base.IntegrateReport;
import base.driversManager.MobileManager;
import base.mobile.infrastarcture.InfraStructuresExtensions;
import base.mobile.MobileExtensionsObjects;
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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static io.appium.java_client.touch.LongPressOptions.longPressOptions;

@Slf4j
public class ClickElementExtensions extends MobileManager {
    private int elementTo = 15;
    private int pollingEvery = 500;

    public ClickElementExtensions setFluentWait(int elementTo, int pollingEvery) {
        this.elementTo = elementTo;
        this.pollingEvery = pollingEvery;
        return this;
    }
    public IntegrateReport<MobileExtensionsObjects> clickElement(By by, String desc) {
        return this.clickElement(ExpectedConditions.elementToBeClickable(by), desc);
    }

    public IntegrateReport<MobileExtensionsObjects> clickElement(WebElement element, String desc) {
        return this.clickElement(ExpectedConditions.elementToBeClickable(element), desc);
    }

    public IntegrateReport<MobileExtensionsObjects> clickElement(ExpectedCondition<WebElement> conditions, String desc) {
        ReasonsStep reportStep;
        try {
           this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery)
                    .appiumFluentWait()
                    .until(conditions)
                    .click();
           reportStep = this.reasonsStep(Status.PASS, this.step, TestCategory.DRIVER, TestSeverity.NONE, this.step + " pass click on " + desc);
        } catch (Exception exception) {
            reportStep = this.reasonsStep(this.status, this.step, TestCategory.DRIVER, TestSeverity.NONE, desc + " fail to click on element " + exception.getMessage());
        }
        reportStepTest(reportStep);
        return new IntegrateReport<>(reportStep, new MobileExtensionsObjects());
    }

    public IntegrateReport<MobileExtensionsObjects> tapElement(WebElement element) {
        ReasonsStep reportStep;
        try {
            this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery)
                    .appiumFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(element));
            Point point = this.getElementCords(element);
            reportStep = this.tapAtPoint(point);
        } catch (Exception e) {
            reportStep = this.reasonsStep(this.status, this.step, TestCategory.DRIVER, TestSeverity.NONE, this.step + " tap element fail");
        }
        reportStepTest(reportStep);
        return new IntegrateReport<>(reportStep, new MobileExtensionsObjects());
    }

    private ReasonsStep tapAtPoint(Point point) {
        ReasonsStep reasonsStep;
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
            reasonsStep = this.reasonsStep(this.status, this.step, TestCategory.DRIVER, TestSeverity.HIGH, "pass tap at point");
        } catch (Exception e) {
            reasonsStep = this.reasonsStep(this.status, this.step, TestCategory.DRIVER, TestSeverity.HIGH, e.getMessage());
        }
        return reasonsStep;
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
                    .release().perform();
        } catch (Exception e) {

        }
    }

    @SuppressWarnings("rawtypes")
    public void doubleClick(ExpectedCondition<WebElement> conditions) {
        try {

            WebElement element = this
                    .appiumFluentWaitExtensions(this.elementTo, this.pollingEvery)
                    .appiumFluentWait()
                    .until(conditions);

            Point point = this.getElementCords(element);
            (new TouchAction((AppiumDriver) getDriver()))
                    .press(PointOption.point(point)).release().perform()
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(50)))
                    .press(PointOption.point(point)).release().perform();
        } catch (Exception a) {

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
            this.clickElement(ExpectedConditions.elementToBeClickable(element), name);
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
