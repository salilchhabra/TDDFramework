package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.HashMap;
import java.util.Map;

public class ExtentReport {
    static ExtentReports extentReport;
    final static String filePath = "Extent.html";
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();

    public synchronized static ExtentReports getReporter() {
        if (extentReport == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("Extent.html");
            sparkReporter.config().setDocumentTitle("Appium Framework");
            sparkReporter.config().setReportName("My App");
            sparkReporter.config().setTheme(Theme.STANDARD);
            extentReport = new ExtentReports();
            extentReport.attachReporter(sparkReporter);
        }
        return extentReport;
    }

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }
    public static synchronized ExtentTest startTest(String testName,String desc) {
        ExtentTest test=getReporter().createTest(testName,desc);
        extentTestMap.put((int) Thread.currentThread().getId(),test);
        return test;
    }

}
