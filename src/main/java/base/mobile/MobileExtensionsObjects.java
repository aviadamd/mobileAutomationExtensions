package base.mobile;

import base.driversManager.MobileManager;
import base.mobile.findElements.*;
import base.mobile.infrastarcture.DeepLinksExtensions;
import base.mobile.infrastarcture.InfraStructuresExtensions;
import com.aventstack.extentreports.Status;

public class MobileExtensionsObjects extends MobileManager {

    private String stepNumber = "";

    public MobileExtensionsObjects setStep(String stepNumber) {
        this.stepNumber = stepNumber;
        return this;
    }
    public final ElementGetTextsExtensions elementGetTextsExtensions;
    public final ElementsSearchExtensions elementsSearchExtensions;
    public final DeepLinksExtensions deepLinksExtensions;
    public final ClickElementExtensions clickElementExtensions;
    public final AppiumFluentWaitExtensions appiumFluentWaitExtensions;
    public final InfraStructuresExtensions infraStructuresExtensions;
    public final VerificationsTextsExtensions verificationsTextsExtensions;
    public final MutualSwipeGestureExtensions mutualSwipeGestureExtensions;

    public MobileExtensionsObjects() {
        this.elementGetTextsExtensions = new ElementGetTextsExtensions();
        this.elementsSearchExtensions = new ElementsSearchExtensions();
        this.deepLinksExtensions = new DeepLinksExtensions();
        this.appiumFluentWaitExtensions = new AppiumFluentWaitExtensions();
        this.clickElementExtensions = new ClickElementExtensions();
        this.infraStructuresExtensions = new InfraStructuresExtensions();
        this.verificationsTextsExtensions = new VerificationsTextsExtensions();
        this.mutualSwipeGestureExtensions = new MutualSwipeGestureExtensions();
    }
}
