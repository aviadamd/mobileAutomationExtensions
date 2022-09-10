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
 
## repo link TODO
It uses different libraries and design to extend the base mobile project

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
    TODO
####


