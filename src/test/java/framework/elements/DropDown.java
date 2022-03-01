package framework.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class DropDown extends BaseElement {

    public DropDown(final By locator, String elementName) {
        super(locator, elementName);
    }

    public DropDown(final String ddlLocator, final String ddlLocalizedName, String elementName) {
        super(ddlLocator, ddlLocalizedName, elementName);
    }

    public void selectValue(String value) {
        waitForElementToBePresent();
        scrollToElement();
        select = new Select(element);
        select.selectByVisibleText(value);
    }

    protected String getElementType() {
        return getLogProperty("locale.dropdown");
    }
}
