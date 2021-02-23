package com.qa;

import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reports.ExtentReport;
import utils.TestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseTest {
    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<Properties> props = new ThreadLocal<>();
    protected static ThreadLocal<String> dateTime = new ThreadLocal<>();
    protected static ThreadLocal<String> deviceName = new ThreadLocal<>();

    protected static ThreadLocal<HashMap<String, String>> strings = new ThreadLocal<HashMap<String, String>>();
    private static AppiumDriverLocalService server;
    private TestUtils utils = new TestUtils();

    public BaseTest() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2) {
        driver.set(driver2);
    }

    public Properties getProps() {
        return props.get();
    }

    public void setProps(Properties props2) {
        props.set(props2);
    }

    public HashMap<String, String> getStrings() {
        return strings.get();
    }

    public void setStrings(HashMap<String, String> strings2) {
        strings.set(strings2);
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public void setDateTime(String dateTime2) {
        dateTime.set(dateTime2);
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(String deviceName2) {
        deviceName.set(deviceName2);
    }

    @BeforeSuite
    public void beforeSuite() {
        server = getAppiumService();
        server.start();
        server.clearOutPutStreams();
        System.out.println("appium server started");
    }

    @AfterSuite
    public void afterSuite() {
        server.stop();
        System.out.println("appium server stopped");

    }

    public AppiumDriverLocalService getAppiumService() {
        HashMap<String, String> environment = new HashMap<>();
        environment.put("PATH", "/Users/salilchhabra/Library/Java/JavaVirtualMachines/openjdk-15/Contents/Home/bin:/Users/salilchhabra/Library/Android/sdk/tools:/Users/salilchhabra/Library/Android/sdk/platform-tools:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin" + System.getenv("PATH"));
        environment.put("ANDROID_HOME", "/Users/salilchhabra/Library/Android/sdk");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withEnvironment(environment)
                .withLogFile(new File("ServerLogs/server.log")));
    }

    @Parameters({"emulator", "platformName", "deviceName", "udid", "systemPort", "chromeDriverPort"})
    @BeforeClass
    public void beforeTest(String emulator, String platformName, String deviceName, String udid, String systemPort, String chromeDriverPort) throws IOException {
        InputStream stringis = null;
        InputStream inputStream = null;
        setDateTime(utils.dateTime());
        setDeviceName(deviceName);
        try {
            setProps(new Properties());
            String propFileName = "config.properties";
            String xmlFileName = "strings/strings.xml";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            getProps().load(inputStream);
            stringis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            setStrings(utils.parseStringXML(stringis));
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, getProps().getProperty("androidAutomationName"));
            caps.setCapability("ignoreHiddenApiPolicyError", "true");
            caps.setCapability(MobileCapabilityType.UDID, udid);
            caps.setCapability("appPackage", getProps().getProperty("androidAppPackage"));
            caps.setCapability("appActivity", getProps().getProperty("androidAppActivity"));
            caps.setCapability("systemPort", systemPort);
            caps.setCapability("chromeDriverPort", chromeDriverPort);
            if (emulator.equalsIgnoreCase("true")) {
                caps.setCapability("avd", deviceName);
            }
            String appUrl = getClass().getResource(getProps().getProperty("androidAppLocation")).getFile();
            caps.setCapability(MobileCapabilityType.APP, appUrl);
//        start appium server
            URL url = new URL(getProps().getProperty("appiumURL"));
            //To create new driver session, we need to create appiumdriver object
            setDriver(new AndroidDriver(url, caps));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (stringis != null) {
                stringis.close();
            }
        }

    }

    public void waitForVisibility(MobileElement e) {
        WebDriverWait wait = new WebDriverWait(getDriver(), TestUtils.WAIT);
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void clear(MobileElement e) {
        waitForVisibility(e);
        e.clear();
    }

    public void click(MobileElement e) {
        waitForVisibility(e);
        e.click();
    }

    public void click(MobileElement e, String logMessage) {
        waitForVisibility(e);
        utils.log().info(logMessage);
        if (ExtentReport.getTest() != null) {
            ExtentReport.getTest().log(Status.INFO, logMessage);
        }
        e.click();
    }

    public void sendKeys(MobileElement e, String text) {
        waitForVisibility(e);
        e.sendKeys(text);
    }

    public void sendKeys(MobileElement e, String text, String logMessage) {
        waitForVisibility(e);
        utils.log().info(logMessage);
        if (ExtentReport.getTest() != null) {
            ExtentReport.getTest().log(Status.INFO, logMessage);
        }
        e.sendKeys(text);
    }

    public String getAttribute(MobileElement e, String attribute) {
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    public void closeApp() {
        getDriver().closeApp();
    }

    public void launchApp() {
        getDriver().launchApp();
    }

    public MobileElement scrollToElement() {
        //if there is only 1 scrollable element on page , then no need to find parent element
        return (MobileElement) ((FindsByAndroidUIAutomator) getDriver()).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
                        + "new UiSelector().description(\"test-Price\"));");

        //if there is more than one scrollable element
       /* return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".description(\"test-inventory item page\")).scrollIntoView("
                        + "new UiSelector().description(\"test-Price\"));");*/
    }

    @Parameters({"emulator"})
    @BeforeMethod
    public void beforeMethod(String emulator) {
        if (emulator.equalsIgnoreCase("true")) {
            ((CanRecordScreen) getDriver()).startRecordingScreen();
        }
    }

    //stop video capturing and create *.mp4 file
    @Parameters({"emulator"})
    @AfterMethod
    public synchronized void afterMethod(String emulator, ITestResult result) throws Exception {
        if (emulator.equalsIgnoreCase("true")) {
            String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();
            if (result.getStatus() == 2) {
                Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
                String dirPath = "videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName")
                        + File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();

                File videoDir = new File(dirPath);

                synchronized (videoDir) {
                    if (!videoDir.exists()) {
                        videoDir.mkdirs();
                    }
                }
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
                    stream.write(Base64.getDecoder().decode(media));
                    stream.close();
//            utils.log().info("video path: " + videoDir + File.separator + result.getName() + ".mp4");
                } catch (Exception e) {
//            utils.log().error("error during video capture" + e.toString());
                } finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
            }
        }
    }

    @AfterClass
    public void afterTest() {
        getDriver().quit();
    }
}
