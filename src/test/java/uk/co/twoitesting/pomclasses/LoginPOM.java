// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Selenium classes for web elements and waits
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.twoitesting.utilities.ConfigLoader;
import uk.co.twoitesting.utilities.Helpers;

// Define LoginPOM class for actions on the login page
public class LoginPOM {

    // Store the browser driver to control the browser
    private WebDriver driver;
    // Store WebDriverWait to wait for elements to appear
    private WebDriverWait wait;

    // Locators for username, password fields, and login button
    private By usernameField = By.id("username"); // Username input
    private By passwordField = By.id("password"); // Password input
    private By loginButton = By.name("login");   // Login button
    private By logoutButton = By.linkText("Logout");

    // Constructor to set up LoginPOM with browser driver and wait
    public LoginPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; // Save driver
        this.wait = wait;     // Save wait
    }

    // Method to open the login page
    public void open() {
        driver.get(ConfigLoader.get("base.url") + "/my-account/"); // Go to login page
    }

    // Method to log in
    // Login using credentials from ConfigLoader
    public boolean login() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField))
                    .sendKeys(ConfigLoader.get("username"));
            driver.findElement(passwordField).sendKeys(ConfigLoader.get("password"));
            driver.findElement(loginButton).click();
            return true; // Login attempted successfully
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "LoginError"); // Take screenshot for debugging
            return false; // Login failed
        }
    }

    // Check if user is logged in by waiting for logout button
    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
