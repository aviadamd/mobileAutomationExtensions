package base;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class InstanceManager<T> {

    private final static Map<Integer, WebDriver> insertWebDriverInstance = new HashMap<>();

    public static void addWebDriver(WebDriver driver) {
        Integer threadId = (int) (Thread.currentThread().getId());
        insertWebDriverInstance.put(threadId, driver);
    }

    public static WebDriver getInsertInstance() {
        Integer threadId = (int) (Thread.currentThread().getId());
        return insertWebDriverInstance.get(threadId);
    }
}
