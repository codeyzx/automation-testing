package utils;

import java.time.Duration;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.LoginPage;

public class UserTestDataManager {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;
    private final LoginPage loginPage;

    public UserTestDataManager(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
        this.loginPage = new LoginPage(driver);
    }

    public void resetDefaultStudent() {
        resetStudent(
                TestConfig.DEFAULT_STUDENT_NAME,
                TestConfig.DEFAULT_STUDENT_EMAIL,
                TestConfig.DEFAULT_STUDENT_PASSWORD);
    }

    public void createStudentWithoutCourses() {
        resetStudent(
                TestConfig.NEW_STUDENT_NAME,
                TestConfig.NEW_STUDENT_EMAIL,
                TestConfig.NEW_STUDENT_PASSWORD);
    }

    public void loginAsNewStudent() {
        loginPage.navigateToLoginPage();
        loginPage.login(TestConfig.NEW_STUDENT_EMAIL, TestConfig.NEW_STUDENT_PASSWORD);
    }

    private void resetStudent(String name, String email, String password) {
        try {
            loginAsAdmin();
            openUsersPage();
            deleteUserIfExists(name);
            createStudent(name, email, password);
            logoutCurrentUser();
        } catch (Exception e) {
            savePageSourceForDebugging();
            e.printStackTrace();
            Assert.fail("Failed to prepare student account '" + name + "': " + e.getMessage());
        }
    }

    private void loginAsAdmin() {
        loginPage.navigateToLoginPage();
        loginPage.login(TestConfig.ADMIN_EMAIL, TestConfig.ADMIN_PASSWORD);
        wait.until(ExpectedConditions.urlContains("dashboard-admin"));
    }

    private void openUsersPage() throws InterruptedException {
        driver.get(TestConfig.USERS_URL);
        Thread.sleep(2000);
    }

    private void deleteUserIfExists(String name) throws InterruptedException {
        List<WebElement> rows = driver.findElements(By.xpath(
                "//tbody/tr[td[normalize-space(text())='" + name + "'] and td[normalize-space(text())='pelajar']]"));

        if (rows.isEmpty()) {
            return;
        }

        WebElement deleteButton = rows.get(0).findElement(By.xpath(".//button[img[@alt='Hapus Pengguna']]"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", deleteButton);
        Thread.sleep(700);
        js.executeScript("arguments[0].click();", deleteButton);
        Thread.sleep(1000);

        clickSweetAlertConfirm();
        Thread.sleep(1000);
        clickSweetAlertConfirm();
        Thread.sleep(1000);
        openUsersPage();
    }

    private void createStudent(String name, String email, String password) throws InterruptedException {
        WebElement openModalButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Tambah']")));
        js.executeScript("arguments[0].click();", openModalButton);

        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nama")));
        nameField.clear();
        nameField.sendKeys(name);

        WebElement emailField = driver.findElement(By.name("email"));
        emailField.clear();
        emailField.sendKeys(email);

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement maleRadio = driver.findElement(By.xpath("//input[@name='jenis_kelamin' and @value='L']"));
        if (!maleRadio.isSelected()) {
            js.executeScript("arguments[0].click();", maleRadio);
        }

        new Select(driver.findElement(By.name("role"))).selectByValue("pelajar");

        WebElement addressField = driver.findElement(By.name("alamat"));
        addressField.clear();
        addressField.sendKeys("Politeknik Negeri Bandung");

        WebElement submitButton = driver.findElement(By.xpath("//form//button[@type='submit' or text()='Simpan']"));
        js.executeScript("arguments[0].click();", submitButton);
        Thread.sleep(1500);

        clickSweetAlertConfirm();
        Thread.sleep(1000);
    }

    private void logoutCurrentUser() throws InterruptedException {
        WebElement profileDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'dropdown')]//a[img[@alt='User Profile']]")));
        js.executeScript("arguments[0].click();", profileDropdown);
        Thread.sleep(600);

        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Keluar']")));
        js.executeScript("arguments[0].click();", logoutButton);
        Thread.sleep(1000);
    }

    private void clickSweetAlertConfirm() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".swal2-popup")));
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'swal2-container')]//button[contains(@class,'swal2-confirm')]")));
            js.executeScript("arguments[0].click();", confirmButton);
        } catch (Exception e) {
            System.out.println("WARN: SweetAlert confirmation was not available: " + e.getMessage().split("\n")[0]);
        }
    }

    private void savePageSourceForDebugging() {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("target"));
            java.nio.file.Files.write(
                    java.nio.file.Paths.get("target/error_pagesource.html"),
                    driver.getPageSource().getBytes());
        } catch (Exception e) {
            System.err.println("Could not save error page source: " + e.getMessage());
        }
    }
}
