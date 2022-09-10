package base;

import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public class StepFlowExtensions extends MobileWebDriverManager {

    private Status status = Status.FAIL;
    private String testId = "";
    private String stepId = "";
    private String stepDescription = "";
    private TestCategory testCategory;
    private TestSeverity testSeverity;

    public StepFlowExtensions setTestId(String testId) {
        this.testId = testId;
        return this;
    }

    public StepFlowExtensions setStep(String stepId, String stepDescription, Status status) {
        this.stepId = stepId;
        this.stepDescription = stepDescription;
        this.status = status;
        return this;
    }

    public StepFlowExtensions setStepCategory(TestCategory testCategory, TestSeverity testSeverity) {
        this.testCategory = testCategory;
        this.testSeverity = testSeverity;
        extentTest.assignCategory(this.testCategory.getText());
        return this;
    }

    /**
     * generic step for call any mobile extension objects
     * @param <T>      any object
     * @param consumer your function to be execute
     * @param obj1     your object instance type object
     * @return Consumer this
     */
    @Description("free step ")
    public <T> StepFlowExtensions step(T obj1, Consumer<T> consumer) {
        boolean pass = true;
        String message = "";
        try {
            consumer.accept(obj1);
            pass = true;
        } catch (Exception e) {
            pass = false;
            message = e.getMessage();
        } finally {
            if (pass) {
                reportStepTest(new ReasonsStep(Status.PASS, this.testId, this.stepId, this.testCategory, this.testSeverity, this.stepDescription + " " + message));
            } else {
                reportStepTest(new ReasonsStep(this.status, this.testId, this.stepId, this.testCategory, this.testSeverity, this.stepDescription + " " + message));
            }
        }
        return this;
    }

    /**
     * freeStep
     * generic step for call any mobile extension objects
     * @param <T>        any object
     * @param <V>        any object
     * @param biConsumer your function to be execute
     * @param obj1       your object instance type object
     * @return Consumer function
     */
    @Description("free step ")
    public <T, V> StepFlowExtensions step(T obj1, V obj2, BiConsumer<T, V> biConsumer) {
        boolean pass = true;
        String message = "";
        try {
            biConsumer.accept(obj1, obj2);
        } catch (Exception e) {
            pass = false;
            message = e.getMessage();
        } finally {
            if (pass) {
                reportStepTest(new ReasonsStep(Status.PASS, this.testId, this.stepId, this.testCategory, this.testSeverity, this.stepDescription + " " + message));
            } else {
                reportStepTest(new ReasonsStep(this.status, this.testId, this.stepId, this.testCategory, this.testSeverity, this.stepDescription + " " + message));
            }
        }
        return this;
    }
}
