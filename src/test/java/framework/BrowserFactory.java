package framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.IOException;
import java.util.HashMap;
import java.nio.file.Paths;
import javax.naming.NamingException;

public abstract class BrowserFactory {

    public static String pathToDownloads;

    static {
        try {
            pathToDownloads = Paths.get("src\\test\\resources\\downloadedFiles").toFile().getCanonicalPath();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static WebDriver setup(final String browser) throws NamingException {

        WebDriver driver;
        ChromeOptions optionsChrome = new ChromeOptions();
        FirefoxOptions optionsFirefox = new FirefoxOptions();
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        var browserPrefs = new HashMap<String, Object>();

        switch (browser.trim().toLowerCase()) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                optionsChrome.addArguments("start-maximized");
                optionsChrome.addArguments("disable-popup-blocking");
                browserPrefs.put("download.default_directory", pathToDownloads);
                browserPrefs.put("safebrowsing.enabled", "false");
                optionsChrome.setExperimentalOption("prefs", browserPrefs);
                driver = new ChromeDriver(optionsChrome);
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                firefoxProfile.setPreference("browser.download.folderList", 2);
                firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
                firefoxProfile.setPreference("browser.download.dir", pathToDownloads);
                firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/csv, text/csv, text/plain,application/octet-stream doc xls pdf txt");
                optionsFirefox.setProfile(firefoxProfile);
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
                driver = new FirefoxDriver(optionsFirefox);
                driver.manage().window().maximize();
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            }
            case "opera" -> {
                WebDriverManager.operadriver().setup();
                driver = new OperaDriver();
            }
            case "ie" -> {
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver();
            }
            default -> throw new NamingException();
        }
        return driver;
    }
}

