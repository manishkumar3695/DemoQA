package com.qa.demo.Utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.util.HashMap;

import static com.qa.demo.Utils.FrameworkUtil.getConfig;
import static org.openqa.selenium.remote.DesiredCapabilities.chrome;

public class DriverUtil {
    private static HashMap<String, WebDriver> drivermap;
    private static DriverUtil instance;

    private DriverUtil() {
    }

    public static synchronized DriverUtil getInstance() {
        if (instance == null) {
            instance = new DriverUtil();
            drivermap = new HashMap<>();
        }
        return instance;
    }

    public static synchronized WebDriver getDriver(String... param) {
        String tID = "";
        String browser = "";
        try {
            tID = String.valueOf(Thread.currentThread().getId());
            if (!drivermap.containsKey(tID)) {
                if (param.length != 0) {
                    browser = param[0];
                } else {
                    browser = getConfig("Browser");
                }

                if (browser.equalsIgnoreCase("CHROME")) {
                    ChromeOptions o = new ChromeOptions();
                    o.addArguments("--no-sandbox");
                    WebDriverManager.chromedriver().setup();
                    WebDriver driver = new ChromeDriver(o);
                    drivermap.put(tID, driver);

                } else if (browser.equalsIgnoreCase("Fire")) {
                    WebDriverManager.firefoxdriver().setup();
                    WebDriver driver = new FirefoxDriver();
                    drivermap.put(tID, driver);

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return drivermap.get(tID);
        }


    }
}
