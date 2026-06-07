package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//input[@type='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit' or text()='Masuk']")
    private WebElement loginButton;

    @FindBy(css = "h5.card-title")
    private WebElement welcomeTitle;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        PageFactory.initElements(driver, this);
    }

    public void navigateToLoginPage() {
        int maxRetries = 3;
        for (int i = 1; i <= maxRetries; i++) {
            try {
                driver.get("https://polban-space.cloudias79.com/jtk-learn/");
                waitForPageLoad();
                return; // success
            } catch (Exception e) {
                System.out.println("WARN: navigateToLoginPage attempt " + i + " failed: " + e.getMessage().split("\n")[0]);
                if (i == maxRetries) throw e;
                try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.elementToBeClickable(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isWelcomeTitleDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(welcomeTitle));
            return welcomeTitle.getText().contains("Selamat datang,");
        } catch (Exception e) {
            return false;
        }
    }

    public String getWelcomeTitleText() {
        wait.until(ExpectedConditions.visibilityOf(welcomeTitle));
        return welcomeTitle.getText();
    }

    private void waitForPageLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
