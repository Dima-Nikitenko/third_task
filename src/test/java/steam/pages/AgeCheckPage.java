package steam.pages;

import framework.BasePage;
import org.openqa.selenium.By;
import framework.elements.Button;
import framework.elements.DropDown;

import java.util.Calendar;

public class AgeCheckPage extends BasePage {

    private static String uniqueElement = "agegate_birthday_selector";

    private DropDown ddlAgeYear = new DropDown(By.id("ageYear"), "Year");
    private Button btnViewPage = new Button(By.id("view_product_page_btn"), "View Page");

    public AgeCheckPage(boolean isRedirected) {
        super(By.className(uniqueElement), "Age Check", isRedirected);
        proveAgeOfMajority();
    }

    private void proveAgeOfMajority() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int workaroundYear = currentYear - 18;
        ddlAgeYear.selectValue(Integer.toString(workaroundYear));
        if (btnViewPage.isEnabled()) btnViewPage.clickAndWait();
        new AppPage();
    }
}
