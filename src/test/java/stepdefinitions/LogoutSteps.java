package stepdefinitions;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.DashboardPage;
import utils.DriverFactory;

public class LogoutSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage(driver);
    private DashboardPage dashboardPage = new DashboardPage(driver);

    @When("user clicks account dropdown")
    public void userClicksAccountDropdown() {
        dashboardPage.clickAccountDropdown();
    }

    @When("user clicks logout button")
    public void userClicksLogoutButton() {
        dashboardPage.clickLogoutButton();
    }

    @Then("user should be redirected to login page")
    public void userShouldBeRedirectedToLoginPage() {
        Assert.assertTrue("Redirect to login page welcome title failed", loginPage.isWelcomeTitleDisplayed());
    }

    @Then("page title should contain {string}")
    public void pageTitleShouldContain(String expectedTitle) {
        String actualTitle = loginPage.getWelcomeTitleText();
        Assert.assertTrue(String.format("Expected welcome title to contain '%s', but got '%s'", expectedTitle, actualTitle),
                actualTitle.contains(expectedTitle));
    }
}
