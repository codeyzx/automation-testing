package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MyCoursePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "inprogress-tab")
    private WebElement inprogressTab;

    @FindBy(id = "completed-tab")
    private WebElement completedTab;

    public MyCoursePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        PageFactory.initElements(driver, this);
    }

    public void clickInProgressTab() {
        wait.until(ExpectedConditions.elementToBeClickable(inprogressTab));
        inprogressTab.click();
        waitForLoading();
    }

    public void clickCompletedTab() {
        wait.until(ExpectedConditions.elementToBeClickable(completedTab));
        completedTab.click();
        waitForLoading();
    }

    public boolean isCourseInTab(String courseName, String tabId) {
        String xpathExpr = String.format("//div[@id='%s']//h6[text()='%s']", tabId, courseName);
        return driver.findElements(By.xpath(xpathExpr)).size() > 0;
    }

    public String getCourseProgressPercentage(String courseName) {
        String xpathExpr = String.format("//h6[text()='%s']/..//span[contains(@class,'progress-percentage')]", courseName);
        WebElement progressText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpr)));
        return progressText.getText().trim();
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
