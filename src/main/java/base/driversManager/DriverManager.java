package base.driversManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class DriverManager {
    private DriverManager() {}
    private final static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    static WebDriver getLocalDriver() { return driver.get();}
    static void setLocalDriver(WebDriver test) { driver.set(test); }
    static void unloadLocalDriver() { driver.remove(); }
    private final static ThreadLocal<EventFiringWebDriver> eventFiringWebDriver = new ThreadLocal<>();
    static EventFiringWebDriver getEventFiringWebDriver() { return eventFiringWebDriver.get(); }
    static void setEventFiringWebDriver(EventFiringWebDriver eventFiringWeb) { eventFiringWebDriver.set(eventFiringWeb); }
    static void unloadLocalEvent() { eventFiringWebDriver.remove(); }
}
