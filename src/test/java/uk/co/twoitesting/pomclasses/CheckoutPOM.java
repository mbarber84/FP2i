package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
import uk.co.twoitesting.utilities.*;


public class CheckoutPOM {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final NavPOM navPOM;

    private boolean billingFilled = false;

    // Checkout fields
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

    public CheckoutPOM(WebDriver driver, WebDriverWait wait, NavPOM navPOM) {
        this.driver = driver;
        this.wait = wait;
        this.navPOM = navPOM;
    }

    // Fill billing details from config
    public boolean fillBillingDetailsFromConfig() {
        if (billingFilled) return true;

        try {
            driver.findElement(firstNameField).clear();
            driver.findElement(firstNameField).sendKeys(ConfigLoader.get("billing.firstname"));

            driver.findElement(lastNameField).clear();
            driver.findElement(lastNameField).sendKeys(ConfigLoader.get("billing.lastname"));

            driver.findElement(addressField).clear();
            driver.findElement(addressField).sendKeys(ConfigLoader.get("billing.address"));

            driver.findElement(cityField).clear();
            driver.findElement(cityField).sendKeys(ConfigLoader.get("billing.city"));

            driver.findElement(postcodeField).clear();
            driver.findElement(postcodeField).sendKeys(ConfigLoader.get("billing.postcode"));

            driver.findElement(emailField).clear();
            driver.findElement(emailField).sendKeys(ConfigLoader.get("billing.email"));

            driver.findElement(phoneField).clear();
            driver.findElement(phoneField).sendKeys(ConfigLoader.get("billing.phone"));

            billingFilled = true;
            return true;

        } catch (Exception e) {
            System.out.println("Failed to fill billing details: " + e.getMessage());
            Helpers.takeScreenshot(driver, "BillingError");
            return false;
        }
    }

    // Select check payments
    public void selectCheckPayments() {
        try {
            WebElement checkPayment = wait.until(ExpectedConditions.elementToBeClickable(paymentCheck));
            if (!checkPayment.isSelected()) checkPayment.click();
        } catch (Exception e) {
            System.out.println("Selecting check payment failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "PaymentError");
        }
    }

    // Place the order
    public void placeOrder() {
        try {
            WebElement placeOrder = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
            placeOrder.click();
        } catch (Exception e) {
            System.out.println("Placing order failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "PlaceOrderError");
        }
    }

    // Capture the order number after placing order
    public String captureOrderNumber() {
        try {
            WebElement orderNumberElem = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNumberLocator));
            String orderNumber = orderNumberElem.getText().trim();
            System.out.println("Captured Order Number: " + orderNumber);
            return orderNumber;
        } catch (Exception e) {
            System.out.println("Capturing order number failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "OrderNumberError");
            return "";
        }
    }
}
