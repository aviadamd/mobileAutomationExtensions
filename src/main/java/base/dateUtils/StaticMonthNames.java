package base.dateUtils;

import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static java.lang.String.valueOf;

@Slf4j
public class StaticMonthNames {

    public String getStaticMonth(MonthNamesAs monthNamesAs, Month namesValues) {
        try {
            switch (monthNamesAs) {
                case MONTH_NUMBERS: return monthNumbers()[namesValues.getValue()];
                case HEBREW_MONTHS_NAMES: return hebrewMonthsNames()[namesValues.getValue()];
                case HEBREW_SHORT_MONTHS_NAMES: return hebrewShortMonthsNames()[namesValues.getValue()];
            }
        } catch (Exception e) {
            log.error("getStaticMonth error: " + e.getMessage());
            return "";
        }
        return "";
    }

    public String getDynamicMonth(String dateFormat, MonthDateAs dateAs, int incrementDecrement, MonthNamesAs monthNamesAs) {
        try {
            CalendarStaticData.DateAs monthDateAs = dateAs == MonthDateAs.MINUS_MONTHS ? CalendarStaticData.DateAs.MINUS_MONTHS: CalendarStaticData.DateAs.PLUS_MONTHS;
            LocalDate localDate = CalendarStaticData.buildDate(dateFormat, monthDateAs, incrementDecrement);
            switch (monthNamesAs) {
                case MONTH_NUMBERS: return monthNumbers()[localDate.getMonthValue()-1];
                case HEBREW_MONTHS_NAMES: return hebrewMonthsNames()[localDate.getMonthValue()-1];
                case HEBREW_SHORT_MONTHS_NAMES: return hebrewShortMonthsNames()[localDate.getMonthValue()-1];
            }
        } catch (Exception e) {
            log.error("getDynamicMonth error: " + e.getMessage());
            return "";
        }
        return "";
    }

    public enum Month {
        JAN(0),
        FEB(1),
        MAR(2),
        APR(3),
        MAY(4),
        JUN(5),
        JUL(6),
        AUG(7),
        SEP(8),
        OCT(9),
        NOV(10),
        DEC(11);

        private final int value;
        Month(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public enum MonthNamesAs {
        MONTH_NUMBERS,
        HEBREW_MONTHS_NAMES,
        HEBREW_SHORT_MONTHS_NAMES
    }

    private String[] monthNumbers() {
        return new String[] {
                valueOf(1),
                valueOf(2),
                valueOf(3),
                valueOf(4),
                valueOf(5),
                valueOf(6),
                valueOf(7),
                valueOf(8),
                valueOf(9),
                valueOf(10),
                valueOf(11),
                valueOf(12)
        };
    }

    private String[] hebrewMonthsNames() {
        return new String[] {
                "ינואר",
                "פברואר",
                "מרץ",
                "אפריל",
                "מאי",
                "יוני",
                "יולי",
                "אוגוסט",
                "ספטמבר",
                "אוקטובר",
                "נובמבר",
                "דצמבר"
        };
    }

    private String[] hebrewShortMonthsNames() {
        return new String[] {
                "ינו׳",
                "פבר׳",
                "מרץ",
                "אפר׳",
                "מאי",
                "יוני",
                "יולי",
                "אוג׳",
                "ספט׳",
                "אוק׳",
                "נוב׳",
                "דצמ׳"
        };
    }

    /**
     * @param format "dd.MM.yyyy" etc
     * @return fix after SimpleDateFormat
     */
    private static String dateFormat(String format) {
        SimpleDateFormat date = new SimpleDateFormat(format);
        Date getDate = new Date(System.currentTimeMillis());
        return date.format(getDate);
    }

    public enum MonthDateAs {
        MINUS_MONTHS,
        PLUS_MONTHS,
    }
}
