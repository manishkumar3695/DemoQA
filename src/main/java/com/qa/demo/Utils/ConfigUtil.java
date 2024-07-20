package com.qa.demo.Utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import java.util.HashMap;
import java.util.Set;

import static com.qa.demo.Utils.LoggerUtil.logger;


public class ConfigUtil {
    public static String configReader(String filePath, String key) {
        String val = "";
        try {
            PropertiesConfiguration configuration = new PropertiesConfiguration(filePath);
            val = configuration.getProperty(key).toString();
            logger.info("config Reader Output:" + key + "::" + val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void configWriter(String filePath, String val, String key) {
        try {
            PropertiesConfiguration configuration = new PropertiesConfiguration(filePath);
            configuration.setProperty(key, val);
            configuration.save();
            logger.info("Output from Config Writer" + configReader(filePath, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public static HashMap<String, String> configIterator(String fileName) {
        HashMap<String, String> propMap = new HashMap<>();
        try {
            PropertiesConfiguration configuration = new PropertiesConfiguration(fileName);
            PropertiesConfigurationLayout layout = configuration.getLayout();
            Set<String> keys = layout.getKeys();
            for (String key : keys) {
                String val = layout.getConfiguration().getProperty(key).toString();
                logger.info("key: " + key + ";;  value: " + val);
                propMap.put(key,val);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Property file Read for: "+ fileName);
        return propMap;
    }
}
