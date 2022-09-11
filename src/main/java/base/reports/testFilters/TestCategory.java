package base.reports.testFilters;

public enum TestCategory {
    PASS("PASS"),
    NONE("NONE"),
    APPIUM("Appium"),
    APPIUM_SERVER("Appium Server"),
    DRIVER("Driver"),
    SELENIUM("Selenium"),
    INFRASTRUCTURE("Infrastructure"),
    SERVICE("Service"),
    APPLICATION("Application");

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
