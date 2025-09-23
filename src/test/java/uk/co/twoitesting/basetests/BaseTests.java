// Define the package where this class belongs
package uk.co.twoitesting.basetests;

// Import JUnit 5 annotations and classes for testing
import org.junit.jupiter.api.*;
// Import Selenium WebDriver classes
import org.openqa.selenium.*;
// Import ChromeDriver to control Chrome browser

import org.openqa.selenium.support.ui.WebDriverWait;
// Import Duration class to set timeouts
import java.time.Duration;

// Import Page Object Model (POM) classes
import uk.co.twoitesting.pomclasses.AccountPOM;
import uk.co.twoitesting.pomclasses.CartPOM;
import uk.co.twoitesting.pomclasses.LoginPOM;
import uk.co.twoitesting.pomclasses.ShopPOM;

// Import helper utilities
import uk.co.twoitesting.utilities.Helpers;

// Define the BaseTests class which other test classes can extend
public class BaseTests {

    // Declare WebDriver variable to control the browser
    protected WebDriver driver;
    // Declare WebDriverWait variable to wait for elements
    protected WebDriverWait wait;

    // Declare Page Object Model instances for different pages
    protected LoginPOM loginPOM;
    protected ShopPOM shopPOM;
    protected CartPOM cartPOM;
    protected AccountPOM accountPOM;
    // Declare Helpers instance for utility functions
    protected Helpers helpers;

    // Method that runs before each test
    @BeforeEach
    void setUpBase() {
        // Initialize browser
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox":
                driver = new org.openqa.selenium.firefox.FirefoxDriver();
                break;
            case "chrome":
            default:
                driver = new org.openqa.selenium.chrome.ChromeDriver();
                break;
        }


        // Maximize the browser window
        driver.manage().window().maximize();
        // Initialize WebDriverWait with a 10-second timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Initialize helper utilities
        helpers = new Helpers();

        // Initialize page objects, passing driver, wait, and helpers as needed
        loginPOM = new LoginPOM(driver, wait);
        shopPOM = new ShopPOM(driver, wait);
        cartPOM = new CartPOM(driver, wait, helpers);
        accountPOM = new AccountPOM(driver);

        // Ensure the shopping cart is empty before starting tests
        emptyCart();
    }

    // Method that runs after each test
    @AfterEach
    void tearDownBase() {
        // Take a screenshot of the browser before closing
        Helpers.takeScreenshot(driver, "FinalState");
        // Empty the cart again to leave the site clean
        emptyCart();
        // Log out from the account
        accountPOM.logout();
        // Close the browser and clean up resources
        driver.quit();
    }

    // Helper method to empty the shopping cart
    protected void emptyCart() {
        try {
            // Go to the cart page
            driver.get("https://www.edgewordstraining.co.uk/demo-site/cart/");
            // Remove a specific coupon code if applied
            cartPOM.removeCoupon("edgewords");
            // Remove another coupon code if applied
            cartPOM.removeCoupon("2idiscount");
            // Remove any product in the cart
            cartPOM.removeProduct();
            // Print a success message in the console
            System.out.println("Cart emptied successfully.");
        } catch (Exception e) {
            // Print a message if cart was already empty or some error occurred
            System.out.println("Cart already empty or error: " + e.getMessage());
        }
    }
}
