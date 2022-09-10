package base.mobile;

import base.MobileWebDriverManager;
import base.staticData.MobileStringsUtilities;
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
import java.time.Duration;
import static io.appium.java_client.touch.LongPressOptions.longPressOptions;

@Slf4j
public class ClickElementExtensions extends MobileWebDriverManager {

    private int timeOut = 15;

    public ClickElementExtensions timeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    /**
     * this method use simple simple mobile gesture as click with more flexible options
     * @param element the actual element
     */
    public ClickElementExtensions click(WebElement element) {
        this.clickElement(this.timeOut, element,"");
        return this;
    }

    /**
     * this method use simple simple mobile gesture as click with more flexible options
     * @param element the actual element
     * @param elementDesc add element desc
     */
    public ClickElementExtensions click(WebElement element, String elementDesc) {
        this.clickElement(this.timeOut, element, elementDesc);
        return this;
    }

    /**
     * this method use simple simple mobile gesture as click with more flexible options
     * @param by the actual element
     */
    public ClickElementExtensions clickBy(By by) {
        this.clickElementBy(this.timeOut, by,"");
        return this;
    }

    /**
     * this method use simple simple mobile gesture as click with more flexible options
     * @param by the actual element
     * @param elementDesc add element desc
     */
    public ClickElementExtensions clickBy(By by, String elementDesc) {
        this.clickElementBy(this.timeOut, by, elementDesc);
        return this;
    }

    /**
     * this method use simple simple mobile gesture as tap with more flexible options
     * @param element the actual element
     */
    public ClickElementExtensions tap(WebElement element) {
        this.tapElement(this.timeOut, element);
        return this;
    }

    /**
     * @param timeOut set an time out for flexible scenarios
     * @param by locator the actual element
     */
    public void clickElementBy(int timeOut, By by, String desc) {
        String elementName;
        try {

            AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
            appiumFluentWaitExtensions.withGeneralPollingWaitStrategy(Duration.ofSeconds(timeOut));
            if (appiumFluentWaitExtensions.isGetElement(by, desc)) {
                getDriver().findElement(by).click();
            }
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException a) {
            String nameEle = by != null ? by.toString() : "";
            log.error("failed click at element " + nameEle);
        } catch (NullPointerException nx) {
            log.error("failed click at element is null ");
        } catch (Exception exception) {
            log.error("failed click at element");
        }
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

        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException a) {
            String nameEle = element != null ? element.toString() : "";
            log.error("failed click at element " + nameEle);
        } catch (NullPointerException nx) {
            log.error("failed click at element is null " + nx.getMessage());
        } catch (Exception exception) {
            log.error("failed click at element" + exception.getMessage());
        }
    }

    public void tapElement(int timeOut, WebElement element) {
        boolean passTapElement = false;

        try {

            AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
            appiumFluentWaitExtensions.withGeneralPollingWaitStrategy(Duration.ofSeconds(timeOut));

            if (appiumFluentWaitExtensions.isGetElement(element,"")) {
                Point point1 = this.getElementCords(element);
                this.tapAtPoint(point1);
                passTapElement = true;
            }

        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException a) {
            log.error("failed tap at element");
        } finally {
            String elementName = element != null ? element.toString() : "";
            log.error("failed tap at element " + elementName);
        }
    }

    public void tapElement(int timeOut, By by) {
        try {

            WebElement element = new ElementsSearchExtensions()
                    .overRideWebDriverWait(Duration.ofSeconds(timeOut), Duration.ofMillis(500))
                    .findElementBy(by);

            if (element != null) {
                Point point1 = this.getElementCords(element);
                this.tapAtPoint(point1);
            }

        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException a) {
            String name = by != null ? by.toString() : "";
            log.error("failed tap at element" + name);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error("failed tap at element fail element is null ");
            }
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
            log.info("tapAtPoint finish");

        } catch (Exception e) {
            log.error("failed tap at element");
        }
    }

    @SuppressWarnings("rawtypes")
    public void clickByCords(WebElement element) {
        TouchAction action = null;
        try {

            Rectangle rectangle1 = element.getRect();
            int refEleMidX1 = rectangle1.getX();
            int refEleMidY1 = rectangle1.getY();

            action = (new TouchAction((AppiumDriver) getDriver()))
                    .press(PointOption.point(refEleMidX1, refEleMidY1))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(50)))
                    .release()
                    .perform();
        } catch (Exception e) {
        } finally {
            if (action == null) {
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public boolean doubleClick(int timeOut, WebElement element) {
        boolean find = false;
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
                find = action != null;
            }
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException a) {
            log.error("NoSuchElementException failed doubleClick at element " + a.getMessage());
        } catch (Exception e) {
            log.error("Exception failed doubleClick at element " + e.getMessage());
        } finally {
            if (!find) {
                String name = element != null ? element.toString() : "";
            }
        }

        return find;
    }

    private Point getElementCords(WebElement element) {
        Point point = null;

        try {
            Rectangle rect = element.getRect();
            point = new Point(rect.x + (int) (rect.getHeight() / 2.0), rect.y + (int) (rect.getHeight() / 2.0));
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException a) {
            String name = element.toString();
        } catch (Exception exception) {

        } finally {

        }

        return point;
    }

    @SuppressWarnings("rawtypes")
    public void longPressElement(WebElement element, long millis) {
        TouchAction action = null;
        try {

            action = (new TouchAction((AppiumDriver) getDriver()))
                    .longPress(longPressOptions()
                            .withElement(ElementOption.element(element))
                            .withDuration(Duration.ofMillis(millis)))
                    .release()
                    .perform();

            String name = element.toString() != null ? element.toString() : "";
            if (name.contains("accessibility id") || name.contains("id")) {
                name = MobileStringsUtilities.splitString(name,"id",0);
            } else if(name.contains("xpath")) {
                name = MobileStringsUtilities.splitString(name,"xpath",0);
            } else name = "";
        } catch (Exception var4) {
        }
    }

    public void clickByString(String name, int timeOut) {
        boolean find = false;

        String generateXpath = new ElementGetTextsExtensions().xpathWithOptions(name);
        WebElement element = new ElementsSearchExtensions()
                .overRideWebDriverWait(Duration.ofSeconds(timeOut), Duration.ofMillis(500))
                .findElementBy(By.xpath(generateXpath));

        if (element != null) {
            this.click(element);
            find = true;
        }

        if (!find) {

        }
    }
}
