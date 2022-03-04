package steam.pages;

import framework.BasePage;
import org.apache.commons.lang3.text.WordUtils;
import org.openqa.selenium.By;
import framework.elements.Label;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ActionPage extends BasePage {

    private static final String PAGE_ACTION = "page.action";
    private static final String APP_DISCOUNTED = "app.discounted";

    private static String uniqueElement = "//div[contains(@class, 'ContentHubLabsWidget')]/following-sibling::div[contains(text(), '%s')]";
    private static String lblTitle = locale.getLocalizedElementProperty(PAGE_ACTION);

    protected static String lblChosenAppTitle;
    private String lblSaleItemHeader =  "By.xpath//div[contains(@class, 'FlavorLabel') and contains(text(), '%s')]";
    private String lblSpecialsMaxDiscount = "By.xpath//div[@id = 'SaleSection_13268']//div[contains(@class, 'StoreSaleDiscountBox') and contains(text(), '%s')]";
    private String lblWidgetTitle = String.format("%s/./preceding::div[contains(@class, 'StoreSaleWidgetTitle')][1]", lblSpecialsMaxDiscount);

    private Label lblPrecedingSale = new Label(By.id("SaleSection_92087"), "Section preceding SALE");
    private Label lblDiscountsList = new Label(By.xpath("//div[contains(@class, 'SaleItemBrowserRow')]//div[contains(@class, 'StoreSaleDiscountBox')]"), "Discounts List");

    public ActionPage() {
        super(By.xpath(String.format(uniqueElement, lblTitle)), "Action");
    }

    private String getHighestProfitInDiscounts() {
        ArrayList<Integer> discounts = new ArrayList<>();

        String saleItem = locale.getLocalizedElementProperty(APP_DISCOUNTED);
        lblPrecedingSale.scrollToElementRaw();
        Label lblAppDiscounted = new Label(lblSaleItemHeader, saleItem, saleItem);
        lblAppDiscounted.clickAndWait();

        List<WebElement> lblElements = lblDiscountsList.findListOfElements();
        ArrayList<WebElement> lblDiscounts = new ArrayList<>(lblElements);

        for (WebElement elem : lblDiscounts) {
            String textFromElement = elem.getText();
            String isolatedDiscount = textFromElement.replace("-", "").replace("%", "");
            int parseDiscountToInt = Integer.parseInt(isolatedDiscount);
            discounts.add(parseDiscountToInt);
        }
        return Collections.max(discounts).toString();
    }

    private void checkIfAgeCheckPageIsOpened() {
        try {
            logger.info(getLogProperty("locale.page.redirect"));
            new AgeCheckPage(true);
        } catch (NoSuchElementException e) {
            logger.info(getLogProperty("locale.page.noredirect"));
            new AppPage();
        }
    }

    public void chooseMostProfitableApp() {
        String highestDiscount = getHighestProfitInDiscounts();
        Label lblMostProfitableApp = new Label(lblSpecialsMaxDiscount, highestDiscount, "Highest Discount");
        Label lblAppTitle = new Label(lblWidgetTitle, highestDiscount, "App Title");
        lblMostProfitableApp.moveToElement();
        lblChosenAppTitle = lblAppTitle.getText();
        lblAppTitle = new Label(lblWidgetTitle.concat("/./parent::a"), highestDiscount, "App Title to Click");
        lblAppTitle.clickAndWait();
        checkIfAgeCheckPageIsOpened();
    }
}
