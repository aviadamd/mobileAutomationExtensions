package base.pages;

import base.mobile.elementsData.ElementsConstants;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.HowToUseLocators;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.appium.java_client.pagefactory.LocatorGroupStrategy.ALL_POSSIBLE;

public class CalendarComponentWidget extends UiScreenInitiation {

    public CalendarComponentWidget(WebDriver driver) { super(driver); }

    private final String ANDROID_ID = "android:id/";
    private final String ANDROID_YEAR_ID = ""+ANDROID_ID+"date_picker_year_picker";
    private final String IOS_DATE_COMPONENT = "ChooseDateController_datePickerComponent";
    private final String IOS_PICKER_CLASS = "XCUIElementTypePicker/XCUIElementTypePickerWheel";

    //Android/Ios calendar close button
    @HowToUseLocators(androidAutomation = ALL_POSSIBLE , iOSXCUITAutomation = ALL_POSSIBLE)
    @iOSXCUITFindBy(id = "ChooseDateController_finishButton")
    @AndroidFindBy(xpath = "//"+ ElementsConstants.Android.ClassType.BUTTON +"[@text='OK']")
    public WebElement closeDatePickerApprove;

    //Android/Ios calendar close button
    @HowToUseLocators(androidAutomation = ALL_POSSIBLE , iOSXCUITAutomation = ALL_POSSIBLE)
    @iOSXCUITFindBy(xpath = "//*[@name=\"סגירה\" or @name=\"סגור\"]")
    @AndroidFindBy(xpath = "//"+ ElementsConstants.Android.ClassType.BUTTON+"[@text='CANCEL']")
    public WebElement closeDatePickerCancel;

    //Android/Ios calendar year header
    @iOSXCUITFindBy(className = ElementsConstants.Ios.ClassType.PICKER_WHEEL)
    @AndroidFindBy(id = ""+ANDROID_ID + "date_picker_header_year")
    public WebElement datePickerHeaderYear;

    //Android calendar search
    @AndroidFindBy(xpath = "//*[@resource-id='android:id/date_picker_day_picker']//"+ ElementsConstants.Android.FollowingSiblings.VIEW_PAGER+"//"+ ElementsConstants.Android.FollowingSiblings.VIEW+"//"+ ElementsConstants.Android.FollowingSiblings.VIEW+""+"")
    public List<WebElement> calender;

    //Android/Ios Year search
    @iOSXCUITFindBy(className = ElementsConstants.Ios.ClassType.PICKER_WHEEL)
    @AndroidFindBy(xpath = "//*[@resource-id='"+ANDROID_YEAR_ID+"']//"+ ElementsConstants.Android.FollowingSiblings.TEXT_VIEW+"")
    public List<WebElement> calendarYearPickerText;

    //Android Next month
    @AndroidFindBy(id = ""+ANDROID_ID + "next")
    public WebElement datePickerNextMonth;

    //Android Previous month
    @AndroidFindBy(id = ""+ANDROID_ID + "prev")
    public WebElement datePickerPrevMonth;

    //Ios picker wheel
    @iOSXCUITFindBy(xpath = "//"+ ElementsConstants.Ios.ClassType.PICKER_DATE+"[@name=\""+IOS_DATE_COMPONENT+"\"]/"+IOS_PICKER_CLASS+"")
    public List<WebElement> iosPickerWheel;
}
