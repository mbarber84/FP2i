// Define the package where this class belongs
package uk.co.twoitesting.basetests;

// Import JUnit 5 annotations and classes for testing
import org.junit.jupiter.api.*;
// Import Selenium WebDriver classes to interact with the browser
import org.openqa.selenium.*;
// Import Selenium WebDriverWait for waiting until elements are ready
import org.openqa.selenium.support.ui.WebDriverWait;
// Import Duration class to specify timeouts
import java.time.Duration;

// Import Page Object Model (POM) classes for different pages
import uk.co.twoitesting.pomclasses.*;

// Import NavPOM component for navigation elements
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
// Import Helpers utility class for reusable functions
import uk.co.twoitesting.utilities.Helpers;

// Define the BaseTests class which other test classes can extend
public class BaseTests {

    // Declare WebDriver variable to control the browser instance
    protected WebDriver driver;
    // Declare WebDriverWait variable to wait for elements to appear
    protected WebDriverWait wait;

    // Declare page object variables for different pages in the application
    protected LoginPOM loginPOM;
    protected ShopPOM shopPOM;
    protected CartPOM cartPOM;
    protected AccountPOM accountPOM;
    // Declare a Helpers instance for utility functions like screenshots
    protected Helpers helpers;

    // Declare NavPOM variable for handling navigation bar elements
    protected NavPOM navPOM;

    // Method that runs before each test method in the class
    @BeforeEach
    void setUpBase() {
        // Get the 'browser' system property or default to "chrome"
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        // Switch statement to initialize the WebDriver based on the browser type
        switch (browser) {
            case "firefox":
                // If browser is Firefox, create a FirefoxDriver instance
                driver = new org.openqa.selenium.firefox.FirefoxDriver();
                break;
            case "chrome":
            default:
                // Default case is Chrome browser, create a ChromeDriver instance
                driver = new org.openqa.selenium.chrome.ChromeDriver();
                break;
        }

        // Maximize the browser window for better visibility
        driver.manage().window().maximize();
        // Initialize WebDriverWait with a timeout of 15 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Initialize helper utilities instance for reusable helper functions
        helpers = new Helpers();

        // Initialize Page Object Model (POM) instances and pass driver, wait, and navPOM as needed
        loginPOM = new LoginPOM(driver, wait, navPOM);  // POM for login page
        shopPOM = new ShopPOM(driver, wait);            // POM for shop page
        cartPOM = new CartPOM(driver, wait);            // POM for cart page
        accountPOM = new AccountPOM(driver, wait, navPOM); // POM for account page

        // Initialize NavPOM instance for navigation bar components
        navPOM = new NavPOM(driver, wait);
    }

    // Method that runs after each test method to clean up resources
    @AfterEach
    void tearDownBase() {
        // Take a screenshot of the browser before closing it
        Helpers.takeScreenshot(driver, "FinalState");
        // Empty the shopping cart to leave the site in a clean state
        emptyCart();
        // Log out from the account after test completion
        accountPOM.logout();
        // Close the browser and release resources
        driver.quit();
    }

    // Helper method to empty the shopping cart
    protected void emptyCart() {
        try {
            // Navigate directly to the cart page
            driver.get("base.url" + "/cart/");
            // Remove a specific coupon code named "edgewords" if applied
            cartPOM.removeCoupon("edgewords");
            // Remove another coupon code named "2idiscount" if applied
            cartPOM.removeCoupon("2idiscount");
            // Remove any product in the cart to empty it
            cartPOM.removeProduct();
            // Print a message indicating the cart was emptied successfully
            System.out.println("Cart emptied successfully.");
        } catch (Exception e) {
            // Catch any errors and print a message if cart is already empty or another error occurs
            System.out.println("Cart already empty or error: " + e.getMessage());
        }
    }
}
