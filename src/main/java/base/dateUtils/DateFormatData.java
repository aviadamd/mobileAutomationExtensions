package base.dateUtils;

import base.anontations.Info;

/**
 * this class return the option to get date format with the date pattern
 */
public class DateFormatData {

    public enum Formats {
        WITH_OUT_SEPARATION_DD_MM_YYYY("ddMMyyyy","\\d{2}.\\d{2}.\\d{4}"),
        WITH_DOT_DD_MM_YYYY("dd.MM.yyyy","\\d{2}.\\d{2}.\\d{4}"),
        WITH_SLASH_DD_MM_YY("dd/MM/yy","\\d{2}/\\d{2}/\\d{2}"),
        WITH_DOT_DD_MM_YY("dd.MM.yy","\\d{2}.\\d{2}.\\d{2}"),
        WITH_MONTH_AS_TEXT_DD_MMMM_YYYY("dd MMMM yyyy","\\d{2} \\d{4} \\d{4}"),
        WITH_DOT_MONTH_AS_TEXT_DD_MMMM_YYYY("dd.MMMM.yyyy","\\d{2}.\\d{4}.\\d{4}"),
        WITH_SLASH_MONTH_AS_TEXT_DD_MMMM_YYYY("dd/MMMM/yyyy","\\d{2}/\\d{4}/\\d{4}"),
        WITH_HYPhEN_MONTH_AS_TEXT_DD_MMMM_YYYY("dd-MMMM-yyyy","\\d{2}-\\d{4}-\\d{4}"),
        WITH_YEAR_YYYY("yyyy","\\d{2}"),
        WITH_YEAR_YY("yy","\\d{2}"),
        WITH_DAY_DD("dd","\\d{2}"),
        WITH_MONTH_MM("MM","\\d{2}"),
        WITH_MONTH_MMMM("MMMM","\\d{2}");

        private final String format, pattern;
        Formats(String format, String pattern) {
            this.format = format;
            this.pattern = pattern;
        }

        public String getFormat() { return format; }
        public String getPattern() { return pattern; }
    }
}
