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
import pages.LoginPage;
import pages.DashboardPage;
import utils.DriverFactory;
import java.time.Duration;
import java.util.List;

public class LoginSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage(driver);
    private DashboardPage dashboardPage = new DashboardPage(driver);

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
        resetStudentAhmadJoni();
        loginPage.navigateToLoginPage();
        loginPage.login("ahmadjonii@gmail.com", "ahmadjoni");
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
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.urlContains("dashboard-pengajar"));
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

    private void waitForSwalAndClick(WebDriver drv, long timeoutSec) {
        WebDriverWait swalWait = new WebDriverWait(drv, Duration.ofSeconds(timeoutSec));
        try {
            swalWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".swal2-popup")));
            WebElement btn = swalWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'swal2-container')]//button[contains(@class,'swal2-confirm')]")));
            ((org.openqa.selenium.JavascriptExecutor) drv).executeScript("arguments[0].click();", btn);
            Thread.sleep(1200); // brief pause for popup transition
        } catch (Exception ex) {
            System.out.println("WARN: SweetAlert not found or already dismissed. " + ex.getMessage().split("\n")[0]);
        }
    }

    private void resetStudentAhmadJoni() {
        WebDriver driver = DriverFactory.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        try {
            driver.get("https://polban-space.cloudias79.com/jtk-learn/");
            Thread.sleep(1500);

            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='email']")));
            emailInput.clear();
            emailInput.sendKeys("admin@example.com");
            driver.findElement(By.xpath("//input[@type='password']")).sendKeys("admin");
            driver.findElement(By.xpath("//button[@type='submit' or text()='Masuk']")).click();

            wait.until(ExpectedConditions.urlContains("dashboard-admin"));
            driver.get("https://polban-space.cloudias79.com/jtk-learn/users");
            Thread.sleep(3000);

            // Delete Ahmad Joni (pelajar) if exists - use exact text match for both columns
            java.util.List<WebElement> rows = driver.findElements(
                    By.xpath("//tbody/tr[td[normalize-space(text())='Ahmad Joni'] and td[normalize-space(text())='pelajar']]"));

            if (rows.isEmpty()) {
                System.out.println("DEBUG: Ahmad Joni (pelajar) not found. Will create fresh.");
            } else {
                System.out.println("DEBUG: Found " + rows.size() + " Ahmad Joni pelajar row(s). Deleting...");
                WebElement deleteBtn = rows.get(0).findElement(By.xpath(".//button[img[@alt='Hapus Pengguna']]"));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", deleteBtn);
                Thread.sleep(700);
                js.executeScript("arguments[0].click();", deleteBtn);
                Thread.sleep(1500);

                // Click delete confirmation (Ya, hapus!)
                waitForSwalAndClick(driver, 10);
                Thread.sleep(1000);

                // Click success OK notification
                waitForSwalAndClick(driver, 10);
                Thread.sleep(1000);

                // Reload page to ensure fresh state
                driver.get("https://polban-space.cloudias79.com/jtk-learn/users");
                Thread.sleep(2000);
            }

            // Create Ahmad Joni (pelajar) fresh
            WebElement openModalBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Tambah']")));
            js.executeScript("arguments[0].click();", openModalBtn);
            Thread.sleep(1500);

            WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nama")));
            nameField.clear();
            nameField.sendKeys("Ahmad Joni");

            WebElement emailField = driver.findElement(By.name("email"));
            emailField.clear();
            emailField.sendKeys("ahmadjonii@gmail.com");

            WebElement pwdField = driver.findElement(By.name("password"));
            pwdField.clear();
            pwdField.sendKeys("ahmadjoni");

            WebElement maleRadio = driver.findElement(By.xpath("//input[@name='jenis_kelamin' and @value='L']"));
            if (!maleRadio.isSelected()) {
                js.executeScript("arguments[0].click();", maleRadio);
            }

            new org.openqa.selenium.support.ui.Select(driver.findElement(By.name("role"))).selectByValue("pelajar");
            driver.findElement(By.name("alamat")).sendKeys("Politeknik Negeri Bandung");

            WebElement submitBtn = driver.findElement(By.xpath("//form//button[@type='submit' or text()='Simpan']"));
            js.executeScript("arguments[0].click();", submitBtn);
            Thread.sleep(2000);

            // Click OK after user creation
            waitForSwalAndClick(driver, 10);
            Thread.sleep(1000);

            // Log out admin
            WebElement profileDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@class,'dropdown')]//a[img[@alt='User Profile']]")));
            js.executeScript("arguments[0].click();", profileDropdown);
            Thread.sleep(600);
            WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[text()='Keluar']")));
            js.executeScript("arguments[0].click();", logoutBtn);
            Thread.sleep(1500);

        } catch (Exception e) {
            System.err.println("ERROR during resetStudentAhmadJoni! URL: " + driver.getCurrentUrl());
            try {
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get("target"));
                java.nio.file.Files.write(java.nio.file.Paths.get("target/error_pagesource.html"),
                        driver.getPageSource().getBytes());
                System.err.println("DEBUG: Saved error page source to target/error_pagesource.html");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

