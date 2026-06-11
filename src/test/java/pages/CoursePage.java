package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CoursePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//button[contains(@class,'button-overview') or contains(text(),'Kursus') or contains(text(),'Lihat')]")
    private WebElement startOrContinueCourseButton;

    @FindBy(css = "h3.course-title")
    private WebElement courseTitle;

    public CoursePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        PageFactory.initElements(driver, this);
    }

    public void startOrContinueCourse() {
        try {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", startOrContinueCourseButton);
            Thread.sleep(500);
        } catch (Exception e) {}

        wait.until(ExpectedConditions.elementToBeClickable(startOrContinueCourseButton));
        try {
            startOrContinueCourseButton.click();
        } catch (Exception e) {
            System.out.println("Start/continue button click intercepted, using JavascriptExecutor click.");
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", startOrContinueCourseButton);
        }
        waitForLoading();
    }

    public void selectInstruction(String instructionName) {
        WebElement instructionItem = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'sidebar-container')]//*[contains(text(),'" + instructionName + "')]")
        ));
        instructionItem.click();
        waitForLoading();
    }

    public void completeInstruction() {
        // Look for any typical complete button or checkbox, if present
        try {
            WebElement completeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Selesai') or contains(text(),'Tandai') or contains(text(),'Complete')]")
            ));
            completeBtn.click();
            waitForLoading();
        } catch (Exception e) {
            // If no explicit complete button is found, try clicking 'Selanjutnya' to trigger completion
            try {
                WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class,'course-next-button') or contains(text(),'Selanjutnya')]")
                ));
                nextBtn.click();
                waitForLoading();
            } catch (Exception ex) {
                System.out.println("No explicit completion button or next button clicked: " + ex.getMessage());
            }
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
