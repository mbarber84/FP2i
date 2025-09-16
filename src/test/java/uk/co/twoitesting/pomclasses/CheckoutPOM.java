package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.twoitesting.utilities.ConfigLoader;
import uk.co.twoitesting.utilities.Helpers;

public class CheckoutPOM {

    private WebDriver driver;
    private WebDriverWait wait;

    private By firstNameField = By.id("billing_first_name");
    private By lastNameField = By.id("billing_last_name");
    private By addressField = By.id("billing_address_1");
    private By cityField = By.id("billing_city");
    private By postcodeField = By.id("billing_postcode");
    private By emailField = By.id("billing_email");
    private By phoneField = By.id("billing_phone");
    private By paymentCheck = By.id("payment_method_cheque");
    private By placeOrderButton = By.id("place_order");
    private By orderNumberLocator = By.cssSelector(".order > strong");

    private boolean billingFilled = false;

    public CheckoutPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

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

    public void selectCheckPayments() {
        try {
            WebElement checkPayment = wait.until(ExpectedConditions.elementToBeClickable(paymentCheck));
            if (!checkPayment.isSelected()) checkPayment.click();
        } catch (Exception e) {
            System.out.println("Selecting check payment failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "PaymentError");
        }
    }

    public void placeOrder() {
        try {
            WebElement placeOrder = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
            placeOrder.click();
        } catch (Exception e) {
            System.out.println("Placing order failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "PlaceOrderError");
        }
    }

    public String captureOrderNumber() {
        try {
            WebElement orderNumberElem = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNumberLocator));
            String orderNumber = orderNumberElem.getText();
            System.out.println("Order Number: " + orderNumber);
            return orderNumber;
        } catch (Exception e) {
            System.out.println("Capturing order number failed: " + e.getMessage());
            Helpers.takeScreenshot(driver, "OrderNumberError");
            return "";
        }
    }
}
