package base;

import base.mobile.ClickElementExtensions;
import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Description;
import java.util.function.Consumer;

@Slf4j
public class StepFlowExtensions extends MobileWebDriverManager {

    private Status statusOnFail = Status.FAIL;
    private String testId = "";
    private String stepId = "";
    private String stepDescription = "";
    public ClickElementExtensions clickElementExtensions;
    private ReasonsStep reasonsStep;

    public StepFlowExtensions() {
        this.clickElementExtensions = new ClickElementExtensions();
    }

    public StepFlowExtensions setTestId(String testId) {
        this.testId = testId;
        return this;
    }

    public StepFlowExtensions setStep(String stepId, String stepDescription, Status statusOnFail) {
        this.stepId = stepId;
        this.stepDescription = stepDescription;
        this.statusOnFail = statusOnFail;
        return this;
    }

    public IntegrateReportStepWithAction<StepFlowExtensions> clickBy(int timeOut, By element, String stepDescription) {
        this.reasonsStep = this.clickElementExtensions.setStep(this.stepId).setReportFromClass(false).clickElementBy(timeOut, element, stepDescription);

        String step = this.reasonsStep.getStatus() == Status.FAIL ? this.reasonsStep.getDescription() : this.stepDescription;
        Status status = this.reasonsStep.getStatus() == Status.FAIL && this.statusOnFail == Status.FAIL ? Status.FAIL : Status.INFO;
        this.reasonsStep = this.setReasonStep(this.testId, this.reasonsStep.getStepId(), step, status);

        this.reportStepTest(this.reasonsStep);
        return new IntegrateReportStepWithAction<>(this.reasonsStep, this);
    }

    private ReasonsStep setReasonStep(String testId, String stepId, String stepDesc, Status status) {
        this.reasonsStep.setTestId(testId);
        this.reasonsStep.setStepId(stepId);
        this.reasonsStep.setDescription(stepDesc);
        this.reasonsStep.setSeverity(this.reasonsStep.getSeverity());
        this.reasonsStep.setCategory(this.reasonsStep.getCategory());
        this.reasonsStep.setStatus(status);
        return this.reasonsStep;
    }

    /**
     * generic step for call any mobile extension objects
     * @param consumer your function to be execute
     * @return Consumer this
     */
    @Description("free step ")
    public StepFlowExtensions step(Consumer<StepFlowExtensions> consumer) {
        try {
            consumer.accept(this);
        } catch (Exception e) {
            //
        }
        return this;
    }
}
