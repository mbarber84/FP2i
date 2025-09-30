// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Selenium classes for working with web elements
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;

// Define the AccountPOM class for account-related actions
public class AccountPOM {

    // Store the browser driver so we can control the browser
    private final WebDriver driver;

    // Locate the "Logout" link on the page
    private final By logoutLink = By.linkText("Logout");

    // Constructor to set up the class with the browser driver
    public AccountPOM(WebDriver driver, WebDriverWait wait, NavPOM navPOM) {
        this.driver = driver; // Save the driver so we can use it in this class
    }

    // Method to log out from the account
    public void logout() {
        try {
            // Find the logout link element on the page
            WebElement logout = driver.findElement(logoutLink);
            // Check if the logout link is visible on the page
            if (logout.isDisplayed()) {
                // Click the logout link to log out
                logout.click();
                // Print a message to the console
                System.out.println("Logged out successfully.");
            }
        } catch (NoSuchElementException e) {
            // If the logout link is not found (user not logged in), print a message
            System.out.println("No user logged in, skipping logout.");
        }
    }
}

