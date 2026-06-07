package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "h3.greeting-title")
    private WebElement greetingTitle;

    @FindBy(xpath = "//li[contains(@class,'dropdown')]//a[img[@alt='User Profile']]")
    private WebElement accountDropdown;

    @FindBy(xpath = "//button[text()='Keluar']")
    private WebElement logoutButton;

    @FindBy(xpath = "//a[contains(text(),'Kursus Saya')]")
    private WebElement kursusSayaLink;

    @FindBy(xpath = "//a[contains(text(),'Beranda')]")
    private WebElement berandaLink;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        PageFactory.initElements(driver, this);
    }

    public boolean isDashboardDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(greetingTitle));
            waitForLoading();
            return greetingTitle.getText().contains("Hai,");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDashboardDisplayed(String expectedGreeting) {
        try {
            wait.until(ExpectedConditions.visibilityOf(greetingTitle));
            waitForLoading();
            return greetingTitle.getText().contains(expectedGreeting);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAccountDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(accountDropdown));
        accountDropdown.click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getAccountDropdownText() {
        wait.until(ExpectedConditions.visibilityOf(accountDropdown));
        return accountDropdown.getText().trim();
    }

    public void clickLogoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButton.click();
        waitForLoading();
    }

    public void navigateToKursusSaya() {
        wait.until(ExpectedConditions.elementToBeClickable(kursusSayaLink));
        kursusSayaLink.click();
        waitForLoading();
    }

    public void navigateToBeranda() {
        wait.until(ExpectedConditions.elementToBeClickable(berandaLink));
        berandaLink.click();
        waitForLoading();
    }

    public void clickCourse(String courseName) {
        WebElement courseCard = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[text()='" + courseName + "']/ancestor::div[contains(@class,'card')]")
        ));
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", courseCard);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        wait.until(ExpectedConditions.elementToBeClickable(courseCard));
        try {
            courseCard.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", courseCard);
        }
        waitForLoading();
    }

    public boolean isNavigationMenuDisplayed(String menuName) {
        try {
            String xpath;
            if (menuName.equalsIgnoreCase("Rekap Hasil Kuis")) {
                xpath = "//a[contains(@class,'nav-link') and (text()='Rekap Hasil Kuis' or text()='Pemantauan')]";
            } else {
                xpath = "//a[contains(@class,'nav-link') and text()='" + menuName + "']";
            }
            WebElement menu = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            return menu.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccountNameDisplayed(String expectedName) {
        try {
            WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//li[contains(@class,'dropdown')]//a[contains(.,'" + expectedName + "')]")
            ));
            return nameElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForLoading() {
        try {
            Thread.sleep(1500);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(text(),'Loading')]")));
            Thread.sleep(500);
        } catch (Exception e) {
            // Ignore
        }
    }
}
