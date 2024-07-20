package com.qa.demo.Utils;

import org.slf4j.LoggerFactory;

public class LoggerUtil {
    static org.slf4j.Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }
}
