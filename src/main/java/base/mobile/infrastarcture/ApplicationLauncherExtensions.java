package base.mobile.infrastarcture;

import base.IntegrateReport;
import base.driversManager.MobileManager;
import base.mobile.findElements.VerificationsTextsExtensions;
import base.reports.testFilters.ReasonsStep;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;

public class ApplicationLauncherExtensions extends MobileManager {

    private String setAppActivity = "";
    public boolean isReportFromClass = true;
    private String setApplicationToNavigate = "";

    public ApplicationLauncherExtensions setAppActivity(String setAppActivity) {
        this.setAppActivity = setAppActivity;
        return this;
    }

    public ApplicationLauncherExtensions setApplicationToNavigate(String setApplicationToNavigate) {
        this.setApplicationToNavigate = setApplicationToNavigate;
        return this;
    }

    public ApplicationLauncherExtensions setIsReportFromClass(boolean isReportFromClass) {
        this.isReportFromClass = isReportFromClass;
        return this;
    }

    public IntegrateReport<ApplicationLauncherExtensions> launch(LaunchAppOptions appOptions, String packageNameValidation) {
        ReasonsStep step;

        switch (appOptions) {
            case ACTIVATE_WITH_PACKAGE_NAME: {
                if (!this.setApplicationToNavigate.isEmpty())
                    new InfraStructuresExtensions().activateApp(this.setApplicationToNavigate);
                break;
            }
            case ACTIVATE_WITH_PACKAGE_NAME_AND_ACTIVITY: {
                if (!this.setApplicationToNavigate.isEmpty() && !this.setAppActivity.isEmpty())
                    new InfraStructuresExtensions().startActivity(this.setApplicationToNavigate, this.setAppActivity);
                break;
            }
        }

        String getCurrent = new InfraStructuresExtensions().getCurrentAppPackage();
        if (new VerificationsTextsExtensions().isTextEquals(getCurrent, packageNameValidation, Status.INFO)) {
            step = new ReasonsStep(Status.PASS,"", TestCategory.NONE, TestSeverity.NONE, "pass launch app "+packageNameValidation+ " with "+appOptions.getDescription()+"");
        } else {
            step = new ReasonsStep(Status.FAIL,"", TestCategory.NONE, TestSeverity.NONE, "pass launch app "+packageNameValidation+ " with "+appOptions.getDescription()+"");
            if (this.isReportFromClass) reportStepTest(step);
        }
        return new IntegrateReport<>(step, this);
    }

    public void returnToBaseApplication() {
        new InfraStructuresExtensions().activateApp(this.setApplicationToNavigate);
    }

    public enum LaunchAppOptions {
        ACTIVATE_WITH_PACKAGE_NAME("ACTIVATE_WITH_PACKAGE_NAME"),
        ACTIVATE_WITH_PACKAGE_NAME_AND_ACTIVITY("ACTIVATE_WITH_PACKAGE_NAME_AND_ACTIVITY");
        private final String description;
        LaunchAppOptions(String description) { this.description = description; }
        public String getDescription() { return this.description; }
    }
}
