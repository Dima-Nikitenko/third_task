package framework.elements;

import org.openqa.selenium.By;

public class Label extends BaseElement {

    public Label(final By locator, String elementName) {
        super(locator, elementName);
    }

    public Label(final String lblLocator, final String lblLocalizedName, String elementName) {
        super(lblLocator, lblLocalizedName, elementName);
    }

    protected String getElementType() {
        return getLogProperty("locale.label");
    }
}
