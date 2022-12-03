package base.reports.testFilters;

public enum  TestCategory {
    PASS("PASS"),
    NONE("NONE"),
    APPIUM("APPIUM"),
    APPIUM_SERVER("APPIUM SERVER"),
    DRIVER("DRIVER"),
    SELENIUM("SELENIUM"),
    INFRASTRUCTURE("INFRASTRUCTURE"),
    SERVICE("SERVICE"),
    APPLICATION("APPLICATION");
    private final String text;
    TestCategory(String text) { this.text = text; }
    public String getText() { return text; }

    @Override
    public String toString() {
        return "TestCategory{" +
                "text='" + text + '\'' +
                '}';
    }
}
