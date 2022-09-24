package base.driversManager;

import base.reports.testFilters.Reasons;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static base.reports.extentManager.ExtentLogger.reportTest;

public class AppiumServerManager {

    private AppiumServerManager() {}
    private static ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();

    public static void setServer(AppiumDriverLocalService server) {
        AppiumServerManager.server.set(server);
    }

    public static AppiumDriverLocalService getServer() {
        return server.get();
    }

    public static void flush() {
        server.remove();
    }

    static AppiumDriverLocalService initServer(String nodeJsPath, String appiumMainJsPath) {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        try {
            builder.usingDriverExecutable(new File(nodeJsPath))
                    .withAppiumJS(new File(appiumMainJsPath))
                    .usingAnyFreePort()
                    .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                    .withStartUpTimeOut(15, TimeUnit.SECONDS);
        } catch (Exception appiumEx) {
            reportTest(new Reasons(Status.FAIL,"init","init" , TestCategory.APPIUM, TestSeverity.HIGH,"fail launch appium server"));
        }
        return AppiumDriverLocalService.buildService(builder);
    }

    static HashMap<String,String> environment(String path, String androidHome, String javaHome) {
        HashMap<String, String> environment = new HashMap<>();
      //  environment.put("PATH", getProperty().getLocalBin() + System.getenv("PATH"));
      //  environment.put("ANDROID_HOME", getProperty().getAndroidSdk());
      //  environment.put("JAVA_HOME", getProperty().getJavaHome());

        environment.put("PATH", path + System.getenv("PATH"));
        environment.put("ANDROID_HOME", androidHome);
        environment.put("JAVA_HOME", javaHome);
        return environment;
    }
}
