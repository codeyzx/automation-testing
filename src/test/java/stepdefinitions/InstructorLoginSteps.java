package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.DashboardPage;
import utils.DriverFactory;

public class InstructorLoginSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage(driver);
    private DashboardPage dashboardPage = new DashboardPage(driver);

    @Given("user is on JTKLearn login page")
    public void userIsOnJtkLearnLoginPage() {
        loginPage.navigateToLoginPage();
    }

    @When("user enters email {string}")
    public void userEntersEmail(String email) {
        loginPage.enterEmail(email);
    }

    @And("user enters password {string}")
    public void userEntersPassword(String password) {
        loginPage.enterPassword(password);
    }

    @And("user clicks Masuk button")
    public void userClicksMasukButton() {
        loginPage.clickLoginButton();
    }

    @Then("login should be successful")
    public void loginShouldBeSuccessful() {
        Assert.assertTrue("Dashboard greeting or title was not displayed after login", 
                dashboardPage.isDashboardDisplayed());
    }

    @And("instructor dashboard should be displayed")
    public void instructorDashboardShouldBeDisplayed() {
        Assert.assertTrue("Instructor dashboard was not displayed or wrong name greeting shown", 
                dashboardPage.isDashboardDisplayed());
    }

    @And("navigation menu should contain {string}")
    public void navigationMenuShouldContain(String menuName) {
        Assert.assertTrue(String.format("Navigation menu '%s' should be present", menuName), 
                dashboardPage.isNavigationMenuPresent(menuName));
    }

    @And("account menu should be displayed")
    public void accountMenuShouldBeDisplayed() {
        Assert.assertTrue("Account dropdown is not displayed", 
                dashboardPage.isAccountDropdownDisplayed());
    }

    @And("logout submenu should be accessible")
    public void logoutSubmenuShouldBeAccessible() {
        dashboardPage.clickAccountDropdown();
        Assert.assertTrue("Logout button was not displayed inside the dropdown menu", 
                dashboardPage.isLogoutButtonDisplayed());
    }
}
