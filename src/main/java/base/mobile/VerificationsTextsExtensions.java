package base.mobile;

import base.MobileWebDriverManager;
import base.staticData.MobileStringsUtilities;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Description;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class VerificationsTextsExtensions extends MobileWebDriverManager {

    @Description("compare text")
    public boolean compareText(boolean equals, String actual, String expected, Status logStatus) {
        boolean status;
        try {
            if (equals) {
                if (!isTextEquals(actual, expected, logStatus)) {
                    log.info("actual text " + actual + "");
                    log.info("is not equals to expected text " + expected + "");
                    status = false;
                } else {
                    log.info("actual text " + actual + "");
                    log.info("is equals to expected text " + expected + "");
                    status = true;
                }
            } else {
                if (isTextEquals(actual, expected, logStatus)) {
                    log.info("actual text " + actual + "");
                    log.info("is equals to expected text " + expected + "");
                    status = false;
                } else {
                    log.info("actual text " + actual + "");
                    log.info("is not equals to expected text " + expected + "");
                    status = true;
                }
            }
        } catch (Exception var7) {
            log.error("compareText error " + var7.getMessage());
            return false;
        }
        return status;
    }

    /**
     * findData
     * @param data get values
     * @param actual from the page itself
     * @return collector for each text that collected
     */
    public List<String> findsData(List<String> data, String actual) {
        List<String> collector = new ArrayList<>();
        for (String getData: data) {
            if (this.isTextEquals(getData, actual, Status.INFO)) {
                log.info("expected  "+getData+" is equals to actual "+actual+"");
                collector.add(getData);
            }
        }
        return collector;
    }

    /**
     * isTextEquals
     * @param element actual
     * @param text expected
     * @param status LogStatus
     * @return true/false
     */
    public boolean isTextEquals(WebElement element, String text, Status status) {
        String textEle = MobileStringsUtilities.hebrewTextLeftToRight(element.getText().trim());
        String textTrim = MobileStringsUtilities.hebrewTextLeftToRight(text.trim());

        boolean statusCheck;

        if (status == Status.INFO) {
            statusCheck = textEle.equals(textTrim) || textEle.contains(textTrim) || textEle.contentEquals(textTrim);
        } else if (status == Status.WARNING) {
            statusCheck = textEle.equals(textTrim) || textEle.contains(textTrim);
        } else if (status == Status.FAIL) {
            statusCheck = textEle.equals(textTrim);
        } else statusCheck = textEle.contains(textTrim);

        return statusCheck;
    }

    /**
     * isTextEquals
     * @param text1 expected
     * @param text2 actual
     * @param status LogStatus
     * @return true/false
     */
    public boolean isTextEquals(String text1, String text2, Status status) {
        String textEle = MobileStringsUtilities.hebrewTextLeftToRight(text1.trim());
        String textTrim = MobileStringsUtilities.hebrewTextLeftToRight(text2.trim());

        boolean statusCheck;

        if (status == Status.INFO) {
            statusCheck = textEle.equals(textTrim) || textEle.contains(textTrim) || textEle.contentEquals(textTrim);
        } else if (status == Status.WARNING) {
            statusCheck = textEle.equals(textTrim) || textEle.contains(textTrim);
        } else if (status == Status.FAIL) {
            statusCheck = textEle.equals(textTrim);
        } else statusCheck = textEle.contains(textTrim);

        return statusCheck;
    }

    /**
     * findData
     * @param expected list
     * @param actual list
     * @param status to test status
     * @return true false
     */
    public boolean findData(List<String> expected, List<String> actual, Status status) {
        Pair<List<String>, List<String>> collector = this.collectLists(expected, actual);
        //getLeft the pass find strings
        collector.getLeft().forEach(text ->  log.info("items that was found from actual list " + text));
        //getRight the fail find strings
        if (!collector.getRight().isEmpty()) {
            collector.getRight().forEach(text -> log.info("items that was not found from actual list " + text));
            log.info("fail check texts from list " + collector.getRight().toString());
            return false;
        }

        return true;
    }

    private Pair<List<String>, List<String>> collectLists(List<String> expected, List<String> actual) {
        List<String> addPass = new ArrayList<>();
        List<String> addFail = new ArrayList<>();

        for (String setText: expected) {
            if (actual.contains(setText)) {
                addPass.add(setText);
            } else addFail.add(setText);
        }

        addFail.removeIf(addPass::contains);
        return Pair.of(addPass, addFail);
    }
}
