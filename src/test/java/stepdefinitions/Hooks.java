package stepdefinitions;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.DriverFactory;

public class Hooks {

    private WebDriver driver;

    @Before
    public void setUp() {
        driver = DriverFactory.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
                
                // Save screenshot file to disk for analysis
                java.io.File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                java.io.File destFile = new java.io.File("target/failure-screenshot.png");
                java.io.File parent = destFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                java.nio.file.Files.copy(screenshotFile.toPath(), destFile.toPath(), 
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Saved failure screenshot to: " + destFile.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("Error taking screenshot: " + e.getMessage());
            }
        }
        DriverFactory.quitDriver();
    }
}

