package base.mobile;

import base.MobileWebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.context.annotation.Description;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DeepLinksExtensions extends MobileWebDriverManager {

    private String deepLinkUrl;
    private String clientPackage;
    private int deepLinksNumber;

    public DeepLinksExtensions setDeepLinkUrl(String deepLinkUrl) {
        this.deepLinkUrl = deepLinkUrl;
        return this;
    }

    public DeepLinksExtensions setClientPackage(String clientPackage) {
        this.clientPackage = clientPackage;
        return this;
    }

    public DeepLinksExtensions setDeepLinksNumber(int deepLinksNumber) {
        this.deepLinksNumber = deepLinksNumber;
        return this;
    }

    @Description("set deep link params number")
    public void executeDeepLink() {
        String url = this.deepLinkUrl + this.deepLinksNumber;

        try {
            if (isAndroidClient()) {
                Map<String, Object> params = new HashMap<>();
                params.put("url", url);
                params.put("package", this.clientPackage);
                new InfraStructuresExtensions().executeScript("mobile: deepLink", params);
            } else {
                getDriver().get(url);
            }
        } catch (WebDriverException e) {
            log.debug( "warning deep link execution ["+url+"]");
        }
    }

    @Override
    public String toString() {
        return "DeepLinks{" +
                "deepLinkUrl='" + deepLinkUrl + '\'' +
                ", clientPackage='" + clientPackage + '\'' +
                ", deepLinksNumber=" + deepLinksNumber +
                '}';
    }
}
