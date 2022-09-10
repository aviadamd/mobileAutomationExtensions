package base.mobile;

import base.MobileWebDriverManager;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationLauncherExtensions extends MobileWebDriverManager {

    private String setAppActivity = "";
    private String setApplicationToNavigate = "";

    public ApplicationLauncherExtensions setAppActivity(String setAppActivity) {
        this.setAppActivity = setAppActivity;
        return this;
    }

    public ApplicationLauncherExtensions setApplicationToNavigate(String setApplicationToNavigate) {
        this.setApplicationToNavigate = setApplicationToNavigate;
        return this;
    }

    public void launch(LaunchAppOptions appOptions, String packageNameValidation) {
        InfraStructuresExtensions infraStructuresExtensions = new InfraStructuresExtensions();
        VerificationsTextsExtensions verificationsTextsExtensions = new VerificationsTextsExtensions();
        switch (appOptions) {
            case ACTIVATE_WITH_PACKAGE_NAME: {
                if (!this.setApplicationToNavigate.isEmpty())
                    infraStructuresExtensions.activateApp(this.setApplicationToNavigate);
                break;
            }
            case ACTIVATE_WITH_PACKAGE_NAME_AND_ACTIVITY: {
                if (!this.setApplicationToNavigate.isEmpty() && !this.setAppActivity.isEmpty())
                    infraStructuresExtensions.startActivity(this.setApplicationToNavigate, this.setAppActivity);
                break;
            }
        }

        String getCurrent = infraStructuresExtensions.getCurrentAppPackage();
        if (verificationsTextsExtensions.isTextEquals(getCurrent, packageNameValidation, Status.INFO)) {
            log.info("pass launch app "+packageNameValidation+ " with "+appOptions.getDescription()+"");
        } else log.info("fail launch app "+packageNameValidation+ " with "+appOptions.getDescription()+"");
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
