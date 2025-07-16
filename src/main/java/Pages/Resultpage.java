package Pages;

import Utils.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Resultpage {
    WebDriver driver;

    private By result = By.xpath("//input[@name='result']");
    private By Back = By.xpath("//input[@value='Back']");

    public Resultpage(WebDriver driver) {

        this.driver = driver;
    }

    public void VerifyResult() throws InterruptedException {
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for password field to be visible
            WebElement Result = wait.until(ExpectedConditions.visibilityOfElementLocated(result));
            // Get the current value of the password field (if any)
            String Result_Value = Result.getText();
            System.out.println("Result value is " +Result_Value);

            // Wait for BacktoHome to be Clickable
            WebElement BacktoHome = wait.until(ExpectedConditions.elementToBeClickable(Back));
            BacktoHome.click();

        }
        catch (Exception e){
            System.err.println("---Error--- " + e.getMessage());
            e.printStackTrace();
        }

    }
}
