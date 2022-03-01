package framework;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;

public abstract class BaseTest {

    private File file = new File(BrowserFactory.pathToDownloads);
    private static Logger logger = Logger.getInstance();

    @BeforeSuite
    public void setup() {
        clearDownloadDir();
        logger.logTestStart(this.getClass().getSimpleName());
        Browser.getInstance().navigateUrl(Browser.urlKey);
        logger.info(String.format(Logger.getLogProperty("locale.navigate.url"), Browser.urlKey));
    }

    @AfterSuite
    public void teardown() {
        Browser.getInstance().closeAndQuit();
        logger.logEndTest(this.getClass().getSimpleName());
    }

    private void clearDownloadDir() {
        String[] downloadedFiles;
        if (file.isDirectory()) {
            downloadedFiles = file.list();
            for (String downloadedFile : downloadedFiles) {
                File fileToDelete = new File(file, downloadedFile);
                fileToDelete.delete();
            }
        }
    }
}

