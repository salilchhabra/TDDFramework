package pages;

import com.qa.MenuPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductDetailsPage extends MenuPage {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]\n")
    private MobileElement SLBTitle;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]\n")
    private MobileElement SLBText;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-BACK TO PRODUCTS\"]/android.widget.TextView")
    private MobileElement backToProductsButton;
    @AndroidFindBy(accessibility = "test-Price")
    private MobileElement SLBPrice;


    public String getSLBTitle() {
        return getAttribute(SLBTitle, "text");
    }

    public String getSLBText() {
        return getAttribute(SLBText, "text");
    }

    public ProductsPage pressBackToProductsPage() {
        click(backToProductsButton);
        return new ProductsPage();
    }
    public String getSLBPrice() {
        return getAttribute(SLBPrice, "text");
    }
    public ProductDetailsPage scrollToSLBPrice(){
        scrollToElement();
        return this;
    }

}
