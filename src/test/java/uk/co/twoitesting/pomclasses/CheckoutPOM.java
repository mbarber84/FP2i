// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Selenium WebDriver classes for browser interaction
import org.openqa.selenium.*;
// Import Selenium support classes for explicit waits
import org.openqa.selenium.support.ui.*;
// Import NavPOM to navigate between pages if needed
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
// Import helper utilities like screenshot capture
import uk.co.twoitesting.utilities.*;

// Define CheckoutPOM class for interacting with the checkout page
public class CheckoutPOM {

    // WebDriver instance to control the browser
    private final WebDriver driver;
    // WebDriverWait instance to wait for elements to appear or become clickable
    private final WebDriverWait wait;
    // NavPOM instance for navigation actions (passed from BaseTests)
    private final NavPOM navPOM;

    // Flag to ensure billing details are filled only once
    private boolean billingFilled = false;

    // Locators for billing and checkout fields
    private final By firstNameField = By.id("billing_first_name");    // First name input field
    private final By lastNameField = By.id("billing_last_name");      // Last name input field
    private final By addressField = By.id("billing_address_1");       // Address input field
    private final By cityField = By.id("billing_city");               // City input field
    private final By postcodeField = By.id("billing_postcode");       // Postcode input field
    private final By emailField = By.id("billing_email");             // Email input field
    private final By phoneField = By.id("billing_phone");             // Phone input field
    private final By paymentCheck = By.id("payment_method_cheque");   // Checkbox for check payments
    private final By placeOrderButton = By.id("place_order");         // Button to place the order
    private final By orderNumberLocator = By.cssSelector(".order > strong"); // Order number element after placing order

    // Constructor for CheckoutPOM
    public CheckoutPOM(WebDriver driver, WebDriverWait wait, NavPOM navPOM) {
        this.driver = driver;   // Assign driver instance to class variable
        this.wait = wait;       // Assign wait instance to class variable
        this.navPOM = navPOM;   // Assign navPOM instance to class variable
    }

    // Method to fill billing details from configuration file
    public boolean fillBillingDetailsFromConfig() {
        // If billing details are already filled, skip
        if (billingFilled) return true;

        try {
            // Clear and fill first name from config
            driver.findElement(firstNameField).clear();
            driver.findElement(firstNameField).sendKeys(ConfigLoader.get("billing.firstname"));

            // Clear and fill last name from config
            driver.findElement(lastNameField).clear();
            driver.findElement(lastNameField).sendKeys(ConfigLoader.get("billing.lastname"));

            // Clear and fill address from config
            driver.findElement(addressField).clear();
            driver.findElement(addressField).sendKeys(ConfigLoader.get("billing.address"));

            // Clear and fill city from config
            driver.findElement(cityField).clear();
            driver.findElement(cityField).sendKeys(ConfigLoader.get("billing.city"));

            // Clear and fill postcode from config
            driver.findElement(postcodeField).clear();
            driver.findElement(postcodeField).sendKeys(ConfigLoader.get("billing.postcode"));

            // Clear and fill email from config
            driver.findElement(emailField).clear();
            driver.findElement(emailField).sendKeys(ConfigLoader.get("billing.email"));

            // Clear and fill phone from config
            driver.findElement(phoneField).clear();
            driver.findElement(phoneField).sendKeys(ConfigLoader.get("billing.phone"));

            // Mark billing as filled to avoid repeating
            billingFilled = true;
            return true;

        } catch (Exception e) {
            // If filling fails, print error message and take screenshot
            System.out.println("Failed to fill billing details: " + e.getMessage());
            Helpers.takeScreenshot(driver, "BillingError");
            return false;
        }
    }

    // Method to select "Check Payments" option
    public void selectCheckPayments() {
        try {
            // Wait until check payment checkbox is clickable
            WebElement checkPayment = wait.until(ExpectedConditions.elementToBeClickable(paymentCheck));
            // Click the checkbox if it is not already selected
            if (!checkPayment.isSelected()) checkPayment.click();
        } catch (Exception e) {
            // If selecting fails, print error message and take screenshot
            System.out.println("Selecting check payment failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "PaymentError");
        }
    }

    // Method to click the "Place Order" button
    public void placeOrder() {
        try {
            // Wait until place order button is clickable
            WebElement placeOrder = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
            // Click the button to place the order
            placeOrder.click();
        } catch (Exception e) {
            // If placing order fails, print error and take screenshot
            System.out.println("Placing order failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "PlaceOrderError");
        }
    }

    // Method to capture the order number after placing the order
    public String captureOrderNumber() {
        try {
            // Wait until order number element is visible
            WebElement orderNumberElem = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNumberLocator));
            // Get the text and trim whitespace
            String orderNumber = orderNumberElem.getText().trim();
            // Print order number to console
            System.out.println("Captured Order Number: " + orderNumber);
            return orderNumber; // Return captured order number
        } catch (Exception e) {
            // If capturing fails, print error and take screenshot
            System.out.println("Capturing order number failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "OrderNumberError");
            return ""; // Return empty string if failed
        }
    }
}
