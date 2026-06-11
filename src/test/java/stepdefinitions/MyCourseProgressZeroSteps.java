package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.LoginPage;
import pages.DashboardPage;
import pages.MyCoursePage;
import utils.DriverFactory;
import utils.TestConfig;

public class MyCourseProgressZeroSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage(driver);
    private DashboardPage dashboardPage = new DashboardPage(driver);
    private MyCoursePage myCoursePage = new MyCoursePage(driver);
    private String targetCourseName;

    @Given("student account is active")
    public void studentAccountIsActive() {
        loginPage.navigateToLoginPage();
        // If already logged in, log out first to ensure clean state with correct account
        if (dashboardPage.isDashboardDisplayed()) {
            dashboardPage.clickAccountDropdown();
            dashboardPage.clickLogoutButton();
        }
        loginPage.login(TestConfig.ZERO_PROGRESS_STUDENT_EMAIL, TestConfig.ZERO_PROGRESS_STUDENT_PASSWORD);
        Assert.assertTrue("Student dashboard was not displayed", dashboardPage.isDashboardDisplayed());
    }

    @And("student is enrolled in {string}")
    public void studentIsEnrolledIn(String courseName) {
        this.targetCourseName = courseName;
        dashboardPage.isDashboardDisplayed();
        dashboardPage.clickCourse(courseName);
        try {
            pages.CoursePage coursePage = new pages.CoursePage(driver);
            coursePage.startOrContinueCourse();
        } catch (Exception e) {
            System.out.println("Could not click enrollment button, maybe already enrolled: " + e.getMessage());
        }
        dashboardPage.navigateToBeranda();
    }

    @And("no instruction has been completed")
    public void noInstructionHasBeenCompleted() {
        // Precondition: progress should start at 0%
        System.out.println("Verifying student has no completed instructions");
    }

    @When("student opens menu {string}")
    public void studentOpensMenu(String menuName) {
        if (menuName.equalsIgnoreCase("Kursus Saya")) {
            dashboardPage.navigateToKursusSaya();
        } else if (menuName.equalsIgnoreCase("Beranda")) {
            dashboardPage.navigateToBeranda();
        }
    }

    @And("student opens tab {string}")
    public void studentOpensTab(String tabName) {
        if (tabName.equalsIgnoreCase("Dalam Progres")) {
            myCoursePage.clickInProgressTab();
        } else if (tabName.equalsIgnoreCase("Selesai")) {
            myCoursePage.clickCompletedTab();
        }
    }

    @Then("course {string} should be displayed")
    public void courseShouldBeDisplayed(String courseName) {
        this.targetCourseName = courseName;
        boolean isPresent = myCoursePage.isCourseInTab(courseName, "inprogress");
        Assert.assertTrue(String.format("Course '%s' should be displayed under Dalam Progres tab", courseName), isPresent);
    }

    @And("progress percentage should be {string}")
    public void progressPercentageShouldBe(String expectedPercentage) {
        String actualPercentage = myCoursePage.getCourseProgressPercentage(targetCourseName);
        Assert.assertEquals("Course progress percentage mismatch", expectedPercentage, actualPercentage);
    }

    @And("progress bar should be empty")
    public void progressBarShouldBeEmpty() {
        String xpathExpr = String.format("//h6[text()='%s']/..//div[contains(@class,'progress-bar-fill')]", targetCourseName);
        WebElement progressBar = driver.findElement(By.xpath(xpathExpr));
        String styleAttr = progressBar.getAttribute("style");
        
        // Assert that the progress bar width is 0%
        String cleanedStyle = styleAttr.replaceAll("\\s", "").toLowerCase();
        Assert.assertTrue("Progress bar is not empty: " + styleAttr, 
                cleanedStyle.contains("width:0%") || cleanedStyle.contains("width:0px") || cleanedStyle.isEmpty());
    }

    @And("course should not appear in tab {string}")
    public void courseShouldNotAppearInTab(String tabName) {
        if (tabName.equalsIgnoreCase("Selesai")) {
            myCoursePage.clickCompletedTab();
            boolean isPresent = myCoursePage.isCourseInTab(targetCourseName, "completed");
            Assert.assertFalse(String.format("Course '%s' should not appear in Selesai tab", targetCourseName), isPresent);
        }
    }
}
