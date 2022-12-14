package base.reports.testFilters;

public enum TestSeverity {
    PASS("PASS"),
    NONE("NONE"),
    LOW("LOW"),
    MIDDLE("MIDDLE"),
    HIGH("HIGH");

    private final String text;
    TestSeverity(String text) { this.text = text; }
    public String getText() { return text; }
}
