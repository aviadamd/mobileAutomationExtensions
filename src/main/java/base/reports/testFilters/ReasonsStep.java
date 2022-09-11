package base.reports.testFilters;

import com.aventstack.extentreports.Status;
import com.mongodb.BasicDBObject;

public class ReasonsStep extends BasicDBObject {

    static final long serialVersionUID = 2105061917470199595L;
    private Status status;
    private String testId;
    private String stepId;
    private TestCategory category;
    private TestSeverity severity;
    private String description;

    public Status getStatus() { return status; }
    public String getTestId() { return testId; }
    public String getStepId() { return stepId; }
    public TestCategory getCategory() { return category; }
    public TestSeverity getSeverity() { return severity; }
    public String getDescription() { return description; }

    public ReasonsStep(
            Status status, String testId, String stepId,
            TestCategory category, TestSeverity severity, String description) {
        this.status = status;
        this.testId = testId;
        this.stepId = stepId;
        this.category = category;
        this.severity = severity;
        this.description = description;
    }

    public void setTestId(String testId) { this.testId = testId; }
    public void setStepId(String stepId) { this.stepId = stepId; }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setCategory(TestCategory category) { this.category = category; }
    public void setSeverity(TestSeverity severity) { this.severity = severity; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "ReasonsStep{" +
                "status=" + status +
                ", testId='" + testId + '\'' +
                ", stepId='" + stepId + '\'' +
                ", category=" + category +
                ", severity=" + severity +
                ", description='" + description + '\'' +
                '}';
    }
}
