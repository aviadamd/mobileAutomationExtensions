package base.driversManager;

import base.reports.testFilters.Reasons;
import base.reports.testFilters.TestCategory;
import base.reports.testFilters.TestSeverity;
import com.aventstack.extentreports.Status;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static base.reports.extentManager.ExtentLogger.reportTest;

public class AppiumServerManager {

    private static Logger logger = LoggerFactory.getLogger(AppiumServerManager.class);

    private AppiumServerManager() {}
    private static final ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();

    public static void setServer(AppiumDriverLocalService server) {
        AppiumServerManager.server.set(server);
    }

    public static AppiumDriverLocalService getServer() {
        return server.get();
    }

    public static void startServer() {
        try {
            AppiumServerManager.getServer().start();
            if (AppiumServerManager.getServer().getUrl() != null && AppiumServerManager.isAppiumConnectionAlive(AppiumServerManager.getServer().getUrl())) {
                reportTest(new Reasons(Status.INFO,"init","init" ,TestCategory.APPIUM, TestSeverity.HIGH,"init appium server"));
            } else reportTest(new Reasons(Status.FAIL,"init","init" ,TestCategory.APPIUM, TestSeverity.HIGH,"fail init appium server"));
        } catch (Exception exception) {
            reportTest(new Reasons(Status.FAIL,"init","init" ,TestCategory.APPIUM, TestSeverity.HIGH,"fail init appium server"));
        }
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
                    .withArgument(GeneralServerFlag.LOCAL_TIMEZONE)
                    .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                    .withStartUpTimeOut(15, TimeUnit.SECONDS);
            builder.build().clearOutPutStreams();
        } catch (Exception appiumEx) {
            reportTest(new Reasons(Status.FAIL,"init","init" , TestCategory.APPIUM, TestSeverity.HIGH,"fail launch appium server"));
        }
        return AppiumDriverLocalService.buildService(builder);
    }

    static HashMap<String,String> environment(String path, String androidHome, String javaHome) {
        HashMap<String, String> environment = new HashMap<>();
        environment.put("PATH", path + System.getenv("PATH"));
        environment.put("ANDROID_HOME", androidHome);
        environment.put("JAVA_HOME", javaHome);
        return environment;
    }

    public static boolean isAppiumConnectionAlive(URL url) {
        try {
            String setUrl = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .port(url.getPort())
                    .host(url.getHost())
                    .path("wd").path("/hub").path("/status")
                    .toUriString();
            ResponseEntity<String> appiumResponse = new RestTemplate().getForEntity(setUrl, String.class);
            logger.info("Appium instance is up " + appiumResponse);
            return true;
        } catch (ResourceAccessException resourceAccessException) {
            logger.info("ResourceAccessException appium instance is down " + resourceAccessException.getMessage());
            return false;
        } catch (Exception exception) {
            logger.info("Exception general exception from verify is appium alive " + exception.getMessage());
            return false;
        }
    }

}
