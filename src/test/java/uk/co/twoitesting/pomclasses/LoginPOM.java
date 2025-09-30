// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Selenium classes for interacting with web elements and handling waits
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
// Import utility classes for configuration and helper functions
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
import uk.co.twoitesting.utilities.*;

// Define the LoginPOM class for performing actions on the login page
public class LoginPOM {

    // Store the browser driver instance to control browser actions
    private final WebDriver driver;

    // Store WebDriverWait instance to handle explicit waits
    private final WebDriverWait wait;

    // Locators for login page elements
    private final By usernameField = By.id("username"); // Locator for username input field
    private final By passwordField = By.id("password"); // Locator for password input field
    private final By loginButton = By.name("login");    // Locator for login button
    private final By logoutButton = By.linkText("Logout"); // Locator for logout link (to verify login)

    // Constructor to initialize the LoginPOM with browser driver and wait
    public LoginPOM(WebDriver driver, WebDriverWait wait, NavPOM navPOM) {
        this.driver = driver; // Save WebDriver instance
        this.wait = wait;     // Save WebDriverWait instance
    }

    // Method to open the login page in the browser
    public void open() {
        driver.get(ConfigLoader.get("base.url") + "/my-account/");
        // Navigate to login page URL (base URL + /my-account/)
    }

    // Method to log in using credentials from config.properties
    public boolean login() {
        try {
            // Wait until the username field is visible, then enter the username
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField))
                    .sendKeys(ConfigLoader.get("username"));

            // Enter the password into the password field
            driver.findElement(passwordField).sendKeys(ConfigLoader.get("password"));

            // Click the login button
            driver.findElement(loginButton).click();

            return true; // Return true if login attempted successfully
        } catch (Exception e) {
            // Handle any exceptions (e.g., element not found, timeout)
            System.out.println("Login failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "LoginError");
            // Take a screenshot for debugging purposes
            return false; // Return false if login failed
        }
    }

    // Method to check if the user is logged in
    public boolean isUserLoggedIn() {
        try {
            // Wait until logout button is visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
            return true; // Logout button found → user is logged in
        } catch (Exception e) {
            return false; // Logout button not found → user is not logged in
        }
    }
}
