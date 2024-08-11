package eg.mos.sportify.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static LogUtil instance;
    private final Logger logger;

    // Private constructor to prevent instantiation
    private LogUtil(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    // Public method to provide access to the singleton instance
    public static synchronized LogUtil getInstance(Class<?> clazz) {
        if (instance == null) {
            instance = new LogUtil(clazz);
        }
        return instance;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void trace(String message) {
        logger.trace(message);
    }

}
