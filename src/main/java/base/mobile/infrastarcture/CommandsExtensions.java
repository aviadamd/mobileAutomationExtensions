package base.mobile.infrastarcture;

import lombok.Data;
import org.openqa.selenium.remote.DriverCommand;

@Data
public class CommandsExtensions implements DriverCommand {

    public static class AndroidCommands {
        public static String shell = "shell";
        public static String scrollBackTo = "scrollBackTo";
        public static String viewportScreenshot = "viewportScreenshot";
        public static String deepLink = "deepLink";
        public static String startLogsBroadcast = "startLogsBroadcast";
        public static String stopLogsBroadcast = "stopLogsBroadcast";
        public static String acceptAlert = "acceptAlert";
        public static String dismissAlert = "dismissAlert";
        public static String batteryInfo = "batteryInfo";
        public static String deviceInfo = "deviceInfo";
        public static String changePermissions = "changePermissions";
        public static String getPermissions = "getPermissions";
        public static String performEditorAction = "performEditorAction";
    }

    public static class IosCommands {
        public static String scroll = "scroll";
        public static String swipe = "swipe";
        public static String pinch = "pinch";
        public static String doubleTap = "doubleTap";
        public static String twoFingerTap = "twoFingerTap";
        public static String touchAndHold = "touchAndHold";
        public static String tap = "tap";
        public static String dragFromToForDuration = "dragFromToForDuration";
        public static String selectPickerWheelValue = "selectPickerWheelValue";
        public static String startLogsBroadcast = "startLogsBroadcast";
        public static String stopLogsBroadcast = "stopLogsBroadcast";
        public static String alert = "alert";
        public static String setPasteboard = "setPasteboard";
        public static String getPasteboard = "getPasteboard";
        public static String source = "source";
        public static String getContexts = "getContexts";
        public static String installApp = "installApp";
        public static String isAppInstalled = "isAppInstalled";
        public static String removeApp = "removeApp";
        public static String launchApp = "launchApp";
        public static String terminateApp = "terminateApp";
        public static String queryAppState = "queryAppState";
        public static String activateApp = "activateApp";
        public static String viewportScreenshot = "viewportScreenshot";
        public static String startPerfRecord = "startPerfRecord";
        public static String stopPerfRecord = "stopPerfRecord";
        public static String installCertificate = "installCertificate";
        public static String batteryInfo = "batteryInfo";
        public static String deviceInfo = "deviceInfo";
        public static String activeAppInfo = "activeAppInfo";
        public static String pressButton = "pressButton";
        public static String enrollBiometric = "enrollBiometric";
        public static String sendBiometricMatch = "sendBiometricMatch";
        public static String isBiometricEnrolled = "isBiometricEnrolled";
        public static String clearKeychains = "mobile: clearKeychains";
        public static String getPermission = "getPermission";
        public static String siriCommand = "enrollBiomsiriCommandetric";
    }
}
