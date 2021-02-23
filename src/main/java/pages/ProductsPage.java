package pages;

import com.qa.BaseTest;
import com.qa.MenuPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsPage extends MenuPage {

    @AndroidFindBy(xpath ="//android.widget.ScrollView[@content-desc=\"test-PRODUCTS\"]/preceding-sibling::android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView" ) private MobileElement productTitle;
    @AndroidFindBy(xpath ="(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]\n" ) private MobileElement SLBTitle;
    @AndroidFindBy(xpath ="(//android.widget.TextView[@content-desc=\"test-Price\"])[1]\n" ) private MobileElement SLBPrice;

    public String getTitle(){
        return getAttribute(productTitle,"text");
    }
    public String getSLBTitle(){
        return getAttribute(SLBTitle,"text");
    }
    public String getSLBPrice(){
        return getAttribute(SLBPrice,"text");
    }
    public ProductDetailsPage pressSLBTitle(){
        click(SLBTitle);
        return new ProductDetailsPage();

    }

}
