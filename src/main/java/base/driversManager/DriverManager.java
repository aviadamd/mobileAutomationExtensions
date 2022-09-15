package base.driversManager;

import org.openqa.selenium.WebDriver;
public class DriverManager {

    private DriverManager(){ }
    private final static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    static WebDriver getLocalDriver() { return driver.get();}
    static void setLocalDriver(WebDriver test) { driver.set(test); }
    static void unloadLocalDriver() { driver.remove(); }
}
