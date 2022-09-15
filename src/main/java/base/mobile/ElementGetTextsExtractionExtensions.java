package base.mobile;

import base.driversManager.MobileWebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebElement;
import static base.staticData.MobileRegexConstants.*;
import static base.staticData.MobileStringsUtilities.formatStringToNumber;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;

@Slf4j
public class ElementGetTextsExtractionExtensions extends MobileWebDriverManager {

    private int elementTimeOut = 4;
    private Pair<String,String> elementsAttr = Pair.of("text", "label");

    private String asLetters = "";
    private String asNumbers = "";
    private double toDoubleFormat = 0.0;
    private String asStringFormat = "";
    private int asLengthFormat = 0;

    public String onlyLetters() { return this.asLetters; }
    public String onlyNumbers() { return this.asNumbers; }
    public double asDouble() { return this.toDoubleFormat; }
    public String asString() { return this.asStringFormat; }
    public int asLength() { return this.asLengthFormat; }

    public ElementGetTextsExtractionExtensions setElementsTo(int elementTimeOut) {
        this.elementTimeOut = elementTimeOut;
        return this;
    }

    public ElementGetTextsExtractionExtensions setElementsAttr(Pair<String,String> elementsAttr) {
        this.elementsAttr = elementsAttr;
        return this;
    }

    /**
     * extractNumber
     * @param element web element to extract the from
     * @return this
     */
    public ElementGetTextsExtractionExtensions extractNumber(WebElement element) {
        String noteText = this.getValueFrom(element);
        String formatAsString = formatStringToNumber(noteText,",", SAVE_NUMERIC_CHARS_WITH_DOT);
        this.toDoubleFormat = toDouble(formatAsString,0.0);
        this.asStringFormat = String.valueOf(this.toDoubleFormat);
        this.asLengthFormat = this.asStringFormat.length();
        this.asLetters = this.asStringFormat.replaceAll(SAVE_LETTERS_CHARS,"");
        this.asNumbers = this.asStringFormat.replaceAll(SAVE_NUMERIC_CHARS,"");
        return this;
    }

    /**
     * extractNumber
     * @param element web element to extract the from
     * @param numberOption
     * INCREMENT, DECREMENT, IGNORE
     * @param by your num choice
     * @return this
     */
    public ElementGetTextsExtractionExtensions extractNumber(WebElement element, NumberOption numberOption, double by) {
        String elementText = this.getValueFrom(element);
        String formatAsString = formatStringToNumber(elementText,",", SAVE_NUMERIC_CHARS_WITH_DOT);
        this.toDoubleFormat = toDouble(formatAsString,0.0);
        this.asStringFormat = String.valueOf(this.toDoubleFormat);
        this.asLengthFormat = this.asStringFormat.length();
        this.asLetters = this.asStringFormat.replaceAll(SAVE_LETTERS_CHARS,"");

        switch (numberOption) {
            case IGNORE:
                log.info(" this.toDoubleFormat with out increment " + this.toDoubleFormat);
                break;
            case DECREMENT:
                this.toDoubleFormat = this.toDoubleFormat - by;
                log.info(" this.toDoubleFormat with decrement " + this.toDoubleFormat);
                this.asStringFormat = String.valueOf(this.toDoubleFormat);
                this.asLengthFormat = this.asStringFormat.length();
                break;
            case INCREMENT:
                this.toDoubleFormat = this.toDoubleFormat + by;
                log.info(" this.toDoubleFormat with increment " + this.toDoubleFormat);
                this.asStringFormat = String.valueOf(this.toDoubleFormat);
                this.asLengthFormat = this.asStringFormat.length();
                break;
        }

        this.asNumbers = this.asStringFormat.replaceAll(SAVE_NUMERIC_CHARS,"");
        return this;
    }

    public enum NumberOption {
        INCREMENT,
        DECREMENT,
        IGNORE
    }

    /**
     * getValueFrom
     * element wrapper to retrieve the get text options from an element
     * @param element ...
     * @return str
     */
    private String getValueFrom(WebElement element) {
        return new ElementGetTextsExtensions()
                .setElementTo(this.elementTimeOut)
                .getValue(element, this.elementsAttr)
                .proceed();
    }
}
