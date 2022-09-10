package base.dateUtils;

import base.mobile.enums.CalendarPeriod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import static base.dateUtils.DateFormatData.Formats.WITH_DOT_DD_MM_YY;
import static base.dateUtils.DateFormatData.Formats.WITH_DOT_MONTH_AS_TEXT_DD_MMMM_YYYY;
import static base.staticData.MobileRegexConstants.SAVE_LETTERS_CHARS;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

@Slf4j
public class CalendarStaticData {

    /*** @return cal instance */
    public static Calendar getCalendar() {
        return GregorianCalendar.getInstance();
    }

    /*** @return cal instance */
    public static LocalDateTime getCalendarLocalDate() {
        Date date = new Date();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /*** @return int hour of the day */
    public static int getTheHourOfTheDay() {
        Calendar date = getCalendar();
        return date.get(HOUR_OF_DAY) * 100 + date.get(MINUTE);
    }

    /**
     * @param dateFormat as free date format
     * @param increment as flag
     * @param by as int
     * @return incremented or decremented day by days
     */
    public static String setDate(String dateFormat, boolean increment, CalendarPeriod period, int by) {
        String dateFull;
        try {

            Calendar date = setCalendarData(dateFormat, increment, period.getPeriod(), by);
            dateFull = new SimpleDateFormat(dateFormat).format(date.getTime());

        } catch (Exception a) {
            log.error(a.getMessage());
            return "";
        }

        return dateFull;
    }

    /**
     * @param monthText as flag
     * @param increment as flag
     * @param by as int
     * @return incremented or decremented day by days
     */
    public static String setDate(boolean monthText, boolean increment, CalendarPeriod period, int by) {
        String dateFull;
        try {

            String formatDay = monthText ? WITH_DOT_MONTH_AS_TEXT_DD_MMMM_YYYY.getFormat() : WITH_DOT_DD_MM_YY.getFormat();
            Calendar date = setCalendarData(formatDay, increment, period.getPeriod(), by);
            dateFull = new SimpleDateFormat(formatDay).format(date.getTime());

        } catch (Exception a) {
            log.error(a.getMessage());
            return "";
        }

        return fixDate(dateFull);
    }

    /**
     * @param by as int
     * @return incremented or decremented day by days
     */
    public static String setDate(String format, int by, DateAs dateAs) {
        String dateFull = "";
        try {

            LocalDate setDate = buildDate(format, dateAs, by);
            return setDate.format(DateTimeFormatter.ofPattern(format));

        } catch (Exception a) {
            log.error(a.getMessage());
            return dateFull;
        }
    }

    public static LocalDate setDate(int year, int atMonth, int day) {
        LocalDate localDate = Year.of(year)
                .atMonth(atMonth)
                .atDay(day);
        log.info("getDate before: " + localDate.toString());
        return localDate;
    }

    /**
     * @param monthText as flag
     * @param increment as flag
     * @param by as int
     * @return incremented or decremented day by days
     */
    public static String setDateByBusinessDay(boolean monthText, boolean increment, CalendarPeriod period, int by) {
        String date = setDate(monthText, increment, period, by);

        if (date.equals(lastOfTheWeeksDays().getLeft())
                || date.equals(lastOfTheWeeksDays().getRight())) {
            date = setDate(monthText, increment, period, 3);
        }

        return date;
    }

    /**
     * @param format as date format
     * @param increment as flag
     * @param by as int
     * @return incremented or decremented day by days
     */
    public static String setDateByBusinessDay(String format, boolean increment, CalendarPeriod period, int by) {
        String date = setDate(format, increment, period, by);
        if (date.equals(lastOfTheWeeksDays(format, increment).getLeft())
                || date.equals(lastOfTheWeeksDays(format, increment).getRight())) {
            date = setDate(format, increment, period, 3);
        }
        return date;
    }

    /**
     * @return pair of FRIDAY/SATURDAY
     */
    public static Pair<String,String> lastOfTheWeeksDays() {
        LocalDate fridayDate = LocalDate.now().with(nextOrSame(DayOfWeek.FRIDAY));
        DateTimeFormatter formatterF = DateTimeFormatter.ofPattern(WITH_DOT_MONTH_AS_TEXT_DD_MMMM_YYYY.getPattern());
        String friday = fridayDate.format(formatterF);

        LocalDate saturdayDate = LocalDate.now().with(nextOrSame(DayOfWeek.SATURDAY));
        DateTimeFormatter formatterS = DateTimeFormatter.ofPattern(WITH_DOT_MONTH_AS_TEXT_DD_MMMM_YYYY.getPattern());
        String saturday = saturdayDate.format(formatterS);

        return Pair.of(friday, saturday);
    }

    /**
     * @return pair of FRIDAY/SATURDAY
     */
    public static Pair<String,String> lastOfTheWeeksDays(String pattern, boolean future) {
        String friday, saturday;

        if (future) {

            LocalDate fridayDateFuture = LocalDate.now().with(nextOrSame(DayOfWeek.FRIDAY));
            DateTimeFormatter fridayDateFutureFormat = DateTimeFormatter.ofPattern(pattern);
            friday = fridayDateFuture.format(fridayDateFutureFormat);

            LocalDate saturdayDateFuture = LocalDate.now().with(nextOrSame(DayOfWeek.SATURDAY));
            DateTimeFormatter saturdayDateFutureFormat = DateTimeFormatter.ofPattern(pattern);
            saturday = saturdayDateFuture.format(saturdayDateFutureFormat);

        } else {

            LocalDate fridayDatePass = LocalDate.now().with(previousOrSame(DayOfWeek.FRIDAY));
            DateTimeFormatter fridayDatePassFormat = DateTimeFormatter.ofPattern(pattern);
            friday = fridayDatePass.format(fridayDatePassFormat);

            LocalDate saturdayDatePass = LocalDate.now().with(previousOrSame(DayOfWeek.SATURDAY));
            DateTimeFormatter saturdayDatePassFormat = DateTimeFormatter.ofPattern(pattern);
            saturday = saturdayDatePass.format(saturdayDatePassFormat);
        }

        return Pair.of(friday, saturday);
    }

    /**
     * @param format "dd.MM.yyyy" etc
     * @return fix after SimpleDateFormat
     */
    public static String dateFormat(String format) {
        SimpleDateFormat date = new SimpleDateFormat(format);
        Date getDate = new Date(System.currentTimeMillis());
        return date.format(getDate);
    }

    /**
     * @param strFormat for get the data option
     * @return the last full date with the last date of the week
     */
    public static String lastDayOfTheWeek(String strFormat) {
        try {
            LocalDate lastDay = LocalDate.now().with(nextOrSame(DayOfWeek.SATURDAY));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(strFormat);
            String dateFix = lastDay.format(formatter);
            return fixDate(dateFix);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * @param monthAsText for get the data option
     * @return the last full date with the last date of the week
     */
    public static String dayOfTheWeek(boolean monthAsText, DayOfWeek dayOfWeek) {
        try {
            LocalDate lastDay = LocalDate.now().with(nextOrSame(dayOfWeek));
            String formatDay = monthAsText ? WITH_DOT_MONTH_AS_TEXT_DD_MMMM_YYYY.getFormat() : WITH_DOT_DD_MM_YY.getFormat();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDay);
            String dateFix = lastDay.format(formatter);
            return fixDate(dateFix);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * @param monthAsText for get the data option
     * @return the last full date with the last date of the week
     */
    public static String lastDayOfTheWeek(boolean monthAsText) {
        try {
            LocalDate lastDay = LocalDate.now().with(nextOrSame(DayOfWeek.SATURDAY));
            String formatDay = monthAsText ? WITH_DOT_MONTH_AS_TEXT_DD_MMMM_YYYY.getFormat() : WITH_DOT_DD_MM_YY.getFormat();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDay);
            String dateFix = lastDay.format(formatter);
            return fixDate(dateFix);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * For short cut this object set the option for ->
     * @param formatDayString full string format
     * @param increment of decrement the days or month or years by the bool flag
     * @param period DAYS/MONTHS/YEARS
     * @param options of decrement the days or month or years by ine value
     * @return calendarObject
     */
    private static Calendar setCalendarData(String formatDayString, boolean increment, int period, int options) {
        Calendar date = getCalendar();
        try {

            date.setTime(new SimpleDateFormat(formatDayString).parse(dateFormat(formatDayString)));
            if (increment) {
                date.add(period, options);
            } else date.add(period, -options);

        } catch (Exception a) {
            log.error(a.getMessage());
        }

        return date;
    }

    /**
     * @param date the date
     * @return fix date match to application string date format
     */
    public static String fixDate(String date) {
        if (date.contains(".")) date = date.replaceAll("\\."," ");
        return date;
    }

    /**
     * For get the date as numbers
     * @param data this
     * @return data fix
     */
    public static String setDateToNumberStr(String data) {
        String saveExpectedDateToLetters = data.replaceAll(SAVE_LETTERS_CHARS, "");
        return monthStringToNumberFormat(saveExpectedDateToLetters);
    }

    /**
     * @param month enter text and return string fix
     * @return the part of the month
     */
    public static String monthStringToNumberFormat(String month) {
        switch (month) {
            case "ינואר": case "January": return "01";
            case "פבואר": case "February": return "02";
            case "מרץ": case "March": return "03";
            case "אפריל": case "April": return  "04";
            case "מאי": case "May": return  "05";
            case "יוני": case "June": return  "06";
            case "יולי": case "July": return  "07";
            case "אוגוסט": case "August": return  "08";
            case "ספטמבר": case "September": return  "09";
            case "אוקטובר": case "October": return  "10";
            case "נובמבר": case "November": return  "11";
            case "דצמבר": case "December": return  "12";
        }
        return "";
    }

    public static String hebrewShortMonthsNamesToEnglishMonthName(String month) {
        switch (month) {
            case "ינו׳":  return  "January";
            case "פבר׳": return "February";
            case "מרץ": return "March";
            case "אפר׳": return "April";
            case "מאי": return "May";
            case "יוני": return "June";
            case "יולי": return "July";
            case "אוג׳":  return "August";
            case "ספט׳": return "September";
            case "אוק׳": return "October";
            case "נוב׳":  return "November";
            case "דצמ׳": return "December";
        }
        return "";
    }

    public static String hebrewShortMonthsNamesToHebrewMonthName(String month) {
        switch (month) {
            case "January": return "ינו׳";
            case "February": return "פבר׳";
            case "March": return "מרץ";
            case "April": return "אפר׳";
            case "May": return "מאי";
            case "June": return "יוני";
            case "July": return "יולי";
            case "August": return "אוג׳";
            case "September": return "ספט׳";
            case "October": return "אוק׳";
            case "November": return "נוב׳";
            case "December": return "דצמ׳";
        }
        return "";
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

    public static LocalDate localDateBuilder(String format) {
        return LocalDate.parse(dateFormat(format), DateTimeFormatter.ofPattern(format));
    }

    public static LocalDate buildDate(String format, DateAs daysAs, int by) {
        LocalDate setDate = LocalDate.parse(dateFormat(format), DateTimeFormatter.ofPattern(format));

        switch (daysAs) {
            case PLUS_DAYS:
                setDate = setDate.plusDays(by);
                break;
            case MINUS_DAYS:
                setDate = setDate.minusDays(by);
                break;
            case PLUS_MONTHS:
                setDate = setDate.plusMonths(by);
                break;
            case MINUS_MONTHS:
                setDate = setDate.minusMonths(by);
                break;
            case PLUS_YEARS:
                setDate = setDate.plusYears(by);
                break;
            case MINUS_YEARS:
                setDate = setDate.minusYears(by);
                break;
            case MINUS_WEEKS:
                setDate = setDate.minusWeeks(by);
                break;
            case PLUS_WEEKS:
                setDate = setDate.plusWeeks(by);
                break;
        }

        return setDate;
    }

    public static String getMonthOrYearName(Locale locale, DateNameFrom dateNameFrom, int at) {
        String setData = "";

        try {
            DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
            switch (dateNameFrom) {
                case MONTH:
                    setData = dateFormatSymbols.getMonths()[at -1];
                    break;
                case SHORT_MONTH:
                    setData = dateFormatSymbols.getShortMonths()[at -1];
                    break;
                case DAY:
                    setData = dateFormatSymbols.getWeekdays()[at -1];
                    break;
                case SHORT_DAY:
                    setData = dateFormatSymbols.getShortWeekdays()[at -1];
                    break;
            }
        } catch (Exception e) {
            return "";
        }

        return setData;
    }

    /**
     * generateFullDateFromDayAndMonth
     * @param dayPattern regex
     * @param monthPattern regex
     * @param yearPattern regex
     * @param fullStr will parse to numeric and hebrew date
     * @return fix date with space between each day month and year
     */
    public static String generateFullDateFromDayAndMonth(
            String dayPattern, String monthPattern, String yearPattern, String fullStr) {
        try {
            String day = fullStr.replaceAll(dayPattern,"");
            String month = fullStr.replaceAll(monthPattern,"");
            month = hebrewShortMonthsNamesToEnglishMonthName(month);
            return day.concat(" " + month).concat(" " + dateFormat(yearPattern));
        } catch (Exception e) {
            log.error("generateFullDateFromDayAndMonth error: " + e.getMessage());
            return "";
        }
    }

    public enum DateNameFrom {
        MONTH,
        SHORT_MONTH,
        DAY,
        SHORT_DAY
    }

    public enum DateAs {
        MINUS_DAYS,
        PLUS_DAYS,
        MINUS_MONTHS,
        PLUS_MONTHS,
        MINUS_YEARS,
        PLUS_YEARS,
        MINUS_WEEKS,
        PLUS_WEEKS
    }
}
