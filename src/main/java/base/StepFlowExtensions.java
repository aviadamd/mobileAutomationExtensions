package base;

import base.driversManager.MobileWebDriverManager;
import base.mobile.*;
import base.reports.extentManager.ExtentLogger;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import java.util.function.Consumer;

@Slf4j
public class StepFlowExtensions extends MobileWebDriverManager {

    public final MobileExtensionsObjects extensions;

    public StepFlowExtensions() {
        this.extensions = new MobileExtensionsObjects();
    }

    /**
     * generic step for call any mobile extension objects
     * @param consumer your function to be
     * @return Consumer this
     */
    @Description("step ")
    public StepFlowExtensions step(String stepId, String stepDescription, Consumer<MobileExtensionsObjects> consumer) {
        try {
            ExtentLogger.loggerPrint(Status.INFO, "execute step id: " + stepId + ", description: " + stepDescription);
            consumer.accept(new MobileExtensionsObjects());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        ExtentLogger.loggerPrint(Status.INFO, "step id: " + stepId + ", description: " + stepDescription + " finish ");
        return this;
    }
}
