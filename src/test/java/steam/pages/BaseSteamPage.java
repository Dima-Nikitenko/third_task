package steam.pages;

import org.testng.Assert;
import framework.BasePage;
import org.openqa.selenium.By;
import framework.elements.Label;
import framework.elements.Button;

public class BaseSteamPage extends BasePage {
    private static final String DROPDOWN_LANGUAGE = "dropdown.language";
    private static final String BUTTON_INSTALL_STEAM = "button.installsteam";

    private static String uniqueElement = "home_page_gutter";
    private String lblLanguageMenuItemLocator = "By.xpath//div[@id = 'language_dropdown']//a[contains(text(), '%s')]";
    private String lblMenuLocator = "By.xpath//div[@id = 'genre_tab']//a[contains(@class, 'pulldown_desktop') and contains(text(), '%s')]";
    private String lblSubMenuLocator = "By.xpath//div[@id = 'genre_flyout']//div[contains(@class, 'popup_menu_subheader')]/a[contains(text(), '%s')]";
    private String btnInstallSteamLocator = "By.xpath//div[@id = 'global_action_menu']//a[contains(@class, 'installsteam') and contains(text(), '%s')]";

    private Label lblLanguageMenu = new Label(By.id("language_pulldown"), "Language Dropdown");

    public BaseSteamPage() {
        super(By.className(uniqueElement), "Base Steam");
    }

    public void checkLanguage(String language) {
        locale.setupLocale(language);
        Label lblLanguageMenuItem = new Label(lblLanguageMenuItemLocator, language, "Language Dropdown Item");
        if (!lblLanguageMenu.getText().contains(locale.getLocalizedElementProperty(DROPDOWN_LANGUAGE))) {
            lblLanguageMenu.click();
            lblLanguageMenuItem.clickAndWait();
        }
        Assert.assertTrue(lblLanguageMenu.getText().contains(locale.getLocalizedElementProperty(DROPDOWN_LANGUAGE)));
    }

    public void navigateMenu(String menuItem, String subMenuItem) {
        String localMenuItem = locale.getLocalizedElementProperty("menu.".concat(menuItem));
        String localSubMenuItem = locale.getLocalizedElementProperty("submenu.".concat(subMenuItem));
        Label lblMenu = new Label(lblMenuLocator, localMenuItem, localMenuItem);
        Label lblSubMenu = new Label(lblSubMenuLocator, localSubMenuItem, localSubMenuItem);
        lblMenu.click();
        lblSubMenu.clickAndWait();
    }

    public void installSteam() {
        String btnName = locale.getLocalizedElementProperty(BUTTON_INSTALL_STEAM);
        Button btnInstallSteam = new Button(btnInstallSteamLocator, btnName, btnName);
        btnInstallSteam.click();
    }
}
