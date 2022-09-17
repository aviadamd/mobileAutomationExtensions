package base.mobile.infrastarcture;

import base.mobile.MobileExtensionsObjects;
import base.mobile.elementsData.ElementsConstants.Android;
import base.mobile.enums.ScrollDirection;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import static com.aventstack.extentreports.Status.INFO;

public class PushPullFilesExtensions extends MobileExtensionsObjects {

    private int searchAttempts = 3;
    private boolean isClickOnFind = false;
    private String setFileLocalLocation = "";
    private String setFileDistentionLocation = "";
    private String setAppActivity = "";
    private String setBaseApplication = "";
    private String setApplicationToNavigate = "";

    public PushPullFilesExtensions setAppActivity(String setAppActivity) {
        this.setAppActivity = setAppActivity;
        return this;
    }

    public PushPullFilesExtensions setSearchAttempts(int searchAttempts) {
        this.searchAttempts = searchAttempts;
        return this;
    }

    public PushPullFilesExtensions setClickOnFind(boolean isClickOnFind) {
        this.isClickOnFind = isClickOnFind;
        return this;
    }

    public PushPullFilesExtensions setFileLocalLocation(String setFileLocalLocation) {
        this.setFileLocalLocation = setFileLocalLocation;
        return this;
    }

    public PushPullFilesExtensions setBaseApplication(String setBaseApplication) {
        this.setBaseApplication = setBaseApplication;
        return this;
    }

    public PushPullFilesExtensions setApplicationToNavigate(String setApplicationToNavigate) {
        this.setApplicationToNavigate = setApplicationToNavigate;
        return this;
    }

    public PushPullFilesExtensions setFileDistentionLocation(String setFileDistentionLocation) {
        this.setFileDistentionLocation = setFileDistentionLocation;
        return this;
    }

    public PushPullFilesExtensions pushFile() {
        InfraStructuresExtensions infraStructuresExtensions = new InfraStructuresExtensions();
        infraStructuresExtensions.pushFile(this.setFileDistentionLocation, this.setFileLocalLocation);
        return this;
    }

    public PushPullFilesExtensions pullFile() {
        InfraStructuresExtensions infraStructuresExtensions = new InfraStructuresExtensions();
        infraStructuresExtensions.pullFile(this.setFileDistentionLocation);
        return this;
    }

    public void fileSystemSearch(HighBarNavigation highBarNavigation, String fileMatch) {
        boolean find = false;
        String nameTemplate = "com.google.android.documentsui:id/nameplate";
        MobileExtensionsObjects extensions = new MobileExtensionsObjects();

        try {

            if (!this.setApplicationToNavigate.isEmpty() && !this.setAppActivity.isEmpty()) {
                extensions.infraStructuresExtensions.startActivity(this.setApplicationToNavigate, this.setAppActivity);
            } else if (!this.setApplicationToNavigate.isEmpty()){
                extensions.infraStructuresExtensions.activateApp(this.setApplicationToNavigate);
            }

            if (highBarNavigation != HighBarNavigation.IGNORE) {
                extensions.clickElementExtensions.clickElement(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='" + highBarNavigation.getName() + "']")),"");
            }

            String name;
            List<WebElement> items = extensions.elementsSearchExtensions
                    .fromManyToNestedMany(By.id(nameTemplate), By.className(Android.ClassType.TEXT_VIEW));
            if (items != null) {
                for (int i = 1; i < this.searchAttempts; i++) {
                    for (WebElement element : items) {
                        name = extensions.elementGetTextsExtensions.getValue(element, null).proceed();
                        if (extensions.verificationsTextsExtensions.isTextEquals(name, fileMatch, INFO)) {
                            if (this.isClickOnFind) extensions.clickElementExtensions.clickElement(ExpectedConditions.elementToBeClickable(element),"");
                            find = true;
                            break;
                        }
                    }
                    extensions.mutualSwipeGestureExtensions.swipe(!find, ScrollDirection.DOWN,"search: " + fileMatch);
                }
            } else this.onFail(fileMatch);
        } catch (Exception ex) {
            this.onFail(fileMatch);
        }

        if (!find) this.onFail(fileMatch);
        extensions.infraStructuresExtensions.activateApp(this.setBaseApplication);
    }

    public enum HighBarNavigation {
        IGNORE(""),
        DOCUMENTS("Documents"),
        IMAGES("Images"),
        AUDIO("Audio"),
        VIDEOS("Videos");

        private final String name;
        HighBarNavigation(String name) {
            this.name = name;
        }
        public String getName() { return name; }
    }

    private void onFail(String fileMatch) {
        InfraStructuresExtensions infraStructuresExtensions = new InfraStructuresExtensions();
        infraStructuresExtensions.activateApp(this.setBaseApplication);
       // report(LogStatus.FAIL,"unable to find file with name " + fileMatch + " from menu");
    }

}
