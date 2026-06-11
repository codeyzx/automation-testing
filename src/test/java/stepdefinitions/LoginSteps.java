package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.DashboardPage;
import utils.DriverFactory;
import utils.TestConfig;
import utils.UserTestDataManager;
import java.util.List;

public class LoginSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage(driver);
    private DashboardPage dashboardPage = new DashboardPage(driver);
    private UserTestDataManager userTestDataManager = new UserTestDataManager(driver);

    @Given("user opens JTKLearn login page")
    public void userOpensJtkLearnLoginPage() {
        loginPage.navigateToLoginPage();
    }

    @Given("user is on login page")
    public void userIsOnLoginPage() {
        loginPage.navigateToLoginPage();
    }

    @Given("user logs in with valid Pelajar credentials")
    public void userLogsInWithValidPelajarCredentials() {
        userTestDataManager.resetDefaultStudent();
        loginPage.navigateToLoginPage();
        loginPage.login(TestConfig.DEFAULT_STUDENT_EMAIL, TestConfig.DEFAULT_STUDENT_PASSWORD);
    }

    @Given("user is on dashboard page")
    public void userIsOnDashboardPage() {
        Assert.assertTrue("Dashboard is not displayed for Pelajar", dashboardPage.isDashboardDisplayed());
    }

    @When("user enters email {string}")
    public void userEntersEmail(String email) {
        loginPage.enterEmail(email);
    }

    @When("user enters password {string}")
    public void userEntersPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("user clicks login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("user should be redirected to teacher dashboard")
    public void userShouldBeRedirectedToTeacherDashboard() {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("dashboard-pengajar"));
        } catch (Exception e) {
            System.out.println("Timeout waiting for redirect. Current URL: " + driver.getCurrentUrl());
            System.out.println("Page content:\n" + driver.findElement(By.tagName("body")).getText());
            throw e;
        }
        Assert.assertTrue("Not redirected to teacher dashboard", driver.getCurrentUrl().contains("dashboard-pengajar"));
    }

    @Then("navigation menu should display:")
    public void navigationMenuShouldDisplay(List<String> expectedMenus) {
        for (String menu : expectedMenus) {
            Assert.assertTrue("Navigation menu " + menu + " is not displayed",
                    dashboardPage.isNavigationMenuDisplayed(menu));
        }
    }

    @Then("account name should be displayed")
    public void accountNameShouldBeDisplayed() {
        String name = dashboardPage.getAccountDropdownText();
        Assert.assertFalse("Account name is empty", name.isEmpty());
        System.out.println("Displayed account name: " + name);
    }
}

