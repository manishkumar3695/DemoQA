package com.qa.demo.Utils;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharSet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.qa.demo.Utils.ConfigUtil.configIterator;
import static com.qa.demo.Utils.DriverUtil.getDriver;
import static com.qa.demo.Utils.FrameworkUtil.getConfig;
import static com.qa.demo.Utils.FrameworkUtil.masterWait;
import static com.qa.demo.Utils.LoggerUtil.logger;

public class ElementUtil {
    public static int longWait, medWait, shortWait;
    private static ElementUtil instance;
    public static HashMap<String, HashMap<String, String>> locatorMap = new HashMap<>();
    public static final String locatorPath = "locators/";

    private ElementUtil() {
    }

    public static ElementUtil getInstance() {
        if (instance == null) {
            instance = new ElementUtil();
            setLocatorMap();
            longWait = Integer.parseInt(getConfig("LongWait"));
        }
        return instance;
    }

    public static void setLocatorMap() {
        String value = "";
        try {
            if (locatorMap.isEmpty()) {
                InputStream is = ElementUtil.class.getClassLoader().getResourceAsStream(locatorPath);
                List<String> files = IOUtils.readLines(is, Charsets.UTF_8);
                for (String fileName : files) {
                    locatorMap.put(fileName, configIterator(locatorPath + fileName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(ElementUtil.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClassName();
            }
        }
        return null;
    }

    public static String getLocator(String... elemInfo) {
        String locator = "";
        String className = getCallerClassName();
        String propFile = className.replace("StepDefinitions", "");
        propFile = propFile.replace("StepDef", "").replace("com.qa.demo.", "");
        propFile = propFile + ".properties";
        if (locatorMap.containsKey(propFile) && locatorMap.get(propFile).containsKey(elemInfo[0])) {
            locator = locatorMap.get(propFile).get(elemInfo[0]);
        }
        if (locator.equals("")) {
            for (Map.Entry<String, HashMap<String, String>> entry : locatorMap.entrySet()) {
                if (entry.getValue().containsKey(elemInfo[0])) {
                    locator = entry.getValue().get(elemInfo[0]);
                    propFile = entry.getKey();
                    break;
                }
            }
        }
        if (elemInfo.length == 2) {
            locator = locator.replaceAll("(?i)XXXX", elemInfo[1]);
        } else if (elemInfo.length > 2 && locator.contains("1111")) {
            for (int i = 1; i < elemInfo.length; i++) {
                String s = String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i);
                locator.replaceAll(s, elemInfo[i]);
            }
        }
        logger.info("locator : " + locator + " ;; propfile : " + propFile + ";; callerclass : " + className);
        return locator;
    }

    public static WebElement getElement(String... elemInfo) {
        String locator = "";
        WebElement elem = null;
        try {
            getInstance();
            locator = getLocator(elemInfo);
            int waitCounter = 0;
            while (getDriver().findElements(By.xpath(locator)).size() == 0) {
                if (waitCounter < longWait) {
                    Thread.sleep(2000);
                    waitCounter += 2;
                } else break;
            }
            elem = masterWait(getDriver(), locator, longWait);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elem;
    }
    public static List<WebElement> getElements(String... elemInfo) {
        String locator = "";
        WebElement elem = null;
        List<WebElement> WeList=null;
        try {
            getInstance();
            locator = getLocator(elemInfo);
            int waitCounter = 0;
            while (getDriver().findElements(By.xpath(locator)).size() == 0) {
                if (waitCounter < longWait) {
                    Thread.sleep(2000);
                    waitCounter += 2;
                } else break;
            }
            elem = masterWait(getDriver(), "("+locator+")[1]", longWait);
            WeList =getDriver().findElements(By.xpath(locator));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WeList;
    }
}
