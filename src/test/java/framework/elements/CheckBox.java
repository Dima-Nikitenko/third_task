package framework.elements;

import org.openqa.selenium.By;

public class CheckBox extends BaseElement {

    public CheckBox(final By locator, String elementName) {
        super(locator, elementName);
    }

    public CheckBox(final String chkLocator, final String chkLocalizedName, String elementName) {
        super(chkLocator, chkLocalizedName, elementName);
    }

    public boolean isSelected(){
        waitForElementToBePresent();
        return this.getElement().isSelected();
    }

    protected String getElementType() {
        return getLogProperty("locale.checkbox");
    }
}
