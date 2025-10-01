// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Selenium classes for interacting with web elements
import org.openqa.selenium.*;
// Import Selenium support classes for explicit waits (not used in this snippet but often needed)
import org.openqa.selenium.support.ui.*;
// Import NavPOM in case we want to use navigation methods (currently not used in this snippet)
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;

// Define the AccountPOM class which handles account-related actions (e.g., logout)
public class AccountPOM {

    // WebDriver instance used to interact with the browser
    private final WebDriver driver;

    // Locator for the "Logout" link on the account page
    private final By logoutLink = By.linkText("Logout");

    // Constructor for AccountPOM, takes WebDriver, WebDriverWait, and NavPOM as arguments
    public AccountPOM(WebDriver driver, WebDriverWait wait, NavPOM navPOM) {
        this.driver = driver; // Assign the driver instance to the class variable
        // Note: WebDriverWait and NavPOM are passed in but not currently used in this class
    }

    // Method to log out from the current user account
    public void logout() {
        try {
            // Find the logout link element on the page using the locator
            WebElement logout = driver.findElement(logoutLink);
            // Check if the logout link is currently visible on the page
            if (logout.isDisplayed()) {
                // Click the logout link to log the user out
                logout.click();
                // Print a success message to the console
                System.out.println("Logged out successfully.");
            }
        } catch (NoSuchElementException e) {
            // Catch the exception if the logout link is not found (user not logged in)
            System.out.println("No user logged in, skipping logout."); // Print message indicating logout was skipped
        }
    }
}
