package steam.pages;

import framework.BasePage;
import org.openqa.selenium.By;
import framework.elements.Button;

public class InstallSteamPage extends BasePage {

    private static final String BUTTON_DOWNLOAD_STEAM = "button.downloadsteam";

    private static String uniqueElement = "about_header_area";
    private String btnInstallSteamLocator = "By.xpath//div[contains(@id, 'about_greeting')]//a[contains(text(), '%s')]";

    public InstallSteamPage() {
        super(By.id(uniqueElement), "Install Steam");
    }

    public void download() {
        String btnName = locale.getLocalizedElementProperty(BUTTON_DOWNLOAD_STEAM);
        Button btnInstallSteam = new Button(btnInstallSteamLocator, btnName, btnName);
        btnInstallSteam.click();
        browser.assertDownload(btnInstallSteam.getLocator());
    }
}
