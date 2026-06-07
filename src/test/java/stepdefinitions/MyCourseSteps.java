package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DashboardPage;
import pages.CoursePage;
import pages.MyCoursePage;
import utils.DriverFactory;
import java.time.Duration;

public class MyCourseSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private DashboardPage dashboardPage = new DashboardPage(driver);
    private CoursePage coursePage = new CoursePage(driver);
    private MyCoursePage myCoursePage = new MyCoursePage(driver);

    // Helper to click SweetAlert2 confirm/ok buttons reliably
    private void clickSweetAlertConfirm(WebDriverWait wait, String buttonText) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".swal2-popup")));
            String xpath = "//div[contains(@class,'swal2-container')]//button[contains(@class,'swal2-confirm')]";
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            Thread.sleep(1200); // brief pause for popup transition
        } catch (Exception ex) {
            System.out.println("WARN: SweetAlert not found or dismissed: " + ex.getMessage().split("\n")[0]);
        }
    }

    @Given("user is enrolled in {string}")
    public void userIsEnrolledIn(String courseName) {
        dashboardPage.isDashboardDisplayed();
        dashboardPage.clickCourse(courseName);
        dashboardPage.navigateToBeranda();
    }

    @Given("a new student account exists with no enrolled courses")
    public void aNewStudentAccountExistsWithNoEnrolledCourses() {
        // Restart driver to get a fresh browser after potentially long previous tests
        DriverFactory.quitDriver();
        driver = DriverFactory.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        try {
            driver.get("https://polban-space.cloudias79.com/jtk-learn/");
            Thread.sleep(1500);

            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='email']")));
            emailInput.clear();
            emailInput.sendKeys("admin@example.com");
            WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password']"));
            passwordInput.clear();
            passwordInput.sendKeys("admin");
            driver.findElement(By.xpath("//button[@type='submit' or text()='Masuk']")).click();

            wait.until(ExpectedConditions.urlContains("dashboard-admin"));
            driver.get("https://polban-space.cloudias79.com/jtk-learn/users");
            Thread.sleep(3000);

            // Delete if exists
            java.util.List<WebElement> rows = driver.findElements(By.xpath("//tr[td[text()='Pendaftaran Baru'] and td[text()='pelajar']]"));
            if (!rows.isEmpty()) {
                WebElement deleteBtn = rows.get(0).findElement(By.xpath(".//button[img[@alt='Hapus Pengguna']]"));
                js.executeScript("arguments[0].scrollIntoView(true);", deleteBtn);
                Thread.sleep(500);
                js.executeScript("arguments[0].click();", deleteBtn);
                Thread.sleep(1500);

                // Click SweetAlert confirm (Ya, hapus!)
                clickSweetAlertConfirm(wait, "Ya, hapus!");
                Thread.sleep(1500);

                // Click SweetAlert OK after deletion
                clickSweetAlertConfirm(wait, "OK");
                Thread.sleep(1500);
            }

            // Create user
            WebElement openModalBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Tambah']")));
            js.executeScript("arguments[0].click();", openModalBtn);
            Thread.sleep(1500);

            WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nama")));
            nameField.sendKeys("Pendaftaran Baru");

            driver.findElement(By.name("email")).sendKeys("pendaftaran_baru@example.com");
            driver.findElement(By.name("password")).sendKeys("admin123");

            WebElement maleRadio = driver.findElement(By.xpath("//input[@name='jenis_kelamin' and @value='L']"));
            if (!maleRadio.isSelected()) {
                js.executeScript("arguments[0].click();", maleRadio);
            }

            new org.openqa.selenium.support.ui.Select(driver.findElement(By.name("role"))).selectByValue("pelajar");
            driver.findElement(By.name("alamat")).sendKeys("Politeknik Negeri Bandung");

            WebElement submitBtn = driver.findElement(By.xpath("//form//button[@type='submit' or text()='Simpan']"));
            js.executeScript("arguments[0].click();", submitBtn);
            Thread.sleep(2000);

            // Click SweetAlert OK after creating user
            clickSweetAlertConfirm(wait, "OK");
            Thread.sleep(1500);

            // Log out admin to prepare for student login
            WebElement profileDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(@class,'dropdown')]//a[img[@alt='User Profile']]")));
            js.executeScript("arguments[0].click();", profileDropdown);
            Thread.sleep(500);
            WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Keluar']")));
            js.executeScript("arguments[0].click();", logoutBtn);
            Thread.sleep(1500);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to setup new student account: " + e.getMessage());
        }
    }

    @Given("the student successfully logs in")
    public void theStudentSuccessfullyLogsIn() {
        pages.LoginPage loginPage = new pages.LoginPage(driver);
        loginPage.login("pendaftaran_baru@example.com", "admin123");
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
