# Base Mobile Extensions

   * [Technical Decisions](#technical-decisions)
   * [Languages and Frameworks](#languages-and-frameworks)
   * [Test architecture](#test-architecture)
   * [What you need](#what-you-need)
   * [Configurations](#configurations)
   * [Inspect elements](#inspect-elements)
   * [iOS specific](#ios-specific)
   * [Configuration Run](#Configuration Run)
   * [Inspect elements on iOS](#inspect-elements-on-ios)
 
## repo link [v1.0.37](http://bitbucket:7990/projects/AUTO/repos/mobileutils/browse)
It uses different libraries and design to extend the base mobile project
http://bitbucket:7990/projects/AUTO/repos/mobileutils/browse

## Technical Decisions
This section will show to you the libraries, frameworks and design decisions that made a lean architecture possible.

### Languages and Frameworks
This project using the following languages and frameworks:

* [Java 11] as the programming language
* [JUnit] as the UnitTest framework to support the test creation
* [Appium] as the mobile test automation framework
* [Extent Report] as the testing report strategy
* [@Slf4j] as the logging manage strategy

### Test architecture
Following items in this architecture:

* Page Objects pattern
* Factory
* Logging
* Configuration files

### What you need
1. Java JDK 11 or JDK 8 installed and configured (with `JAVA_HOME` and `PATH` configured)
2. IDE (to import this project using Maven)
3. Android SDK (for Android execution | with `ANDROID_HOME` and `PATH` configured)
4. Android AVD created
5. XCode and the iPhone Simulator (for iOS execution)
6. Appium installed
   
##### Inspect elements
[Uiautomatorviewer] for fast to inspection on android elements.
[Appium Desktop] for inspection on android and ios elements.
[IOS Accibility incepetor] for fast to inspection on ios elements.

#### iOS specific
To execute the examples over the iOS platform you'll need:
* MacOS machine :-)
* Xcode installed
* iPhone simulator (I recommend, for these tests iOS version > 12)
* Follow all the steps on [https://github.com/appium/appium-xcuitest-driver](https://github.com/appium/appium-xcuitest-driver)

##### Inspect elements on iOS
You also can use [Appium Desktop](https://github.com/appium/appium-desktop)
or you can use the [Macaca App Inspector](https://macacajs.github.io/app-inspector/)
inspection on ios elements [IOS Accibility incepetor](on your mac)
   
#### Configuration Run from base amd mobile extensions
            
            -Dproject.platformType=ANDROID/iOS client options
            -Ddevice.name="emulator-5554" - android emulator name
            -Dproject.deviceName="Pixel_XL_API_27" - android emulator device name
            -DproxyPort="5559" - for appium port on local run no needed
            -Dproject.appiumPort="4723"  - for appium port on local run no needed
            -Ddevice.version.ios=14.2 - ios version
            -Dproject.xcodePath="/Applications/Xcode.app/Contents/Developer/usr/bin" - xcode ios path
            -Dpath.android=/Users/adamgoman/Downloads/account-management-v3-8930.apk - anroid apk path
            -Dpath.ios=/Users/adamgoman/Downloads/AccountManagement.app - ios app path
            -Dproject.customer.user.rbi=true - is user the rbi for login
            -Dproject.wso2.isProxyEnabled=false 
            -Dproject.customer.user.minions=false if true will check for minions page before each login
            -Dapp.serverName=mob3 minons server options
            -Djavax.net.ssl.trustStore=src/test/resources/myTrustedStore.jks
            -Djenkins.jobName="jobName" #used only for jenkins, if presented so will try to recover appium and emulator/simulator
            -Dretry=false #not requared, true in local use nad false in Jenkins   
                              
            -Dproject.customer.user.rbi=true will disable the rbi calls
            -Dproject.local=true will had extra project features like the proxy configuration
            -Dproject.local.proxy=false  will init the proxy params from local run
            -Dproject.customer.login.isCreateShortLogin=true will create an short login on test that have the same user property number
            -Dproject.customer.post.login.isTakeDefaultUser=true from the acount picker after login with rbi
            -Dproject.customer.user.validation=true will check if the property is valid before the login
            -Dproject.serverNameForAccountManagement=MOB3 minons server options
            -Dproject.customer.isLocal=false will had extra project features like the proxy configuration
            -Dproject.cleanEnv=true will reset the app before login step
            -Dproject.login.retry=3 will run 3 times until login pass
            -Dproject.postLoginExtraTimeOut=20 for login to for post login
            -Dproject.login.type.twoIdentifiers=true twoIdentifiers
            -Djavax.net.ssl.trustStore=/src/main/resources/myTrustedStore.jks for the rbi
            -Dapp.keyStorePath=src/test/resources/mobbiz.jks for the rbi
            -Dproject.applicationName = com.ideomobile.hapoalim the pacakage name
            -Dproject.log.listener = false will write to json file appium rest and application activity
            -Dproject.adb.path = Users/aviadbenshenon/Desktop/sdk_2/platform-tools/adb for find the executable adb  
####


