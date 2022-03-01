package framework.elements;

import org.openqa.selenium.By;

public class Button extends BaseElement {

    public Button(final By locator, String elementName) {
        super(locator, elementName);
    }

    public Button(final String btnLocator, final String btnLocalizedName, String elementName) {
        super(btnLocator, btnLocalizedName, elementName);
    }

    protected String getElementType() {
        return getLogProperty("locale.button");
    }
}
