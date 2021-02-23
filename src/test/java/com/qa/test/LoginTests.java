package com.qa.test;

import com.qa.BaseTest;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProductsPage;
import utils.TestUtils;

import java.io.InputStream;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    JSONObject loginUsers;
    TestUtils utils=new TestUtils();

    @BeforeClass
    public void beforeClass() throws Exception {
        InputStream datais = null;
        try {
            String dataFileName = "data/loginUser.json";
            datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(datais);
            loginUsers = new JSONObject(tokener);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (datais != null) {
                datais.close();
            }
        }
        closeApp();
        launchApp();
    }

    @AfterClass
    public void afterClass() {
    }

    @BeforeMethod
    public void beforeMethod(Method m) {
//        utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");
        utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");
        loginPage = new LoginPage();
    }

    @AfterMethod
    public void afterMethod() {
    }

    @Test
    public void invalidUserName() {
        loginPage.enterUsername(loginUsers.getJSONObject("invalidUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
        loginPage.pressLoginButton();
        String actualErrTxt = loginPage.getErrorText();
        String expectedErrText = getStrings().get("err_invalid_username_or_password");
        Assert.assertEquals(actualErrTxt, expectedErrText);
    }

    @Test
    public void invalidPassword() {
        loginPage.enterUsername(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginButton();
        String actualErrTxt = loginPage.getErrorText();
        String expectedErrText = getStrings().get("err_invalid_username_or_password");
        Assert.assertEquals(actualErrTxt, expectedErrText);
    }

    @Test
    public void successfulLogin() {
        loginPage.enterUsername(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.pressLoginButton();
        String actualProductTitle = productsPage.getTitle();
        String expectedProductTitle = getStrings().get("product_title");
        utils.log().info("Actual product title:" + actualProductTitle);
        Assert.assertEquals(actualProductTitle, expectedProductTitle);
    }
}
