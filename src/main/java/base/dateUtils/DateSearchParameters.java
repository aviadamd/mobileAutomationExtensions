package base.dateUtils;

import base.mobile.enums.CalendarPeriod;

import static base.dateUtils.CalendarStaticData.setDate;

public class DateSearchParameters {

    private String dateFormat;
    private boolean incrementDate;
    private CalendarPeriod calendarPeriod;
    private int incrementDateBy;

    public DateSearchParameters(String dateFormat,  boolean incrementDate, CalendarPeriod calendarPeriod, int incrementDateBy) {
        this.dateFormat = dateFormat;
        this.incrementDate = incrementDate;
        this.calendarPeriod = calendarPeriod;
        this.incrementDateBy = incrementDateBy;
    }

    public String getDate() {
        return setDate(this.dateFormat, this.incrementDate, this.calendarPeriod, this.incrementDateBy);
    }

    public String getDateFormat() {
       return this.dateFormat;
    }
}
