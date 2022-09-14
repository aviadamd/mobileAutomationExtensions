package base;

import base.mobile.*;
import base.reports.testFilters.Reasons;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import java.util.function.Consumer;

import static base.reports.extentManager.ExtentReportManager.extentTest;

@Slf4j
public class StepFlowExtensions extends MobileWebDriverManager {

    public final MobileExtensionsObjects extensions;

    public StepFlowExtensions() {
        this.extensions = new MobileExtensionsObjects();
    }

    /**
     * generic step for call any mobile extension objects
     * @param consumer your function to be execute
     * @return Consumer this
     */
    @Description("step ")
    public StepFlowExtensions step(String stepId, String stepDescription, Consumer<MobileExtensionsObjects> consumer) {
        try {
            extentTest.log(Status.INFO, "execute step id: " + stepId + ", description: " + stepDescription);
            consumer.accept(new MobileExtensionsObjects());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return this;
    }
}
