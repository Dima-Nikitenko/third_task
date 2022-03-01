package framework;

import org.testng.Assert;

public final class Logger {

    static final String LOG4J_PROPERTIES = "log4j.properties";
    private static final String LOG_LANGUAGE = "logger.language";
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);
    private static Logger instance;

    private static boolean logSteps = true;
    private static PropertiesHandler propertyLogger;
    private static PropertiesHandler propertyLog4j;

    private String testResult = "locale.logger.test.passed";

    private static void initProperties() {
        propertyLogger = new PropertiesHandler(LOG4J_PROPERTIES);
        propertyLog4j = new PropertiesHandler(LOG4J_PROPERTIES);
        String loggerLocale = propertyLogger.getProperty(LOG_LANGUAGE);
        propertyLogger = Localization.setupLoggerLocale(loggerLocale);
    }

    public static String getLogProperty(final String key) {
        return propertyLogger.getProperty(key);
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            initProperties();
            instance = new Logger();
        }

        return instance;
    }

    public void step(final int step) {
        logSeparatorMsg(getLogProperty("locale.logger.step") + step);
    }

    public void steps(final int fromStep, final int toStep) {
        logSeparatorMsg(getLogProperty("locale.logger.steps") + fromStep + "-" + toStep);
    }

    private void logSeparatorMsg(final String msg) {
        if (logSteps) {
            info(String.format("--------------------------==[ %1$s ]==--------------------------", msg));
        }
    }

    public void logTestStart(final String testName) {
        if (logSteps) {
            String formattedName = String.format("=====================  %1$s: '%2$s' ====================", getLogProperty("locale.logger.test.case"), testName);
            StringBuilder divider = new StringBuilder();
            int nChars = formattedName.length();
            divider.append("-".repeat(nChars));
            info(divider.toString());
            info(formattedName);
            info(divider.toString());
            info("");
            info(getLogProperty("locale.log.destination").concat(propertyLog4j.getProperty("log4j.appender.file.File")));
            info("");
        }
    }

    public void logEndTest(final String testName) {
        if (logSteps) {
            String formattedEnd = String.format("================= %1$s: '%2$s' %3$s! =================", getLogProperty("locale.logger.test.case"), testName, getLogProperty(testResult));
            StringBuilder divider = new StringBuilder();
            int nChars = formattedEnd.length();
            divider.append("-".repeat(nChars));
            info(divider.toString());
            info(formattedEnd);
            info(divider.toString());
            info("");
            info("");
        }
    }

    public void info(final String message) {
        logger.info(message);
    }

    public void warn(final String message) {
        logger.warn(message);
    }

    public void fatal(final String message) {
        logger.fatal(message);
        logger.fatal(getLogProperty("locale.test.failed"));
        testResult = "locale.logger.test.failed";
        Assert.fail(message);
    }

    public void fatal(final String message, Throwable exc) {
        logger.fatal(message);
        exc.printStackTrace();
        logger.fatal(getLogProperty("locale.test.failed"));
        testResult = "locale.logger.test.failed";
        Assert.fail(message);
    }
}
