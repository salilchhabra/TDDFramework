package com.qa.test;

import com.qa.BaseTest;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import pages.ProductDetailsPage;
import pages.ProductsPage;
import pages.SettingsPage;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    SettingsPage settingsPage;
    ProductDetailsPage productDetailsPage;
    JSONObject loginUsers;

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
//		  utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");
        loginPage = new LoginPage();
        productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),
                loginUsers.getJSONObject("validUser").getString("password"));
    }

    @AfterMethod
    public void afterMethod() {
        settingsPage = productsPage.pressSettingsButton();
        loginPage = settingsPage.pressLogoutButton();
    }

    @Test
    public void validateProductOnProductsPage() {
        SoftAssert sa = new SoftAssert();

        String SLBTitle = productsPage.getSLBTitle();
        sa.assertEquals(SLBTitle, getStrings().get("product_page_slb_title"));

        String SLBPrice = productsPage.getSLBPrice();
        sa.assertEquals(SLBPrice, getStrings().get("product_page_slb_price"));
        sa.assertAll();
    }

    @Test
    public void validateProductOnProductDetailsPage() {
        SoftAssert sa = new SoftAssert();

        productDetailsPage = productsPage.pressSLBTitle();

        String SLBTitle = productDetailsPage.getSLBTitle();
        sa.assertEquals(SLBTitle, getStrings().get("product_details_page_slb_title"));

//        String SLBDesc = productDetailsPage.getSLBText();

//        sa.assertEquals(SLBDesc, getStrings().get("product_details_page_slb_desc"));
        productDetailsPage.scrollToSLBPrice();
        String SLBPrice = productDetailsPage.getSLBPrice();
        sa.assertEquals(SLBPrice, getStrings().get("product_page_slb_price"));

        productsPage = productDetailsPage.pressBackToProductsPage();

        sa.assertAll();
    }
}
