package base.dateUtils;

import base.mobile.enums.CalendarPeriod;
import base.mobile.enums.DateBy;

public class CalenderStaticDatesGenerator extends CalenderStaticDatesDto {

    public CalenderStaticDatesGenerator(CalenderDatesBuilder calenderDatesBuilder) {
        this.periodIncrementDecrementBy = calenderDatesBuilder.periodIncrementDecrementBy;
        this.dateBy = calenderDatesBuilder.dateBy;
        this.dateFormat = calenderDatesBuilder.dateFormat;
        this.incrementDecrementBy = calenderDatesBuilder.incrementDecrementBy;
        this.includeOnlyBusinessDays = calenderDatesBuilder.includeOnlyBusinessDays;
    }

    public static class CalenderDatesBuilder {
        private CalendarPeriod periodIncrementDecrementBy = CalendarPeriod.DAY;
        private DateBy dateBy;
        private String dateFormat;
        private int incrementDecrementBy;
        private boolean includeOnlyBusinessDays;

        public CalenderDatesBuilder() { }

        public CalenderDatesBuilder setIncludeOnlyBusinessDays(boolean includeOnlyBusinessDays) {
            this.includeOnlyBusinessDays = includeOnlyBusinessDays;
            return this;
        }

        public CalenderDatesBuilder setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
            return this;
        }

        public CalenderDatesBuilder setIncrementDecrementBy(int incrementDecrementBy, DateBy dateBy, CalendarPeriod periodIncrementDecrementBy) {
            this.incrementDecrementBy = incrementDecrementBy;
            this.dateBy = dateBy;
            this.periodIncrementDecrementBy = periodIncrementDecrementBy;
            return this;
        }

        private String getDate(String dateFormat) {
            return this.includeOnlyBusinessDays ? CalendarStaticData.setDateByBusinessDay(
                    dateFormat,
                    this.dateBy == DateBy.FUTURE_DATE,
                    this.periodIncrementDecrementBy,
                    this.incrementDecrementBy
            ) : CalendarStaticData.setDate(
                    dateFormat,
                    this.dateBy == DateBy.FUTURE_DATE,
                    this.periodIncrementDecrementBy,
                    this.incrementDecrementBy
            );
        }

        public String build() {
            CalenderStaticDatesGenerator calenderStaticDatesGenerator = new CalenderStaticDatesGenerator(this);
            return this.getDate(calenderStaticDatesGenerator.dateFormat);
        }
    }
}
