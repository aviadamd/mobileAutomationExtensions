package base.mobile;

import base.MobileWebDriverManager;
import base.dateUtils.DateFormatData;
import base.mobile.elementsData.ElementsAttributes;
import base.mobile.enums.GetValuesBy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import static base.dateUtils.dateValidator.DateValidatorEx.isDateValid;
import static base.mobile.elementsData.ElementsAttributes.AndroidElementsAttributes.TEXT;
import static base.mobile.elementsData.ElementsAttributes.SharedElementTextAttr.*;
import static base.staticData.MobileStringsUtilities.*;
import static java.util.Arrays.asList;

@Slf4j
public class ElementGetTextsExtensions extends MobileWebDriverManager {

    private int elementTo = 4;

    public ElementGetTextsExtensions setElementTo(int elementTo) {
        this.elementTo = elementTo;
        return this;
    }

    /**
     * element wrapper to retrieve the get text options from an element
     * @param element ...
     * @param clientAttribute can be the attribute also
     * @return str
     */
    public String getValue(WebElement element, List<String> clientAttribute) {
        String strTextElement = "";

        AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
        appiumFluentWaitExtensions.withGeneralPollingWaitStrategy(Duration.ofSeconds(this.elementTo));

        try {
            if (appiumFluentWaitExtensions.isGetElement(element, "")) {
                strTextElement = element.toString() != null ? element.toString() : "";
                if (clientAttribute != null) {
                    if (isAndroidClient()) {
                        strTextElement = element.getAttribute(clientAttribute.get(0));
                    } else {
                        strTextElement = element.getAttribute(clientAttribute.get(1));
                    }
                } else strTextElement = element.getText();
            } else {
                log.debug("not success to get value from element");
            }
        } catch (Exception e) {
            log.debug("not success to get value from element");
        }

        log.info("get value from element: " + hebrewTextLeftToRight(strTextElement));
        return hebrewTextLeftToRight(strTextElement);
    }

    /**
     * element wrapper to retrieve the get text options from an element
     * @param by ...
     * @param clientAttribute can be the attribute also
     * @return str
     */
    public String getValue(By by, List<String> clientAttribute) {
        String strTextElement = "";
        AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
        appiumFluentWaitExtensions.withGeneralPollingWaitStrategy(Duration.ofSeconds(this.elementTo));

        try {
            if (appiumFluentWaitExtensions.isGetElement(by, "")) {
                strTextElement = by.toString() != null ? by.toString() : "";
                if (clientAttribute != null) {
                    if (isAndroidClient()) {
                        strTextElement = this.getElement(by).getAttribute(clientAttribute.get(0));
                    } else {
                        strTextElement = this.getElement(by).getAttribute(clientAttribute.get(1));
                    }
                } else strTextElement = this.getElement(by).getText();
            } else {
                log.debug("not success to get value from element");
                return strTextElement;
            }
        } catch (Exception e) {
            log.debug("not success to get value from element");
        }

        log.info("get value from element: " + hebrewTextLeftToRight(strTextElement));
        return hebrewTextLeftToRight(strTextElement);
    }

    /**
     * String getElementValues(Pair<WebElement,WebElement> element, boolean isSetWarnIfStrEmpty)
     * @param element element
     * @return single text from element with all getAttribute options for both clients Android/iOS
     */
    public String getAllValues(WebElement element, GetValuesBy by) {
        String strTextElement = "";
        boolean findElementText = false;

        AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
        appiumFluentWaitExtensions.withGeneralPollingWaitStrategy(Duration.ofSeconds(this.elementTo));

        try {
            if (appiumFluentWaitExtensions.isGetElement(element, by.getDesc())) {
                for (String getTextAttr: this.clientAttr()) {
                    if (this.isGetEleAttributeValid(element, getTextAttr)) {
                        strTextElement = this.getEleAttribute(element, getTextAttr);
                        if (this.isFindTextBy(strTextElement, by)) {
                            findElementText = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.debug("not success to get value from element");
        }

        if (strTextElement == null || strTextElement.isEmpty()) {
            log.debug("not success to get value from element");
        }

        if (findElementText) log.info("return text by " + by.getDesc() + " with value " + strTextElement);
        return hebrewTextLeftToRight(strTextElement);
    }

    public boolean isFindTextBy(String strTextElement, GetValuesBy by) {
        switch (by) {
            case IGNORE:
                return !strTextElement.isEmpty();
            case DATE:
                return isDateValid(DateFormatData.Formats.WITH_DOT_DD_MM_YY.getFormat(), strTextElement);
            case HEBREW:
                return isContainsHebrewLetters(strTextElement);
            case ENGLISH:
                return isContainsEnglishLetters(strTextElement);
            case NUMBERS:
                return isContainsNumbers(strTextElement);
            case HEBREW_AND_ENGLISH:
                return isContainsLetters(strTextElement);
            default: return false;
        }
    }

    private boolean isGetEleAttributeValid(WebElement element, String strTextElement) {
        try {
            return element.getAttribute(strTextElement) != null
                    && !element.getAttribute(strTextElement).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private String getEleAttribute(WebElement element, String strTextElement) {
        try {
            if (element.getAttribute(strTextElement) != null && !element.getAttribute(strTextElement).isEmpty()) {
                return element.getAttribute(strTextElement);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    private List<String> clientAttr() {
        return isAndroidClient() ?
                asList(TEXT.getTag(), CONTENT_DESC.getTag())
                : asList(LABEL.getTag(), VALUE.getTag(), NAME.getTag());
    }

    /**
     * @param attributes options
     * @param name free text to search
     * @return string of element attribute options
     */
    public String xpathWithOptions(
            Pair<ElementsAttributes.IosElementsAttributes, ElementsAttributes.AndroidElementsAttributes> attributes, String name) {
        return isAndroidClient() ?
                "//*[@"+attributes.getRight().getTag()+"='"+name+"']" :
                "//*[@"+attributes.getRight().getTag()+"=\""+name+"\"]";
    }

    /**
     * @param name free text to search
     * @return string of element attribute options
     */
    public String xpathWithOptions(String name) {
        return isAndroidClient() ?
                "//*[@"+ ElementsAttributes.AndroidElementsAttributes.TEXT.getTag()+"='"+name+"']" :
                "//*[@"+ ElementsAttributes.IosElementsAttributes.LABEL.getTag()+"='"+name+"' " +
                        "or @"+ ElementsAttributes.IosElementsAttributes.VALUE.getTag()+"='"+name+"' " +
                        "or @"+ ElementsAttributes.IosElementsAttributes.NAME.getTag()+"='"+name+"' " +
                        "or @"+ ElementsAttributes.IosElementsAttributes.LABEL.getTag()+"=\""+name+"\" " +
                        "or @"+ ElementsAttributes.IosElementsAttributes.VALUE.getTag()+"=\""+name+"\" " +
                        "or @"+ ElementsAttributes.IosElementsAttributes.NAME.getTag()+"=\""+name+"\"]";
    }

    /**
     * short cut method
     * @param by the ele
     * @return ele
     */
    private WebElement getElement(By by) {
        AppiumFluentWaitExtensions appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
        return appiumFluentWaitExtensions.getFluentWaitObject().until(e -> getDriver().findElement(by));
    }

    /**
     * @param name free text to search
     * @return string of element attribute options
     */
    public String xpathClassName(String name) {
        return isAndroidClient()
                ? "//*[@"+ ElementsAttributes.AndroidElementsAttributes.CLASS.getTag()+"'"+name+"']"
                : "//*[@"+ ElementsAttributes.IosElementsAttributes.CLASS.getTag()+"=\""+name+"\"]";
    }
}
