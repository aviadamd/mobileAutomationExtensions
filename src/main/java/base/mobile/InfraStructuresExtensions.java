package base.mobile;

import base.driversManager.MobileWebDriverManager;
import base.mobile.enums.AppState;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.ScreenOrientation;
import org.springframework.context.annotation.Description;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Slf4j
public class InfraStructuresExtensions extends MobileWebDriverManager {

    @SuppressWarnings("rawtypes")
    public IOSDriver iosDriver() {
        return ((IOSDriver) getDriver());
    }

    @SuppressWarnings("rawtypes")
    public AndroidDriver androidDriver() {
        return ((AndroidDriver) getDriver());
    }

    @Description("getCurrentActivity ")
    public String getCurrentActivity() {
        try {
            return androidDriver().currentActivity();
        } catch (Exception ex) {
            log.error("Exception: error from getCurrentActivity " + ex.getMessage());
            return "";
        }
    }

    @Description("getCurrentAppPackage ")
    public String getCurrentAppPackage() {
        try {
            return androidDriver().getCurrentPackage();
        } catch (Exception ex) {
            log.error("Exception: error from getCurrentActivity " + ex.getMessage());
            return "";
        }
    }

    @Description("startActivity ")
    public void startActivity(String packageName, String activityName) {
        try {
            if (isAndroidClient()) {
                this.androidDriver().startActivity(new Activity(packageName, activityName));
            } else this.iosDriver().activateApp(packageName);
        } catch (Exception ex) {
            log.error("Exception: error from startActivity " + ex.getMessage());
        }
    }

    @Description("activateApp ")
    public void activateApp(String packageName) {
        try {
            if (isAndroidClient()) {
                this.androidDriver().activateApp(packageName);
            } else this.iosDriver().activateApp(packageName);
        } catch (Exception ex) {
            log.error("Exception: error from activateApp " + ex.getMessage());
        }
    }

    public void startUrlListener(String uri) {
        try {
            this.executeScript("script", ImmutableMap.of(CommandsExtensions.IosCommands.startLogsBroadcast,"args"));
            URI endPoint = new URI(uri);
            if (isAndroidClient()) {
                this.androidDriver().getLogcatClient().connect(endPoint);
            } else {
                this.iosDriver().getSyslogClient().connect(endPoint);
            }
        } catch (URISyntaxException e) {
            //
        }
    }

    @Description("pushFile")
    public void pushFile(String devicePath, String fileName) {
        try {
            if (isAndroidClient()) {
                androidDriver().pushFile(devicePath, new File(fileName));
            } else iosDriver().pushFile(devicePath, new File(fileName));

        } catch (IOException io) {

        } catch (Exception ex) {

        }
    }

    @Description("pushFile")
    public void pullFile(String devicePath) {
        try {
            if (isAndroidClient()) {
                androidDriver().pullFile(devicePath);
            } else iosDriver().pullFile(devicePath);
        } catch (Exception ex) {

        }
    }

    @Description("isAppInstalled")
    public boolean isAppInstalled(String bundleId) {
        try {
            if (isAndroidClient())
                return this.androidDriver().isAppInstalled(bundleId);
            return this.iosDriver().isAppInstalled(bundleId);
        } catch (Exception ex) {

        }
        return false;
    }

    @Description("getDeviceTime")
    public String getDeviceTime(String format) {
        String time = "";
        try {
            time = isAndroidClient() ? this.androidDriver().getDeviceTime(format) : this.iosDriver().getDeviceTime(format);
        } catch (Exception e) {

        }
        return time;
    }

    @Description("isOrientationIsPortrait")
    public boolean isOrientationIsPortrait() {
        try {
            String orientation = isAndroidClient() ? this.androidDriver().getOrientation().name() : this.iosDriver().getOrientation().name();
            ScreenOrientation screenActual = ScreenOrientation.valueOf(orientation);
            return screenActual == ScreenOrientation.PORTRAIT;
        } catch (Exception e) {
        }
        return false;
    }

    @Description("rotateDevice")
    public void rotateDevice(ScreenOrientation orientation) {
        try {
            if (isAndroidClient()) {
                this.androidDriver().rotate(orientation);
            } else this.iosDriver().rotate(orientation);
        } catch (Exception e) {

        }
    }

    @Description("getAutomationName")
    public String getAutomationName() {
        String automationName = "";
        try {
            if (isAndroidClient()) {
                automationName = this.androidDriver().getAutomationName();
            } else automationName = this.iosDriver().getAutomationName();
        } catch (Exception e) {

        }

        return automationName;
    }

    @Description("changeContext")
    public void changeContextTo(String context) {
        try {
            if (isAndroidClient()) {
                this.androidDriver().getContextHandles();
                this.androidDriver().context(context);
            } else {
                this.iosDriver().getContextHandles();
                this.iosDriver().context(context);
            }
        } catch (Exception e) {

        }
    }

    @SuppressWarnings("rawtypes")
    @Description("getContextHandles")
    public Set getContextHandles() {
        Set contexts = new HashSet<>();
        try {
            if (isAndroidClient()) {
                contexts = this.androidDriver().getContextHandles();
            } else {
                contexts = this.iosDriver().getContextHandles();
            }
        } catch (Exception e) {

        }

        return contexts;
    }

    @Description("getAppContext")
    public String getAppContext() {
        String contexts = "";
        try {
            if (isAndroidClient()) {
                contexts = this.androidDriver().getContext();
            } else {
                contexts = this.iosDriver().getContext();
            }
        } catch (Exception e) {

        }

        return contexts;
    }

    @Description("isDeviceIsLocked")
    public boolean isDeviceIsLocked() {
        try {
            if (isAndroidClient()) {
                 this.androidDriver().isDeviceLocked();
            } else {
                 this.iosDriver().isDeviceLocked();
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Description("lockDevice")
    public void lockDevice(long mils) {
        try {
            if (isAndroidClient()) {
                this.androidDriver().lockDevice(Duration.ofMillis(mils));
            } else {
                this.iosDriver().lockDevice(Duration.ofMillis(mils));
            }
        } catch (Exception e) {

        }
    }

    /**
     *  @return the application state for the report
     *  NOT_INSTALLED,
     *  NOT_RUNNING,
     *  RUNNING_IN_BACKGROUND_SUSPENDED,
     *  RUNNING_IN_BACKGROUND,
     *  RUNNING_IN_FOREGROUND
     */
    public String getApplicationState(String setApplicationPackage) {
        String state = this.applicationState(setApplicationPackage);
        if (state == null || state.isEmpty()) {
            return "unknown application state";
        }

        return state;
    }

    private void launchAppBackUp(String setApplicationPackage) {
        String appState = getApplicationState(setApplicationPackage);

        if (appState != null && !appState.isEmpty()) {

            Pair<Boolean,String> messages = Pair.of(false,"AppState.RUNNING_IN_FOREGROUND : ");

            if (appState.equals(AppState.RUNNING_IN_FOREGROUND.getText())) {
                return;
            }

            if (appState.equals(AppState.NOT_RUNNING.getText()))
                messages = Pair.of(true,AppState.NOT_RUNNING.getText());

            else if (appState.equals(AppState.RUNNING_IN_BACKGROUND_SUSPENDED.getText()))
                messages = Pair.of(true,AppState.RUNNING_IN_BACKGROUND_SUSPENDED.getText());

            else if (appState.equals(AppState.RUNNING_IN_BACKGROUND.getText()))
                messages = Pair.of(true,AppState.RUNNING_IN_BACKGROUND.getText());

            else if (appState.equals(AppState.NOT_INSTALLED.getText()))
                messages = Pair.of(true,AppState.NOT_INSTALLED.getText());

            if (messages.getLeft()) this.launchApp();
        } else this.launchApp();
    }

    public void launchApp() {
        try {
            if (isAndroidClient()) {
                this.androidDriver().launchApp();
            } else this.iosDriver().launchApp();
        } catch (Exception e) {
            log.error("launchApp error" + e.getMessage());
        }
    }

    public Map<String,Object> iosPackage(Map<String, Object> extra, String iosPackage) {
        Map<String, Object> params = new HashMap<>();
        params.put("package", iosPackage);
        ofNullable(extra).ifPresent(e -> e.putAll(extra));
        return params;
    }

    public Map<String,Object> androidPackage(Map<String, Object> extra, String androidPackage) {
        Map<String, Object> params = new HashMap<>();
        params.put("package", androidPackage);
        ofNullable(extra).ifPresent(e -> e.putAll(extra));
        return params;
    }

    public String getPageSource(int timeOut) {
        try {
            if (getDriver().getPageSource() != null) {
                return getDriver().getPageSource();
            }
        } catch (Exception e) {
            log.error("application page source is not available: " + e.getMessage());
        }

        return "";
    }

    public boolean checkPageSourceMessage(int intervalTimeOut, String message) {
        String pageSource = getPageSource(intervalTimeOut);
        return !pageSource.isEmpty() && pageSource.contains(message);
    }

    public boolean checkPageSourceMessage(int intervalTimeOut, List<String> message) {
        boolean findError = false;
        String pageSource = getPageSource(intervalTimeOut);
        for (String txt: message) {
            if (!pageSource.isEmpty() && pageSource.contains(txt)) {
                findError = true;
                break;
            }
        }
        return findError;
    }

    @SuppressWarnings("rawtypes")
    public void executeScript(String script, Map<String, Object> params) {
        try {
            if (isAndroidClient()) {
                ((AndroidDriver) getDriver()).executeScript(script, params);
            } else {
                ((IOSDriver) getDriver()).executeScript(script, params);
            }
        } catch (Exception e) {
            log.error("executeScript script warning");
        }
    }

    public String applicationState(String applicationState) {
        ApplicationState state = null;

        try {

            if (isAndroidClient())
                state = androidDriver().queryAppState(applicationState);
            else state = iosDriver().queryAppState(applicationState);

        } catch (Exception s) {
            log.debug("take state error " + s.getMessage());
        } finally {
            if (state != null && !state.toString().isEmpty()) {
                if (state.toString().equals(AppState.NOT_RUNNING.getText())
                        || state.toString().equals(AppState.RUNNING_IN_FOREGROUND.getText())) {
                    if (isAndroidClient())
                        state = androidDriver().queryAppState(applicationState);
                    else state = iosDriver().queryAppState(applicationState);
                }
            }
        }

        if (state != null) return state.toString();
        return "fail to take state";
    }

    public void executeRuntimeCommand(String command) {
        if (isAndroidClient()) {
            try {
                log.info("execute runtime command : " + command);
                Runtime.getRuntime().exec(command);
            } catch (IOException | SecurityException e) {
            }
        }
    }

    public static String executeCommand(String command) {
        try {
            Process process = (new ProcessBuilder(new String[0])).command("zsh", "-c", command).start();
            log.debug("Executing command : '" + command + "' results =  " + (!process.waitFor(20L, TimeUnit.SECONDS) ? "still running..." : process.exitValue()));
            return !process.isAlive() ? (String)(new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n")) : "Command still running : " + command;
        } catch (Exception var2) {
            var2.printStackTrace();
            log.debug("Command: '" + command + "' execution failed', " + getCauseIfPresent(var2).toString());
            return "Command: '" + command + "' execution failed', " + getCauseIfPresent(var2).toString();
        }
    }

    private static Throwable getCauseIfPresent(Throwable throwable) {
        return throwable.getCause() != null ? throwable.getCause() : throwable;
    }
}
