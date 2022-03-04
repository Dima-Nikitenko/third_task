package framework;

import framework.utils.FileUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    private static Logger logger = Logger.getInstance();

    @BeforeSuite
    public void setup() {
        FileUtils.clearDownloadDir();
        logger.logTestStart(this.getClass().getSimpleName());
        Browser.getInstance().navigateUrl(Browser.urlKey);
        logger.info(String.format(Logger.getLogProperty("locale.navigate.url"), Browser.urlKey));
    }

    @AfterSuite
    public void teardown() {
        Browser.getInstance().closeAndQuit();
        logger.logEndTest(this.getClass().getSimpleName());
    }
}

