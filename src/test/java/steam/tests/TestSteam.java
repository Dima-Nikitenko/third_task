package steam.tests;

import framework.BaseTest;
import framework.Logger;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

import steam.pages.*;

public class TestSteam extends BaseTest {

    Logger logger = new Logger();

    @Parameters({ "Language", "MenuItem", "SubMenuItem" }) // specified in the testng.xml file
    @Test
    void testSteam(String language, String menuItem, String subMenuItem) {
        logger.step(1);
        BaseSteamPage baseSteamPage = new BaseSteamPage();
        logger.step(2);
        baseSteamPage.checkLanguage(language);
        logger.step(3);
        baseSteamPage.navigateMenu(menuItem, subMenuItem);
        logger.step(4);
        ActionPage actionPage = new ActionPage();
        logger.step(5);
        actionPage.chooseMostProfitableApp();
        logger.step(6);
        baseSteamPage.installSteam();
        logger.step(7);
        InstallSteamPage installSteamPage = new InstallSteamPage();
        installSteamPage.download();
    }
}
