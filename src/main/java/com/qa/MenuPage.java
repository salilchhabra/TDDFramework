package com.qa;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import pages.SettingsPage;

public class MenuPage extends BaseTest{
    @AndroidFindBy(xpath ="//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView\n" ) private MobileElement settingButton;
    public SettingsPage pressSettingsButton(){
        click(settingButton);
        return new SettingsPage();
    }
}
