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

    public boolean isTabActive(String tabName) {
        if (tabName.equalsIgnoreCase("Dalam Progres")) {
            wait.until(ExpectedConditions.visibilityOf(inprogressTab));
            return inprogressTab.getAttribute("class").contains("active");
        } else if (tabName.equalsIgnoreCase("Selesai")) {
            wait.until(ExpectedConditions.visibilityOf(completedTab));
            return completedTab.getAttribute("class").contains("active");
        }
        return false;
    }

    public String getEmptyTabMessage(String tabName) {
        String xpath;
        if (tabName.equalsIgnoreCase("Dalam Progres")) {
            xpath = "//div[@id='inprogress']//p";
        } else {
            xpath = "//div[@id='completed']//p";
        }
        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return msg.getText().trim();
    }

    public boolean isAnyCourseCardDisplayed(String tabName) {
        String xpath;
        if (tabName.equalsIgnoreCase("Dalam Progres")) {
            xpath = "//div[@id='inprogress']//div[contains(@class,'card')]";
        } else {
            xpath = "//div[@id='completed']//div[contains(@class,'card')]";
        }
        return driver.findElements(By.xpath(xpath)).size() > 0;
    }

    public boolean isTabClickable(String tabName) {
        try {
            if (tabName.equalsIgnoreCase("Dalam Progres")) {
                wait.until(ExpectedConditions.elementToBeClickable(inprogressTab));
                return inprogressTab.isEnabled();
            } else if (tabName.equalsIgnoreCase("Selesai")) {
                wait.until(ExpectedConditions.elementToBeClickable(completedTab));
                return completedTab.isEnabled();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean isCourseInTab(String courseName, String tabId) {
        String xpathExpr = String.format("//div[@id='%s']//h6[text()='%s']", tabId, courseName);
        return driver.findElements(By.xpath(xpathExpr)).size() > 0;
    }

    public String getCourseProgressPercentage(String courseName) {
        String xpathExpr = String.format("//h6[text()='%s']/..//span[contains(@class,'progress-percentage')]",
                courseName);
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
