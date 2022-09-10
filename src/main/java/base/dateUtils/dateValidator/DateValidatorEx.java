package base.dateUtils.dateValidator;

import base.dateUtils.dateValidator.DateComperingValidator.Verify;
import base.dateUtils.DateSearchParameters;
import base.dateUtils.FindDateFromTextList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DateValidatorEx {

    public static boolean isDateValid(String pattern, String date) {
        try {
            DateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            return GenericValidator.isDate(date, pattern, true);
        } catch (Exception e) {
            return false;
        }
    }

    public static Date setDate(String pattern, String date) {
        try {
            DateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            if (isDateValid(pattern, date)) {
                return sdf.parse(date);
            }
        } catch (Exception e) {
            return new Date();
        }
        return new Date();
    }

    public static int containsDate(Date start, Date end) {
        try {
            return start.compareTo(end);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isDateRangeBetween(Date minuteOfDay, Date startMinuteOfDay, Date endMinuteOfDay) {
        return minuteOfDay.compareTo(startMinuteOfDay) >= 0
                && minuteOfDay.compareTo(endMinuteOfDay) < 0;
    }

    public FindDateFromTextList searchByDate(DateSearchParameters dateSearchParameters, List<String> listSearch, Verify verify) {
        FindDateFromTextList findDateFromTextList = new FindDateFromTextList("",false);
        String date = dateSearchParameters.getDate();

        for (String text: listSearch) {
            if (new DateComperingValidator().isDateMatch(dateSearchParameters.getDateFormat(), text, date, verify)) {
                log.info("text " + text + " is valid for test");
                findDateFromTextList = new FindDateFromTextList(text, true);
                break;
            }
        }

        if (!findDateFromTextList.isFind()) {
            log.error("fail find from list of: "+listSearch+", string date type");
        }

        return findDateFromTextList;
    }

    /**
     * extractDateFromString
     * @param textWithDate full string to extract the date from
     * @param datePattern "\\d{2}-\\d{2}-\\d{4}" || "\\d{2}-\\d{4}-\\d{2}" || "\\d{2}-\\d{4}-\\d{4}"
     * @return empty string if there not match or the fix string with the date
     */
    public static String extractDateFromString(String textWithDate, String datePattern) {
        try {
            Pattern pattern = Pattern.compile(datePattern);
            Matcher matcher = pattern.matcher(textWithDate);
            if(matcher.find()) {
                return matcher.group();
            } else return "";
        } catch (Exception e) {
            return "";
        }
    }
}
