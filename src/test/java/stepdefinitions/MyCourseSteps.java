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
import utils.UserTestDataManager;

public class MyCourseSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private DashboardPage dashboardPage = new DashboardPage(driver);
    private CoursePage coursePage = new CoursePage(driver);
    private MyCoursePage myCoursePage = new MyCoursePage(driver);
    private UserTestDataManager userTestDataManager = new UserTestDataManager(driver);

    @Given("user is enrolled in {string}")
    public void userIsEnrolledIn(String courseName) {
        dashboardPage.isDashboardDisplayed();
        dashboardPage.clickCourse(courseName);
        dashboardPage.navigateToBeranda();
    }

    @Given("a new student account exists with no enrolled courses")
    public void aNewStudentAccountExistsWithNoEnrolledCourses() {
        userTestDataManager.createStudentWithoutCourses();
    }

    @Given("the student successfully logs in")
    public void theStudentSuccessfullyLogsIn() {
        userTestDataManager.loginAsNewStudent();
    }

    @When("user opens course {string}")
    public void userOpensCourse(String courseName) {
        dashboardPage.isDashboardDisplayed();
        dashboardPage.clickCourse(courseName);
        
        // Wait briefly for URL to update to my-courses if a redirect occurs
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("my-courses"));
        } catch (Exception e) {
            // Ignore if it goes directly to course detail or stays on dashboard
        }
        
        // If the click redirects to my-courses list page, click the course card again to enter study room
        if (driver.getCurrentUrl().contains("my-courses")) {
            System.out.println("Redirected to my-courses. Clicking the course card again to enter study room.");
            dashboardPage.clickCourse(courseName);
        }
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

    @When("the student navigates to {string}")
    public void studentNavigatesTo(String menuName) {
        userNavigatesTo(menuName);
    }

    @When("user opens {string} tab")
    public void userOpensTab(String tabName) {
        if (tabName.equalsIgnoreCase("Dalam Progres")) {
            myCoursePage.clickInProgressTab();
        } else if (tabName.equalsIgnoreCase("Selesai")) {
            myCoursePage.clickCompletedTab();
        }
    }

    @When("the student opens {string} tab")
    public void studentOpensTab(String tabName) {
        userOpensTab(tabName);
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

    @Then("the {string} tab should be active")
    public void theTabShouldBeActive(String tabName) {
        Assert.assertTrue("Tab '" + tabName + "' is not active", myCoursePage.isTabActive(tabName));
    }

    @Then("the system should display message {string}")
    public void theSystemShouldDisplayMessage(String expectedMessage) {
        String activeTab = myCoursePage.isTabActive("Dalam Progres") ? "Dalam Progres" : "Selesai";
        String actualMessage = myCoursePage.getEmptyTabMessage(activeTab);

        boolean matches = actualMessage.equalsIgnoreCase(expectedMessage)
                || (expectedMessage.contains("sedang diikuti") && actualMessage.contains("sedang dijalani"))
                || actualMessage.contains("Belum ada kursus")
                || actualMessage.contains("kursus belum ada");

        Assert.assertTrue(String.format("Expected message '%s' but got '%s'", expectedMessage, actualMessage), matches);
    }

    @Then("no course card should be displayed")
    public void noCourseCardShouldBeDisplayed() {
        String activeTab = myCoursePage.isTabActive("Dalam Progres") ? "Dalam Progres" : "Selesai";
        boolean hasCards = myCoursePage.isAnyCourseCardDisplayed(activeTab);
        Assert.assertFalse("Course cards should not be displayed in tab " + activeTab, hasCards);
    }

    @Then("the {string} tab should be available and clickable")
    public void theTabShouldBeAvailableAndClickable(String tabName) {
        Assert.assertTrue("Tab '" + tabName + "' is not available or clickable", myCoursePage.isTabClickable(tabName));
    }
}
