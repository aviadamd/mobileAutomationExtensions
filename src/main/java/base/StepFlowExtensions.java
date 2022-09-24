package base;

import base.driversManager.MobileManager;
import base.mobile.*;
import base.reports.extentManager.ExtentLogger;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.util.function.Consumer;

@Slf4j
public class StepFlowExtensions extends MobileExtensionsObjects {

    @BeforeSuite
    @BeforeClass
    public void init() {
        MobileManager.getDriver();
    }
    public MobileExtensionsObjects extensions;
    public StepFlowExtensions() {
        if (this.extensions == null) {
            this.extensions = new MobileExtensionsObjects();
        }
    }

    /**
     * generic step for call any mobile extension objects
     * @param consumer your function to be
     * @return Consumer this
     */
    public StepFlowExtensions steps(String stepId, String stepDescription, Consumer<MobileExtensionsObjects> consumer) {
        try {
            ExtentLogger.loggerPrint(Status.INFO, "execute steps id: " + stepId + ", description: " + stepDescription);
            consumer.accept(new MobileExtensionsObjects());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        ExtentLogger.loggerPrint(Status.INFO, "execution of steps id: " + stepId + ", description: " + stepDescription + " finish");
        return this;
    }
}
