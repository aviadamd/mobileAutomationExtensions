package base.mobile.findElements;

import base.driversManager.MobileManager;
import base.mobile.enums.ScrollDirection;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import java.time.Duration;

@Slf4j
public class MutualSwipeGestureExtensions extends MobileManager {

    /** default screen - 10 from screen frame value **/
    private final int edgeBorder = 10;
    /** default time out for wait to element **/
    private int elementTimeOut = 10;
    /** default wait after each scroll action **/
    private int timeOutAfterScroll = 500;
    /** default on fail action log status **/
    private Status logStatus = Status.FAIL;
    /** default scroll press time **/
    private int pressTimeOut = 100;

    public MutualSwipeGestureExtensions setPressTime(int pressTimeOut) {
        this.pressTimeOut = pressTimeOut;
        return this;
    }

    public MutualSwipeGestureExtensions setLogStatus(Status logStatus) {
        this.logStatus = logStatus;
        return this;
    }

    public MutualSwipeGestureExtensions setTimeOutAfterScroll(int timeOutAfterScroll) {
        this.timeOutAfterScroll = timeOutAfterScroll;
        return this;
    }

    public MutualSwipeGestureExtensions setElementTimeOut(int elementTimeOut) {
        this.elementTimeOut = elementTimeOut;
        return this;
    }

    public int getPageUpperPosition(int defaultValue) {
        try {
            return 10 + this.edgeBorder;
        } catch (Exception cannotGetPageButtonPotionEx) {
            return defaultValue;
        }
    }

    public int getPageBottomPosition(int defaultValue) {
        try {
            Dimension dimension = getDriver().manage().window().getSize();
            return dimension.getHeight() - this.edgeBorder;
        } catch (Exception cannotGetPageButtonPotionEx) {
            return defaultValue;
        }
    }

    /**
     * swipeToElement
     * @param swipeSearch attempts to find element
     * @param element the WebElement
     * @param flow add element name to print
     * @param direction
     *     LEFT,
     *     RIGHT,
     *     DOWN_LARGE,
     *     DOWN,
     *     UP,
     *     UP_LITTLE,
     *     UP_LARGE,
     *     DOWN_LITTLE
     */
    public void swipeToElement(int swipeSearch, WebElement element, String flow, ScrollDirection direction) {
        boolean findElement = false;
        try {
            for (int i = 1; i < swipeSearch; i++) {
//                if (extensions.verificationsEx().isGetPage(element, this.elementTimeOut, 2, flow)) {
//                    findElement = true;
//                    log.info("swipeToElement pass to swipe to " + flow + ", after " + i + " times");
//                    break;
//                }
                this.swipe(true, direction, flow);
            }
        } catch (Exception e) {
            log.error("swipeToElement general error");
        } finally {

        }
    }

    public void swipeToElement(int swipeSearch, By by1, String flow, ScrollDirection direction) {
        boolean findElement = false;

        try {
            for (int i = 1; i < swipeSearch; i++) {
//                if (extensions.verificationsEx().isGetPage(by1, this.elementTimeOut, this.elementTimeOut, flow)) {
//                    findElement = true;
//                    break;
//                }
                this.swipe(true, direction, flow);
            }
        } catch (Exception e) {
            log.error("swipeToElement general error");
        } finally {

        }
    }

    public void swipeOverTimes(ScrollDirection direction, int times, String desc) {
        for (int i = 1; i < times; i++) {
            this.swipe(true, direction, desc);
        }
    }

    @SuppressWarnings("rawtypes")
    public void swipe(boolean activate, ScrollDirection direction, String desc) {
        if (activate) {

           TouchAction isPassScroll = null;
            try {

                PointOption pointOptionStart, pointOptionEnd;
                Dimension dimension = getDriver().manage().window().getSize();
                pointOptionStart = PointOption.point(dimension.width / 2, dimension.height / 2);
                int scrollDownDivider = 2, scrollABitDivider = 10;

                switch (direction) {
                    case UP_LARGE:
                        pointOptionEnd = PointOption.point(dimension.width / 2, dimension.height - this.edgeBorder);
                        isPassScroll = this.swipeGeneral(pointOptionStart, pointOptionEnd);
                        break;
                    case DOWN_LARGE:
                        pointOptionEnd = PointOption.point(dimension.width / 2, this.edgeBorder);
                        isPassScroll = this.swipeGeneral(pointOptionStart, pointOptionEnd);
                        break;
                    case UP:
                        pointOptionEnd = PointOption.point(dimension.width / 2, (dimension.height / 2) + (dimension.height / 2) / scrollDownDivider);
                        isPassScroll = this.swipeGeneral(pointOptionStart, pointOptionEnd);
                        break;
                    case DOWN:
                        pointOptionEnd = PointOption.point(dimension.width / 2, (dimension.height / 2) - (dimension.height / 2) / scrollDownDivider);
                        isPassScroll = this.swipeGeneral(pointOptionStart, pointOptionEnd);
                        break;
                    case DOWN_LITTLE:
                        pointOptionEnd = PointOption.point(dimension.width / 2, (dimension.height / 2) - (dimension.height / 2) / scrollABitDivider);
                        isPassScroll = this.swipeGeneral(pointOptionStart, pointOptionEnd);
                        break;
                    case UP_LITTLE:
                        pointOptionEnd = PointOption.point(dimension.width / 2, (dimension.height / 2) + (dimension.height / 2) / scrollABitDivider);
                        isPassScroll = this.swipeGeneral(pointOptionStart, pointOptionEnd);
                        break;
                    case LEFT:
                        pointOptionEnd = PointOption.point(edgeBorder, dimension.height - 2);
                        isPassScroll = isAndroidClient() ? swipeGeneral(pointOptionStart, pointOptionEnd) : this.swipeLeftToRight(false);
                        break;
                    case RIGHT:
                        pointOptionEnd = PointOption.point(dimension.width - this.edgeBorder, dimension.height / 2);
                        isPassScroll = isAndroidClient() ? this.swipeGeneral(pointOptionStart, pointOptionEnd) : this.swipeRightToLeft(false);
                        break;
                }
            } catch (Exception swipeGestureExtensions) {
                log.info("swipeGestureExtensions error ex: " + swipeGestureExtensions.getMessage());
            } finally {
                if (isPassScroll != null) {

                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private TouchAction swipeGeneral(PointOption pointOptionStart, PointOption pointOptionEnd) {
        TouchAction action = null;

        try {
            action = new TouchAction((AppiumDriver) getDriver())
                    .press(pointOptionStart)
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(this.pressTimeOut)))
                    .moveTo(pointOptionEnd)
                    .release()
                    .perform();
        } catch (Exception e) {
            log.error("swipe screen(): TouchAction fail  \n" + e.getMessage());
        } finally {
            if (action != null) {

            }
        }

        return action;
    }

    /**
     * swipeLeftToRight
     * like the base with out failing for optional swipe
     */
    @SuppressWarnings("rawtypes")
    public TouchAction swipeLeftToRight(boolean failTest) {
        TouchAction action = null;
        try {
            action = new TouchAction((AppiumDriver) getDriver());
            int yStart = getDriver().manage().window().getSize().getHeight() / 2;
            int xStart = (int) ((double) getDriver().manage().window().getSize().getWidth() * 0.2D);
            int xEnd = (int) ((double) getDriver().manage().window().getSize().getWidth() * 0.9);
            PointOption xyStart = (new PointOption()).withCoordinates(xStart, yStart);
            PointOption xyEnd = (new PointOption()).withCoordinates(xEnd, yStart);
            action.longPress(xyStart).moveTo(xyEnd).release().perform();
        } catch (Exception var8) {
            log.warn("swipe left to right failed");
            //if (failTest) this.report(LogStatus.FAIL, "swipe left to right failed");
        }
        return action;
    }

    /**
     * swipeRightToLeft
     * like the base with out failing for optional swipe
     */
    @SuppressWarnings("rawtypes")
    public TouchAction swipeRightToLeft(boolean failTest) {
        TouchAction action = null;
        try {
            action = new TouchAction((AppiumDriver) getDriver());
            int yStart = getDriver().manage().window().getSize().getHeight() / 2;
            int xStart = (int)((double) getDriver().manage().window().getSize().getWidth() * 0.2D);
            int xEnd = (int)((double) getDriver().manage().window().getSize().getWidth() * 0.9D);
            PointOption xyStart = (new PointOption()).withCoordinates(xStart, yStart);
            PointOption xyEnd = (new PointOption()).withCoordinates(xEnd, yStart);
            action.longPress(xyEnd).moveTo(xyStart).release().perform();

        } catch (Exception var8) {
            log.warn("swipe right to left failed");
            //if (failTest) this.report(LogStatus.FAIL, "swipe right to left failed");
        }
        return action;
    }

    @Override
    public String toString() {
        return "MutualSwipeGestureExtensions{" +
                " edgeBorder=" + edgeBorder +
                ", elementTimeOut=" + elementTimeOut +
                ", timeOutAfterScroll=" + timeOutAfterScroll +
                ", logStatus=" + logStatus +
                ", setPressTimeOut=" + pressTimeOut +
                '}';
    }
}
