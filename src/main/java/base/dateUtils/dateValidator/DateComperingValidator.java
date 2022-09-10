package base.dateUtils.dateValidator;

import java.util.Date;

public class DateComperingValidator {

    private String pattern;
    private String date1;
    private String date2;
    private String date3;
    private Verify verify;

    public DateComperingValidator() {}

    public DateComperingValidator(String pattern, String date1, String date2, String date3) {
        this.pattern = pattern;
        this.date1 = date1;
        this.date2 = date2;
        this.date3 = date3;
    }

    public DateComperingValidator(String pattern, String date1, String date2, Verify verify) {
        this.pattern = pattern;
        this.date1 = date1;
        this.date2 = date2;
        this.verify = verify;
    }

    public boolean isDateMatch() {
        try {

            Date start = DateValidatorEx.setDate(this.pattern, this.date1);
            Date end = DateValidatorEx.setDate(this.pattern, this.date2);

            boolean find = DateValidatorEx.isDateValid(this.pattern, this.date1) && DateValidatorEx.isDateValid(this.pattern, this.date2);

            if (find) {
                final int zero = 0;
                switch (this.verify) {
                    case SMALLER: return DateValidatorEx.containsDate(start, end) < zero;
                    case GREATER: return DateValidatorEx.containsDate(start, end) > zero;
                    case EQUALS: return DateValidatorEx.containsDate(start, end) == zero;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public boolean isDateMatch(String pattern, String current, String expected, Verify verify) {
        try {

            Date start = DateValidatorEx.setDate(pattern, current);
            Date end = DateValidatorEx.setDate(pattern, expected);
            boolean find = DateValidatorEx.isDateValid(pattern, current) && DateValidatorEx.isDateValid(pattern, expected);

            if (find) {
                switch (verify) {
                    case SMALLER: return DateValidatorEx.containsDate(start, end) < 0;
                    case GREATER: return DateValidatorEx.containsDate(start, end) > 0;
                    case EQUALS: return DateValidatorEx.containsDate(start, end) == 0;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public boolean isDateInRangeOf() {
        Date actualDate = DateValidatorEx.setDate(this.pattern, this.date1);
        Date passDate = DateValidatorEx.setDate(this.pattern, this.date2);
        Date futureDate =  DateValidatorEx.setDate(this.pattern, this.date3);
        return DateValidatorEx.isDateRangeBetween(actualDate, passDate, futureDate);
    }

    public boolean isDateInRangeOf(String pattern, String date1, String date2, String date3) {
        Date actualDate = DateValidatorEx.setDate(pattern, date1);
        Date passDate = DateValidatorEx.setDate(pattern, date2);
        Date futureDate =  DateValidatorEx.setDate(pattern, date3);
        return DateValidatorEx.isDateRangeBetween(actualDate, passDate, futureDate);
    }

    public enum Verify {
        EQUALS,
        GREATER,
        SMALLER
    }

    @Override
    public String toString() {
        return "DateComperingValidator{" +
                "pattern='" + pattern + '\'' +
                ", date1='" + date1 + '\'' +
                ", date2='" + date2 + '\'' +
                ", verify=" + verify +
                '}';
    }
}
