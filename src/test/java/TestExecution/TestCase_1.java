package TestExecution;

import BrowserFactory.ChromeDriverManager;
import Pages.Homepage;
import org.testng.annotations.Test;

public class TestCase_1 extends ChromeDriverManager {

    @Test(priority = 1)
    public void loginAndCheckDashboard() throws InterruptedException {
        Homepage homepage = new Homepage(getDriver());  // pass initialized driver
        homepage.login("arun@regression.com", "Rently@1234");
    }

    @Test(priority = 2)
    public void loginAndCheckDashboard2() throws InterruptedException {
        Homepage homepage = new Homepage(getDriver());  // pass initialized driver
        homepage.login("arun@regression.com", "Rently@1234");
    }
}
