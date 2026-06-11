package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestConfig;
import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "h3.greeting-title")
    private WebElement greetingTitle;

    @FindBy(xpath = "//li[contains(@class,'dropdown')]//a[img[@alt='User Profile'] or contains(@class,'nav-link')]")
    private WebElement accountDropdown;

    @FindBy(xpath = "//button[text()='Keluar']")
    private WebElement logoutButton;

    @FindBy(xpath = "//div[@id='navbarNav']//a[contains(@href, 'my-courses') or contains(text(),'Kursus Saya')]")
    private WebElement kursusSayaLink;

    @FindBy(xpath = "//div[@id='navbarNav']//a[contains(@href, 'dashboard') or contains(text(),'Beranda')]")
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
            String text = greetingTitle.getText();
            return text.contains("Hai,") || text.contains("Ahmad Joni") || text.contains("JONI") || text.contains("Joni");
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
        try {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, 0);");
            Thread.sleep(500);
        } catch (Exception e) {}
        
        wait.until(ExpectedConditions.elementToBeClickable(accountDropdown));
        try {
            accountDropdown.click();
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", accountDropdown);
        }
        // Allow a small delay for dropdown transition
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
        try {
            logoutButton.click();
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", logoutButton);
        }
        waitForLoading();
    }

    public void navigateToKursusSaya() {
        try {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, 0);");
            Thread.sleep(500);
        } catch (Exception e) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(kursusSayaLink));
            kursusSayaLink.click();
        } catch (Exception e) {
            try {
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", kursusSayaLink);
            } catch (Exception ex) {
                System.out.println("Could not click Kursus Saya link, navigating directly.");
            }
        }
        waitForLoading();

        // Wait for URL to change to my-courses first
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("my-courses"));
        } catch (Exception e) {
            System.out.println("URL did not change to my-courses, performing direct navigation.");
            driver.get(TestConfig.MY_COURSES_URL);
            waitForLoading();
        }
    }

    public void navigateToBeranda() {
        try {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, 0);");
            Thread.sleep(500);
        } catch (Exception e) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(berandaLink));
            berandaLink.click();
        } catch (Exception e) {
            try {
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", berandaLink);
            } catch (Exception ex) {
                System.out.println("Could not click Beranda link, navigating directly.");
            }
        }
        waitForLoading();

        // Wait for URL to change to dashboard first
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("dashboard"));
        } catch (Exception e) {
            System.out.println("URL did not change to dashboard, performing direct navigation.");
            driver.get(TestConfig.STUDENT_DASHBOARD_URL);
            waitForLoading();
        }
    }

    public void clickCourse(String courseName) {
        WebElement courseCard = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h6[text()='" + courseName + "']/ancestor::div[contains(@class,'card')]")
        ));
        // Scroll into view to avoid header/footer interception
        try {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", courseCard);
            Thread.sleep(500);
        } catch (Exception e) {
            // Ignore scroll exceptions
        }

        // Wait until clickable and attempt normal click
        wait.until(ExpectedConditions.elementToBeClickable(courseCard));
        try {
            courseCard.click();
        } catch (Exception e) {
            System.out.println("Standard click intercepted, falling back to JavaScript click.");
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", courseCard);
        }
        waitForLoading();
    }

    public boolean isNavigationMenuPresent(String menuName) {
        try {
            String xpathExpr = String.format("//a[contains(@class,'nav-link') and normalize-space(text())='%s']", menuName);
            WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpr)));
            return menu.isDisplayed();
        } catch (Exception e) {
            return false;
        }
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

    public boolean isAccountDropdownDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(accountDropdown));
            return accountDropdown.isDisplayed();
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

    public boolean isLogoutButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(logoutButton));
            return logoutButton.isDisplayed();
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
