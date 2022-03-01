package framework.elements;

import framework.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class TextBox extends BaseElement {

    public TextBox(final By locator, String elementName) {
        super(locator, elementName);
    }

    public TextBox(final String txtLocator, final String txtLocalizedName, String elementName) {
        super(txtLocator, txtLocalizedName, elementName);
    }

    public void sendKeys(Keys key) {
        waitForElementToBePresent();
        scrollToElement();
        logger.info(String.format(getLogProperty("locale.text.typing") + " '%1$s'", key));
        browser.getDriver().findElement(locator).sendKeys(key);
    }

    public String getValue() {
        waitForElementToBePresent();
        scrollToElement();
        return element.getAttribute("value");
    }

    protected String getElementType() {
        return getLogProperty("locale.text.box");
    }
}
