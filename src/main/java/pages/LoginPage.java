package pages;

import com.qa.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import utils.TestUtils;

public class LoginPage extends BaseTest {
    @AndroidFindBy(accessibility ="test-Username" ) private MobileElement userNameTextField;
    @AndroidFindBy(accessibility ="test-Password" ) private MobileElement passwordField;
    @AndroidFindBy(accessibility ="test-LOGIN" ) private MobileElement loginButton;
    @AndroidFindBy(xpath ="//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView\n" ) private MobileElement errText;

    public LoginPage enterUsername(String userName){
        clear(userNameTextField);
        sendKeys(userNameTextField,userName,"login with username "+userName);
        return this;
    }
    public LoginPage enterPassword(String password){
        clear(passwordField);
        sendKeys(passwordField,password,"login with password "+password);
        return this;
    }
    public ProductsPage pressLoginButton(){
        click(loginButton,"login button pressed");
        return new ProductsPage();
    }
    public String getErrorText(){
        return getAttribute(errText,"text");
    }

    public ProductsPage login(String userName,String password){
        enterUsername(userName);
        enterPassword(password);
        return pressLoginButton();
    }
}
