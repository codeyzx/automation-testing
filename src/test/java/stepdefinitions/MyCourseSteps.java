package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.DashboardPage;
import pages.CoursePage;
import pages.MyCoursePage;
import utils.DriverFactory;

public class MyCourseSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private DashboardPage dashboardPage = new DashboardPage(driver);
    private CoursePage coursePage = new CoursePage(driver);
    private MyCoursePage myCoursePage = new MyCoursePage(driver);

    @Given("user is enrolled in {string}")
    public void userIsEnrolledIn(String courseName) {
        dashboardPage.isDashboardDisplayed(); // Ensure dashboard is fully loaded and loading spinner is gone
        // Checked implicitly; if course card is not visible or clickable, it fails here
        dashboardPage.clickCourse(courseName);
        // Go back to beranda to prepare for step 4
        dashboardPage.navigateToBeranda();
    }

    @When("user opens course {string}")
    public void userOpensCourse(String courseName) {
        dashboardPage.isDashboardDisplayed();
        dashboardPage.clickCourse(courseName);
    }

    @When("user completes instruction {string}")
    public void userCompletesInstruction(String instructionName) {
        coursePage.startOrContinueCourse();
        coursePage.selectInstruction(instructionName);
        coursePage.completeInstruction();
    }

    @When("user navigates to {string}")
    public void userNavigatesTo(String menuName) {
        if (menuName.equalsIgnoreCase("Kursus Saya")) {
            dashboardPage.navigateToKursusSaya();
        } else if (menuName.equalsIgnoreCase("Beranda")) {
            dashboardPage.navigateToBeranda();
        }
    }

    @When("user opens {string} tab")
    public void userOpensTab(String tabName) {
        if (tabName.equalsIgnoreCase("Dalam Progres")) {
            myCoursePage.clickInProgressTab();
        } else if (tabName.equalsIgnoreCase("Selesai")) {
            myCoursePage.clickCompletedTab();
        }
    }

    @Then("progress percentage should display {string}")
    public void progressPercentageShouldDisplay(String expectedPercentage) {
        String actualPercentage = myCoursePage.getCourseProgressPercentage("Kursus Ahli Pedang");
        Assert.assertEquals("Course progress percentage mismatch", expectedPercentage, actualPercentage);
    }

    @Then("course should not appear in {string} tab")
    public void courseShouldNotAppearInTab(String tabName) {
        if (tabName.equalsIgnoreCase("Selesai")) {
            myCoursePage.clickCompletedTab();
            boolean isPresent = myCoursePage.isCourseInTab("Kursus Ahli Pedang", "completed");
            Assert.assertFalse("Course 'Kursus Ahli Pedang' should not appear in Selesai tab", isPresent);
        }
    }
}
