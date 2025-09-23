package uk.co.twoitesting.pomclasses;
// Declares the package this class belongs to (Page Object Model classes for the project).

import org.openqa.selenium.*;
// Imports Selenium WebDriver core classes (WebDriver, WebElement, By, etc.).

import org.openqa.selenium.support.ui.ExpectedConditions;
// Imports ExpectedConditions to use with WebDriverWait (explicit waits).

import org.openqa.selenium.support.ui.WebDriverWait;
// Imports WebDriverWait to wait for elements before interacting with them.

import uk.co.twoitesting.utilities.ConfigLoader;
// Imports ConfigLoader to fetch billing details from config.properties.

import uk.co.twoitesting.utilities.Helpers;
// Imports Helpers utility class (e.g., for screenshots).

public class CheckoutPOM {
// Defines the CheckoutPOM class (Page Object Model for Checkout page).

    private final WebDriver driver;
    // Declares WebDriver instance for controlling the browser.

    private final WebDriverWait wait;
    // Declares WebDriverWait instance for handling explicit waits.

    // Locators for checkout page elements
    private final By firstNameField = By.id("billing_first_name");
    private final By lastNameField = By.id("billing_last_name");
    private final By addressField = By.id("billing_address_1");
    private final By cityField = By.id("billing_city");
    private final By postcodeField = By.id("billing_postcode");
    private final By emailField = By.id("billing_email");
    private final By phoneField = By.id("billing_phone");
    private final By paymentCheck = By.id("payment_method_cheque");
    private final By placeOrderButton = By.id("place_order");
    private final By orderNumberLocator = By.cssSelector(".order > strong");

    private boolean billingFilled = false;
    // Flag to prevent filling billing details multiple times.

    public CheckoutPOM(WebDriver driver, WebDriverWait wait) {
        // Constructor: initializes CheckoutPOM with WebDriver and WebDriverWait.

        this.driver = driver;
        // Assigns passed WebDriver instance to this class.

        this.wait = wait;
        // Assigns passed WebDriverWait instance to this class.
    }

    public boolean fillBillingDetailsFromConfig() {
        // Method to fill billing details into checkout form using config.properties.

        if (billingFilled) return true;
        // If already filled once, skip to avoid duplicates.

        try {
            // Try block to handle possible exceptions during form filling.

            driver.findElement(firstNameField).clear();
            // Clears the First Name input field.
            driver.findElement(firstNameField).sendKeys(ConfigLoader.get("billing.firstname"));
            // Enters First Name from config file.

            driver.findElement(lastNameField).clear();
            driver.findElement(lastNameField).sendKeys(ConfigLoader.get("billing.lastname"));
            // Enters Last Name from config file.

            driver.findElement(addressField).clear();
            driver.findElement(addressField).sendKeys(ConfigLoader.get("billing.address"));
            // Enters Billing Address from config file.

            driver.findElement(cityField).clear();
            driver.findElement(cityField).sendKeys(ConfigLoader.get("billing.city"));
            // Enters City from config file.

            driver.findElement(postcodeField).clear();
            driver.findElement(postcodeField).sendKeys(ConfigLoader.get("billing.postcode"));
            // Enters Postcode from config file.

            driver.findElement(emailField).clear();
            driver.findElement(emailField).sendKeys(ConfigLoader.get("billing.email"));
            // Enters Email from config file.

            driver.findElement(phoneField).clear();
            driver.findElement(phoneField).sendKeys(ConfigLoader.get("billing.phone"));
            // Enters Phone Number from config file.

            billingFilled = true;
            // Sets flag to true after filling details.

            return true;
            // Returns success.

        } catch (Exception e) {
            // Catches any exception during filling billing details.

            System.out.println("Failed to fill billing details: " + e.getMessage());
            // Logs error message.

            Helpers.takeScreenshot(driver, "BillingError");
            // Captures screenshot for debugging.

            return false;
            // Returns failure.
        }
    }

    public void selectCheckPayments() {
        // Method to select "Check Payments" option during checkout.

        try {
            WebElement checkPayment = wait.until(ExpectedConditions.elementToBeClickable(paymentCheck));
            // Waits until the "Check Payments" option is clickable, then finds it.

            if (!checkPayment.isSelected()) checkPayment.click();
            // Clicks the option if it’s not already selected.

        } catch (Exception e) {
            // Catches exceptions if the element isn’t found or clickable.

            System.out.println("Selecting check payment failed: " + e.getMessage());
            // Logs error.

            Helpers.takeScreenshot(driver, "PaymentError");
            // Captures screenshot for debugging.
        }
    }

    public void placeOrder() {
        // Method to click the "Place Order" button.

        try {
            WebElement placeOrder = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
            // Waits until "Place Order" button is clickable, then finds it.

            placeOrder.click();
            // Clicks the button.

        } catch (Exception e) {
            // Handles failure if button can’t be clicked.

            System.out.println("Placing order failed: " + e.getMessage());
            // Logs error.

            Helpers.takeScreenshot(driver, "PlaceOrderError");
            // Captures screenshot for debugging.
        }
    }

    public String captureOrderNumber() {
        // Method to get the order number after placing the order.

        try {
            WebElement orderNumberElem = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNumberLocator));
            // Waits until order number element is visible on page.

            String orderNumber = orderNumberElem.getText();
            // Gets the order number text.

            System.out.println("Order Number: " + orderNumber);
            // Logs order number.

            return orderNumber;
            // Returns order number.

        } catch (Exception e) {
            // Handles case where order number cannot be found.

            System.out.println("Capturing order number failed: " + e.getMessage());
            // Logs error.

            Helpers.takeScreenshot(driver, "OrderNumberError");
            // Captures screenshot for debugging.

            return "";
            // Returns empty string if capture fails.
        }
    }
}
