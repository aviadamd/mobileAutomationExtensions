package base.propertyConfig;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertyConfig {

    private String platformType;
    private String remotePlatformType;
    private String localBin;
    private String javaHome;
    private String androidSdk;
    private String nodeJs;
    private String appPath;
    private String deviceName;
    private String hubHost;
    private String isRunProxy;
    private int appiumPort;
    private int appiumStartUpTimeOut;
    private String appiumMainJsPath;
    private String appiumIpAddress;
    private String logFileLocation;
    private String mongoDbName;
    private String mongoDbCollectionName;
    private String mongoDbStringConnection;
    private String reportJsonTemplatePath;
    private String extentSparkPath;
    private String appNamePackage;

    public void setProperty(String key, String value) {
        String prop = System.setProperty(key, value);
        if (prop.isEmpty()) {
            log.error("cannot set property with: " + key + " and value: " + value);
        } else log.info("set property with: " + key + " and value: " + value);
    }

    public String getPlatformType() { return this.platformType; }
    public String getRemotePlatformType() { return this.remotePlatformType; }
    public String getLocalBin() { return this.localBin; }
    public String getJavaHome() { return this.javaHome; }
    public String getAndroidSdk() { return this.androidSdk; }
    public String getNodeJs() { return this.nodeJs; }
    public String getAppPath() { return this.appPath; }
    public String getDeviceName() { return this.deviceName; }
    public String getHubHost() { return this.hubHost; }
    public String getIsRunProxy() { return this.isRunProxy; }
    public int getAppiumPort() { return this.appiumPort; }
    public int getAppiumStartUpTimeOut() { return this.appiumStartUpTimeOut; }
    public String getAppiumMainJsPath() { return appiumMainJsPath; }
    public String getAppiumIpAddress() { return this.appiumIpAddress; }
    public String getLogFileLocation() { return this.logFileLocation; }
    public String getMongoDbName() { return mongoDbName; }
    public String getMongoDbCollectionName() { return this.mongoDbCollectionName; }
    public String getMongoDbStringConnection() { return this.mongoDbStringConnection; }
    public String getReportJsonTemplatePath() { return this.reportJsonTemplatePath; }
    public String getExtentSparkPath() { return this.extentSparkPath; }
    public String getAppNamePackage() { return this.appNamePackage; }

    public PropertyConfig() {
        String root = System.getProperty("user.dir");
        String appConfigPath = root + "/src/main/resources/config.properties";
        Properties appProps = new Properties();

        try {
            appProps.load(new FileInputStream(appConfigPath));
            this.platformType = appProps.getProperty("device.platformType");
            this.remotePlatformType = appProps.getProperty("device.remote.platformType");
            this.localBin = appProps.getProperty("path.localBin");
            this.androidSdk = appProps.getProperty("path.androidSdk");
            this.nodeJs = appProps.getProperty("path.nodeJs");
            this.appPath = appProps.getProperty("path.appPath");
            this.hubHost = appProps.getProperty("hobHost");
            this.isRunProxy = appProps.getProperty("proxy.isRunProxy");
            this.appiumPort = Integer.parseInt(appProps.getProperty("appium.appiumPort"));
            this.appiumMainJsPath = appProps.getProperty("path.appiumMainJsPath");
            this.appiumIpAddress = appProps.getProperty("appium.appiumIpAddress");
            this.logFileLocation = appProps.getProperty("path.logFileLocation");
            this.appiumStartUpTimeOut = Integer.parseInt(appProps.getProperty("appium.appiumStartUpTimeOut"));
            this.javaHome = appProps.getProperty("path.javaHome");
            this.deviceName = appProps.getProperty("device.name");
            this.mongoDbName = appProps.getProperty("mongo.dbName");
            this.mongoDbCollectionName = appProps.getProperty("mongo.db.collectionName");
            this.mongoDbStringConnection = appProps.getProperty("mongodb.uri");
            this.reportJsonTemplatePath = appProps.getProperty("path.json.report.config");
            this.extentSparkPath = appProps.getProperty("path.extent.spark");
            this.appNamePackage = appProps.getProperty("app.name.package");
        } catch (IOException ioException) {
            log.error("init property files fails " + ioException.getMessage());
        }
    }
}
