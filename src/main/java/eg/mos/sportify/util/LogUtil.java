package eg.mos.sportify.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class for logging. This class provides a singleton logger instance
 * for a specific class, ensuring that logging is done in a consistent manner
 * across the application.
 */
public class LogUtil {
    private static LogUtil instance;
    private final Logger logger;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the logger for the given class.
     *
     * @param clazz the class for which the logger will be created.
     */
    private LogUtil(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Provides access to the singleton logger instance for the specified class.
     * If the instance does not exist, it will be created. This method is
     * synchronized to ensure thread safety.
     *
     * @param clazz the class for which the logger will be created.
     * @return the singleton instance of LogUtil for the specified class.
     */
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
