package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.DashboardPage;
import utils.DriverFactory;

public class LoginSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage(driver);
    private DashboardPage dashboardPage = new DashboardPage(driver);

    @Given("user opens JTKLearn login page")
    public void userOpensJtkLearnLoginPage() {
        loginPage.navigateToLoginPage();
    }

    @Given("user logs in with valid Pelajar credentials")
    public void userLogsInWithValidPelajarCredentials() {
        loginPage.login("ahmadjonii@gmail.com", "ahmadjoni");
    }

    @Given("user is on dashboard page")
    public void userIsOnDashboardPage() {
        Assert.assertTrue("Dashboard is not displayed for Pelajar", dashboardPage.isDashboardDisplayed());
    }
}
