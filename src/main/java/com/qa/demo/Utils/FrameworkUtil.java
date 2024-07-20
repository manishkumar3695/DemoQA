package com.qa.demo.Utils;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.interfaces.BCKeyStore;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static com.qa.demo.Utils.DriverUtil.getDriver;
import static com.qa.demo.Utils.ConfigUtil.configIterator;

public class FrameworkUtil {
    public static final String configPath = "config/";
    public static HashMap<String, HashMap<String, String>> configMap = new HashMap<>();
    public static LoggerUtil logger = new LoggerUtil();

    public static WebElement fluentWait(WebDriver driver, String xpathLocator, int timeout) {
        Duration timeOut = Duration.ofSeconds(timeout);
        Duration pollingWait = Duration.ofMillis(500);


        Wait wait = new FluentWait(driver)
                .withTimeout(timeOut)
                .pollingEvery(pollingWait)
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotInteractableException.class);


        Function<WebDriver, WebElement> fun = d -> d.findElement(By.xpath(xpathLocator));
        WebElement element = (WebElement) wait.until(fun);
        if (element.isDisplayed() && element.isEnabled()) {
            logger.info("Fluent wait successful");
        }
        return element;
    }

    public static WebElement masterWait(WebDriver driver, String xpathLocator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout, 250);
        wait.until(d -> {
            WebElement e = d.findElement(By.xpath(xpathLocator));
            return (e != null && e.isDisplayed() && e.isEnabled());
        });
        return driver.findElement(By.xpath(xpathLocator));
    }

    public static boolean conditionalWait(ExpectedCondition<?> exp, int timeout) {
        WebDriver driver = getDriver();

        WebDriverWait wait = new WebDriverWait(driver, timeout, 250);
        try {
            wait.ignoring(NoSuchElementException.class);
            wait.ignoring(ElementNotInteractableException.class);
            wait.ignoring(StaleElementReferenceException.class);
            wait.ignoring(ElementClickInterceptedException.class);
            wait.ignoring(ElementNotVisibleException.class);
            wait.ignoring(ElementNotSelectableException.class);
            wait.until(exp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;

    }

    public static String getConfig(String configName) {
        String value = "";
        try {
            if (configMap.isEmpty()) {
                InputStream is = FrameworkUtil.class.getClassLoader().getResourceAsStream(configPath);
                List<String> files = IOUtils.readLines(is, Charsets.UTF_8);
                for (String fileName : files) {
                    configMap.put(fileName, configIterator(configPath + fileName));
                }
            }
            String envParam = System.getenv(configName.toUpperCase());
            if (envParam == null) {
                for (String fileName : configMap.keySet()) {
                    if (configMap.get(fileName).containsKey(configName)) {
                        value = configMap.get(fileName).get(configName);
                        break;
                    }
                }
            }
            else
                value=envParam;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
