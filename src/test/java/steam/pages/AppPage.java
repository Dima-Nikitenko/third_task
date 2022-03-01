package steam.pages;

import framework.BasePage;
import org.openqa.selenium.By;

public class AppPage extends BasePage {

    private static String uniqueElement = "//div[@id = 'appHubAppName'][contains(text(), '%s')]";
    private static String lblTitle = ActionPage.lblChosenAppTitle;

    public AppPage() {
        super(By.xpath(String.format(uniqueElement, lblTitle)), lblTitle);
    }
}
