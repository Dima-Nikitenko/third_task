package framework;

import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.nio.file.Path;
import java.time.Duration;
import java.nio.file.Paths;
import javax.naming.NamingException;

public final class Browser {

    public static WebDriver driver;
    public static Browser instance;
    public static PropertiesHandler property;

    static final String PROPERTIES_FILE = "config.properties";
    private static final String URL_KEY = "url";
    private static final String BROWSER_KEY = "browser";
    private static final String IMPLICITLY_WAIT = "implicitlyWait";
    private static final String DOWNLOAD_TIMEOUT = "timeoutForDownload";
    private static final String DEFAULT_TIMEOUT = "timeoutForExplicitWait";
    private static final String DEFAULT_PAGE_LOAD_TIMEOUT = "timeoutForPageLoad";
    private static final String DEFAULT_REFRESHED_STALENESS_TIMEOUT = "timeoutForRefreshStaleness";

    public static String urlKey;
    public static String chosenBrowser;
    public static String pageLoadTimeout;
    public static String downloadTimeout;
    public static String conditionTimeout;
    public static String implicitlyTimeout;
    public static String refreshedStalenessTimeout;

    Actions actions = new Actions(driver);
    private static BaseTest baseTest;
    private static Logger logger = Logger.getInstance();

    private static void initProperties() {
        property = new PropertiesHandler(PROPERTIES_FILE);
        urlKey = property.getProperty(URL_KEY);
        implicitlyTimeout = property.getProperty(IMPLICITLY_WAIT);
        conditionTimeout = property.getProperty(DEFAULT_TIMEOUT);
        refreshedStalenessTimeout = property.getProperty(DEFAULT_REFRESHED_STALENESS_TIMEOUT);
        pageLoadTimeout = property.getProperty(DEFAULT_PAGE_LOAD_TIMEOUT);
        chosenBrowser = property.getProperty(BROWSER_KEY);
        downloadTimeout = property.getProperty(DOWNLOAD_TIMEOUT);
    }

    public static Browser getInstance() {
        if (instance == null) {
            initProperties();
            try {
                driver = BrowserFactory.setup(chosenBrowser);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(implicitlyTimeout)));
                logger.info(getLogProperty("locale.browser.configured"));
            } catch (NamingException exc) {
                logger.fatal(Logger.getLogProperty("locale.browser.name.wrong").concat("\nchrome\nfirefox\nedge\nopera\nie"), exc);
            }
            instance = new Browser();
            logger.info(String.format(getLogProperty("locale.browser.ready"), chosenBrowser));
        }
        return instance;
    }

    private static String getLogProperty(final String key) {
        return Logger.getLogProperty(key);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void navigateUrl(final String url) {
        driver.get(url);
    }

    public void waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(pageLoadTimeout)));
        try {
            wait.until((ExpectedCondition<Boolean>) d -> {
                if (!(d instanceof JavascriptExecutor)) {
                    return true;
                }
                Object result = ((JavascriptExecutor) d)
                        .executeScript("return document['readyState'] ? 'complete' == document.readyState : true");
                return result instanceof Boolean && (Boolean) result;
            });
        } catch (Exception e) {
            logger.warn(Logger.getLogProperty("locale.browser.page.timeout"));
        }
    }

    public void moveByOffset(final int xOffset, final int yOffset) {
        actions.moveByOffset(xOffset, yOffset).build().perform();
    }

    public void refresh() {
        getDriver().navigate().refresh();
        waitForPageToLoad();
    }

    public void assertDownload(By locator) {
        String downloadHref = getDriver().findElement(locator).getAttribute("href");
        String fileNameFromHref = downloadHref.substring(downloadHref.lastIndexOf('/')+1);
        String downloadedFile = Paths.get(fileNameFromHref).getFileName().toString();
        Path downloadFilePath = Paths.get(BrowserFactory.pathToDownloads, downloadedFile);
        logger.info(String.format(getLogProperty("locale.download.wait"), fileNameFromHref));
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(Integer.parseInt(Browser.downloadTimeout)))
                    .until(d -> downloadFilePath.toFile().exists());
        } catch (TimeoutException e) {
            logger.fatal(getLogProperty("locale.download.timeout"));
        }
        logger.info(getLogProperty("locale.download.finished"));
        String actual = downloadFilePath.toFile().getName();
        logger.info(getLogProperty("locale.download.verify"));
        try {
            Assert.assertEquals(actual, fileNameFromHref, (Logger.getLogProperty("locale.is.absent")));
        } catch (AssertionError exc) {
            logger.fatal(getLogProperty("locale.download.failed"), exc);
        }
        logger.info(getLogProperty("locale.download.verified"));
    }

    public void closeAndQuit() {
        if(null != driver) {
            try {
                driver.quit();
                logger.info("");
                logger.info(getLogProperty("locale.browser.driver.quit"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.fatal(getLogProperty("locale.browser.driver.quit.fail"));
            } finally {
                instance = null;
            }
        }
    }
}
