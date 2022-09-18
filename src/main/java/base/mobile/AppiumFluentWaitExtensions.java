package base.mobile;

import base.driversManager.MobileManager;
import io.appium.java_client.AppiumFluentWait;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Sleeper;
import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import static java.util.Arrays.asList;

@Slf4j
public class AppiumFluentWaitExtensions extends MobileManager {

    private final Clock clock;
    private final Sleeper sleeper;
    private Duration pollingEvery;
    private Duration withPollingStrategy;

    public AppiumFluentWait<WebDriver> appiumFluentWait() {
        AppiumFluentWait<WebDriver> appiumFluentWait = new AppiumFluentWait<>(getDriver());
        appiumFluentWait
                .withTimeout(this.withPollingStrategy)
                .pollingEvery(this.pollingEvery)
                .ignoreAll(this.errors);
        return appiumFluentWait;
    }

    private ArrayList<Class<? extends Exception>> errors = new ArrayList<>(asList(
            Exception.class,
            WebDriverException.class,
            TimeoutException.class,
            java.util.concurrent.TimeoutException.class,
            ConcurrentModificationException.class
    ));

    public AppiumFluentWaitExtensions() {
        this.pollingEvery = Duration.ofMillis(500);
        this.withPollingStrategy = Duration.ofSeconds(2);
        this.clock = Clock.systemDefaultZone();
        this.sleeper = duration -> this.sleepSeconds(1);
    }

    public AppiumFluentWaitExtensions(Clock clock, Sleeper sleeper) {
        this.pollingEvery = Duration.ofMillis(500);
        this.withPollingStrategy = Duration.ofSeconds(2);
        this.clock = clock;
        this.sleeper = sleeper;
    }

    public AppiumFluentWaitExtensions pollingEvery(Duration pollingEvery) {
        this.pollingEvery = pollingEvery;
        return this;
    }

    public AppiumFluentWaitExtensions withGeneralPollingWaitStrategy(Duration withPollingStrategy) {
        this.withPollingStrategy = withPollingStrategy;
        return this;
    }

    public AppiumFluentWaitExtensions setIgnoreClasses(ArrayList<Class<? extends Exception>> errors) {
        this.errors = errors;
        return this;
    }
}
