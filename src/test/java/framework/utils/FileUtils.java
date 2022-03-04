package framework.utils;

import framework.Browser;
import framework.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static framework.Logger.getLogProperty;

public class FileUtils {

    public static String pathToDownloads;
    static {
        try {
            pathToDownloads = Paths.get("src\\test\\resources\\downloadedFiles").toFile().getCanonicalPath();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
    private static File file = new File(pathToDownloads);
    private static Logger logger = Logger.getInstance();

    public static void clearDownloadDir() {
        String[] downloadedFiles;
        if (file.isDirectory()) {
            downloadedFiles = file.list();
            for (String downloadedFile : downloadedFiles) {
                File fileToDelete = new File(file, downloadedFile);
                fileToDelete.delete();
            }
        }
    }

    public static void assertDownload(By locator) {
        String downloadHref = Browser.getDriver().findElement(locator).getAttribute("href");
        String fileNameFromHref = downloadHref.substring(downloadHref.lastIndexOf('/')+1);
        String downloadedFile = Paths.get(fileNameFromHref).getFileName().toString();
        Path downloadFilePath = Paths.get(pathToDownloads, downloadedFile);
        logger.info(String.format(getLogProperty("locale.download.wait"), fileNameFromHref));
        try {
            new WebDriverWait(Browser.getDriver(), Duration.ofSeconds(Integer.parseInt(Browser.downloadTimeout)))
                    .until(d -> downloadFilePath.toFile().exists());
        } catch (TimeoutException e) {
            logger.fatal(getLogProperty("locale.download.timeout"));
        }
        logger.info(getLogProperty("locale.download.finished"));
        String actual = downloadFilePath.toFile().getName();
        logger.info(getLogProperty("locale.download.verify"));
        try {
            Assert.assertEquals(actual, fileNameFromHref, (getLogProperty("locale.is.absent")));
        } catch (AssertionError exc) {
            logger.fatal(getLogProperty("locale.download.failed"), exc);
        }
        logger.info(getLogProperty("locale.download.verified"));
    }
}
