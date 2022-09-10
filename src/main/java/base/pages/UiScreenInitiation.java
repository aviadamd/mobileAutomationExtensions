package base.pages;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class UiScreenInitiation {
    public UiScreenInitiation(WebDriver driver){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
}
