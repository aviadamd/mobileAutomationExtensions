package base.driversManager.appiumEntry;

public class LogTypeBy {

    public enum GetMainEntryLogBy {
        BROWSER("browser"),
        CLIENT("client"),
        DRIVER("driver"),
        PERFORMANCE("performance"),
        PROFILER("profiler"),
        SERVER("server"),
        LOG_CAT_OR_SYS_LOG("logcat");

        private final String value;
        GetMainEntryLogBy(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    public enum AndroidLogCatTags {
        FIREBASE_INSTANCE_ID("FirebaseInstanceId"),
        NETWORK_MONITOR("NetworkMonitor"),
        APPIUM_RESPONSE("server"),
        ASSETS_STORAGE_CACHE("ASSETS STORAGE CACHE"),
        APPLICATION("com.ideomobile.hapoalim"),
        ACTIVITY_TASK_MANAGER("ActivityTaskManager"),
        CAA_A_OK_INTERCEPTOR("caa-aOkInterceptor"),
        CAA_A_OK_CALLBACK("aOkCallback"),
        OK_HTTP("OkHttp"),
        ACTIVITY_MANAGER("ActivityManager");

        private final String value;
        AndroidLogCatTags(String value) { this.value = value; }
        public String getValue() { return value; }
    }


    public enum IosSysLogTags {
        FIREBASE_INSTANCE_ID("FirebaseInstanceId"),
        NETWORK_MONITOR("NetworkMonitor"),
        APPIUM_RESPONSE("AppiumResponse"),
        ASSETS_STORAGE_CACHE("ASSETS STORAGE CACHE"),
        APPLICATION("EP.hapoalim.hapoalim"),
        ACTIVITY_TASK_MANAGER("ActivityTaskManager"),
        CAA_A_OK_INTERCEPTOR("caa-aOkInterceptor"),
        CAA_A_OK_CALLBACK("aOkCallback"),
        OK_HTTP("OkHttp"),
        ACTIVITY_MANAGER("ActivityManager");

        private final String value;
        IosSysLogTags(String value) { this.value = value; }
        public String getValue() { return value; }
    }
}
