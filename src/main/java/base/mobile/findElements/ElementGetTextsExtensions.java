package base.mobile.findElements;

import base.IntegrateReport;
import base.driversManager.MobileManager;
import base.dateUtils.DateFormatData;
import base.mobile.elementsData.ElementsConstants;
import base.mobile.enums.GetValuesBy;
import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static base.dateUtils.dateValidator.DateValidatorEx.isDateValid;
import static base.staticData.MobileStringsUtilities.*;

@Slf4j
public class ElementGetTextsExtensions extends MobileManager {

    private int elementTo = 5;
    private int pollingEvery = 500;

    public ElementGetTextsExtensions setFluentWait(int elementTo, int pollingEvery) {
        this.elementTo = elementTo;
        this.pollingEvery = pollingEvery;
        return this;
    }

    /**
     * element wrapper to retrieve the get text options from an element
     * @param element ...
     * @return str
     */
    public IntegrateReport<String> getValue(WebElement element) {
        ReasonsStep step = new ReasonsStep(Status.INFO, "", this.step, TestCategory.NONE, TestSeverity.NONE, "desc");
        String strTextElement = "";
        try {
            strTextElement = this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery)
                    .appiumFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(element))
                    .getText();
            step = !strTextElement.isEmpty() ? new ReasonsStep(Status.PASS, "", this.step, TestCategory.DRIVER, TestSeverity.NONE, "get text " + strTextElement + " from element") : step;
        } catch (Exception e) {
            step = new ReasonsStep(this.status, "", this.step, TestCategory.DRIVER, TestSeverity.NONE, "fail to take text from element" + e.getMessage());
        }
        return new IntegrateReport<>(step, strTextElement);
    }

    /**
     * element wrapper to retrieve the get text options from an element
     * @param element ...
     * @param clientAttribute can be the attribute also
     * @return str
     */
    public IntegrateReport<String> getValue(WebElement element, Pair<String,String> clientAttribute) {
        ReasonsStep step = new ReasonsStep(Status.PASS, "", this.step, TestCategory.NONE, TestSeverity.NONE, "desc");
        String strTextElement = "";
        try {
            strTextElement = this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery)
                    .appiumFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(element))
                    .getAttribute(isAndroidClient() ? clientAttribute.getLeft() : clientAttribute.getRight());
            step = !strTextElement.isEmpty() ? new ReasonsStep(Status.PASS, "", this.step, TestCategory.DRIVER, TestSeverity.NONE, "get text " + strTextElement + " from element") : step;
        } catch (Exception e) {
            step = new ReasonsStep(this.status, "", this.step, TestCategory.DRIVER, TestSeverity.NONE, e.getMessage());
        }
        return new IntegrateReport<>(step, strTextElement);
    }

    /**
     * element wrapper to retrieve the get text options from an element
     * @param by ...
     * @return str
     */
    public IntegrateReport<String> getValue(By by) {
        ReasonsStep step = new ReasonsStep(Status.PASS, "", this.step, TestCategory.NONE, TestSeverity.NONE, "desc");
        String strTextElement = "";
        try {
            strTextElement = this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery)
                    .appiumFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(by))
                    .getText();
            step = !strTextElement.isEmpty() ? new ReasonsStep(Status.PASS, "", this.step, TestCategory.DRIVER, TestSeverity.NONE, "get text " + strTextElement + " from element") : step;
        } catch (Exception e) {
            step = new ReasonsStep(this.status, "", this.step, TestCategory.DRIVER, TestSeverity.NONE, e.getMessage());
        }
        return new IntegrateReport<>(step, strTextElement);
    }

    /**
     * element wrapper to retrieve the get text options from an element
     * @param by ...
     * @param clientAttribute can be the attribute also
     * @return str
     */
    public IntegrateReport<String> getValue(By by, Pair<String,String> clientAttribute) {
        ReasonsStep step = new ReasonsStep(Status.PASS, "", this.step, TestCategory.NONE, TestSeverity.NONE, "desc");
        String strTextElement = "";
        try {
            strTextElement =  this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery)
                    .appiumFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(by))
                    .getAttribute(isAndroidClient() ? clientAttribute.getLeft() : clientAttribute.getRight());
            step = !strTextElement.isEmpty() ? new ReasonsStep(Status.PASS, "", this.step, TestCategory.DRIVER, TestSeverity.NONE, "get text " + strTextElement + " from element") : step;
        } catch (Exception e) {
            step = new ReasonsStep(this.status, "", this.step, TestCategory.DRIVER, TestSeverity.NONE, e.getMessage());
        }
        return new IntegrateReport<>(step, strTextElement);
    }

    /**
     * String getElementValues(Pair<WebElement,WebElement> element, boolean isSetWarnIfStrEmpty)
     * @param element element
     * @return single text from element with all getAttribute options for both clients Android/iOS
     */
    public IntegrateReport<String> getAllValues(WebElement element, GetValuesBy by) {
        String strTextElement = "";
        boolean findElementText = false;

        ReasonsStep step = new ReasonsStep(Status.FAIL, "", this.step, TestCategory.NONE, TestSeverity.NONE, "");
        try {
            if (this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery).isGetElement(element, by.getDesc())) {
                for (String getTextAttr : ElementsConstants.clientAttr()) {
                    strTextElement = element.getAttribute(getTextAttr);
                    if (this.isFindTextBy(strTextElement, by)) {
                        findElementText = true;
                        break;
                    }
                }
                if (findElementText) {
                    step = new ReasonsStep(Status.PASS, "", this.step, TestCategory.NONE, TestSeverity.NONE, "take text from element");
                } else step = new ReasonsStep(this.status, "", this.step, TestCategory.NONE, TestSeverity.NONE, "fail to take text from element");
            }
        } catch (Exception e) {
            step = new ReasonsStep(this.status, "", this.step, TestCategory.NONE, TestSeverity.NONE, e.getMessage());
        }
        return new IntegrateReport<>(step, strTextElement);
    }

    /**
     * String getElementValues(Pair<WebElement,WebElement> element, boolean isSetWarnIfStrEmpty)
     * @param by element
     * @return single text from element with all getAttribute options for both clients Android/iOS
     */
    public IntegrateReport<String> getAllValues(By by, GetValuesBy byVal) {
        String strTextElement = "";
        boolean findElementText = false;

        ReasonsStep step = new ReasonsStep(this.status, "", this.step, TestCategory.NONE, TestSeverity.NONE, "");
        try {
            if (this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery).isGetElement(by, byVal.getDesc())) {
                for (String getTextAttr : ElementsConstants.clientAttr()) {
                    strTextElement = this.appiumFluentWaitExtensions(this.elementTo, this.pollingEvery)
                            .appiumFluentWait()
                            .until(ExpectedConditions.elementToBeClickable(by))
                            .getAttribute(getTextAttr);
                    if (this.isFindTextBy(strTextElement, byVal)) {
                        findElementText = true;
                        break;
                    }
                }
                if (findElementText) {
                    step = new ReasonsStep(Status.PASS, "", this.step, TestCategory.NONE, TestSeverity.NONE, "");
                } else step = new ReasonsStep(this.status, "", this.step, TestCategory.NONE, TestSeverity.NONE, "");
            }
        } catch (Exception e) {
            step = new ReasonsStep(this.status, "", this.step, TestCategory.NONE, TestSeverity.NONE, e.getMessage());
        }
        return new IntegrateReport<>(step, strTextElement);
    }

    public boolean isFindTextBy(String strTextElement, GetValuesBy by) {
        switch (by) {
            case IGNORE: return !strTextElement.isEmpty();
            case DATE: return isDateValid(DateFormatData.Formats.WITH_DOT_DD_MM_YY.getFormat(), strTextElement);
            case HEBREW: return isContainsHebrewLetters(strTextElement);
            case ENGLISH: return isContainsEnglishLetters(strTextElement);
            case NUMBERS: return isContainsNumbers(strTextElement);
            case HEBREW_AND_ENGLISH: return isContainsLetters(strTextElement);
            default: return false;
        }
    }
}
