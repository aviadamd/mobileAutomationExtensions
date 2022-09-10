package base.reports.testFilters;

import com.aventstack.extentreports.Status;
import com.mongodb.BasicDBObject;

public class Reasons extends BasicDBObject {

    static final long serialVersionUID = 2105061907470199595L;

    private final String testId;
    private final String testName;
    private Status testStatus;
    private TestCategory category;
    private TestSeverity severity;
    private String description;

    public Reasons(
            Status testStatus, String testId, String testName,
            TestCategory category, TestSeverity severity, String description) {
        this.testId = testId;
        this.testName = testName;
        this.testStatus = testStatus;
        this.category = category;
        this.severity = severity;
        this.description = description;
    }

    public String getTestId() { return testId; }
    public String getTestName() { return testName; }
    public Status getTestStatus() { return this.testStatus; }
    public TestCategory getCategory() { return this.category; }
    public TestSeverity getSeverity() { return this.severity; }
    public String getDescription() { return this.description; }

    public void setTestStatus(Status testStatus) {
        this.testStatus = testStatus;
    }
    public void setCategory(TestCategory category) { this.category = category; }
    public void setSeverity(TestSeverity severity) { this.severity = severity; }
    public void setDescription(String description) { this.description = description; }
}
