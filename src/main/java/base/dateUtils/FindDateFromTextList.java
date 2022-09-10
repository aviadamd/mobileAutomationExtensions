package base.dateUtils;

public class FindDateFromTextList {

    private final String date;
    private final boolean isFind;

    public FindDateFromTextList(String date, boolean isFind) {
        this.date = date;
        this.isFind = isFind;
    }

    public String getDate() { return date; }
    public boolean isFind() { return isFind; }

}
