package base.mobile;

import base.driversManager.MobileWebDriverManager;

public class MobileExtensionsObjects extends MobileWebDriverManager {

    public final ElementGetTextsExtensions elementGetTextsExtensions;
    public final ElementsSearchExtensions elementsSearchExtensions;
    public final DeepLinksExtensions deepLinksExtensions;
    public final ClickElementExtensions clickElementExtensions;
    public final AppiumFluentWaitExtensions appiumFluentWaitExtensions;

    public MobileExtensionsObjects() {
        this.elementGetTextsExtensions = new ElementGetTextsExtensions();
        this.elementsSearchExtensions = new ElementsSearchExtensions();
        this.deepLinksExtensions = new DeepLinksExtensions();
        this.appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
        this.clickElementExtensions = new ClickElementExtensions();
    }
}
