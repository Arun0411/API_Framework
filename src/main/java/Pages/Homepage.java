package Pages;

import Utils.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Homepage {
    WebDriver driver;

    private By username_field = By.xpath("//input[@name='agent_identity[email]']");
    private By password_field = By.xpath("//input[@name='agent_identity[password]']");
    private By loginButton = By.xpath("//input[@value='Continue']");
    private By loginButton1 = By.xpath("//input[@value='Log in']");

    public Homepage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) throws InterruptedException {
        String URL = Utilities.getProperty("URL");
        driver.get(URL);
        Thread.sleep(5000);

        driver.findElement(username_field).sendKeys(username);
        Thread.sleep(5000);
        driver.findElement(loginButton).click();
        Thread.sleep(5000);
        driver.findElement(password_field).sendKeys(password);
        Thread.sleep(5000);
        driver.findElement(loginButton1).click();
    }
}

//public class Homepage {
//    WebDriver driver;
//
//    private By inputfield = By.xpath("//input[@name='inputfield']");
//    private By SubmitButton = By.xpath("//input[@value='Submit']");
//    private By BacktoHome = By.xpath("//input[@value='Back']");
//
//    public Homepage(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    public void EnterInputandSubmit(String input) throws InterruptedException {
//        String URL = Utilities.getProperty("URL");
//        try{
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//            // Wait for input field to be visible and enter value
//            WebElement inputfield = wait.until(ExpectedConditions.visibilityOfElementLocated(inputfield));
//            inputfield.sendKeys(input);
//
//            // Wait for the submit button to be clickable and click
//            WebElement SubmitButton = wait.until(ExpectedConditions.elementToBeClickable(SubmitButton));
//            SubmitButton.click();
//
//            System.out.println("Input is entered and Submit button is clicked");
//        }
//        catch (Exception e){
//            System.err.println("---Error--- " + e.getMessage());
//            e.printStackTrace();
//        }
//
//    }
//}
