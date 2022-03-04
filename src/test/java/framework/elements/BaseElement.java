package framework.elements;

import framework.Logger;
import framework.Browser;
import org.testng.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.time.Duration;

public abstract class BaseElement {

    protected By locator;
    protected Select select;
    protected WebDriverWait wait;
    protected WebElement element;

    protected String elementName;
    private static final String ID = "By.id";
    private static final String CLASS = "By.className";
    private static final String XPATH = "By.xpath";

    protected Browser browser = Browser.getInstance();
    protected static Logger logger = Logger.getInstance();
    Actions action = new Actions(Browser.getDriver());

    protected BaseElement(final By locator, final String elementName) {
        this.locator = locator;
        this.elementName = elementName;
        wait = new WebDriverWait(Browser.getDriver(), Duration.ofSeconds(Long.parseLong(Browser.conditionTimeout)));
    }

    protected BaseElement(String elementLocator, String insertString, final String elementName) {
        if (elementLocator.contains(ID)) {
            locator = By.id(String.format(elementLocator.replace(ID, ""), insertString));
        } else if (elementLocator.contains(CLASS)) {
            locator = By.className(String.format(elementLocator.replace(CLASS, ""), insertString));
        } else if (elementLocator.contains(XPATH)) {
            locator = By.xpath(String.format(elementLocator.replace(XPATH, ""), insertString));
        } else {
            logger.fatal("Unknown locator's type.");
        }
        this.elementName = elementName;
        wait = new WebDriverWait(Browser.getDriver(), Duration.ofSeconds(Long.parseLong(Browser.conditionTimeout)));
    }

    protected static String getLogProperty(final String key) {
        return Logger.getLogProperty(key);
    }

    protected String formatLog(final String message) {
        return String.format("%1$s '%2$s' %3$s", getElementType(), elementName, message);
    }

    protected abstract String getElementType();

    public WebElement getElement() {
        waitForElementToBePresent();
        return element;
    }

    public By getLocator() {
        return locator;
    }

    public String getText() {
        return getElement().getAttribute("textContent");
    }

    protected boolean isElementPresent() {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        element = Browser.getDriver().findElement(locator);
        waitRefreshedStaleness();
        return element.isDisplayed();
    }

    public void waitForElementToBePresent() {
        waitRefreshedStaleness();
        try {
            Assert.assertTrue(isElementPresent());
        } catch (Throwable exc) {
            logger.fatal(formatLog(getLogProperty("locale.is.absent")), exc);
        }
    }

    public void waitRefreshedStaleness() {
        try {
            new WebDriverWait(Browser.getDriver(), Duration.ofSeconds(Long.parseLong(Browser.refreshedStalenessTimeout))).
                    until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(Browser.getDriver().findElement(locator))));
        } catch (TimeoutException ignore) {
            //Do nothing. Element is not stale.
        }
    }

    public boolean isEnabled() {
        waitForElementToBePresent();
        return this.getElement().isEnabled();
    }

    public List<WebElement> findListOfElements() {
        waitForElementToBePresent();
        return Browser.getDriver().findElements(locator);
    }

    public void scrollToElement() {
        ((JavascriptExecutor) Browser.getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        if (Browser.getDriver() instanceof JavascriptExecutor) {
            ((JavascriptExecutor) Browser.getDriver()).executeScript("arguments[0].style.border='3px solid red'", element);
        }
    }

    public void scrollToElementRaw() {
        waitForElementToBePresent();
        ((JavascriptExecutor) Browser.getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public void moveToElement() {
        waitForElementToBePresent();
        scrollToElement();
        action.moveToElement(element).build().perform();
    }

    public void click() {
        moveToElement();
        logger.info(String.format(getLogProperty("locale.clicking"), elementName, getElementType().toLowerCase()));
        ((JavascriptExecutor) Browser.getDriver()).executeScript("arguments[0].setAttribute('target', '_self');", element);
        try {
            element.click();
        } catch (ElementClickInterceptedException exc) {
            logger.fatal(String.format(getLogProperty("locale.clicking.exception"), elementName, getElementType().toLowerCase()), exc);
        }
    }

    public void clickAndWait() {
        click();
        browser.waitForPageToLoad();
    }
}
