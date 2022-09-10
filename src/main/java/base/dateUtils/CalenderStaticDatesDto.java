package base.dateUtils;

import base.mobile.enums.CalendarPeriod;
import base.mobile.enums.DateBy;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CalenderStaticDatesDto {

    protected int                            monthNumber;
    protected DateBy dateBy;
    protected int                   incrementDecrementBy;
    protected String                           monthName;
    protected String                          dateFormat;
    protected boolean            includeOnlyBusinessDays;
    protected CalendarPeriod periodIncrementDecrementBy;
}
