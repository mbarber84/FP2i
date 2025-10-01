// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Selenium WebDriver classes for interacting with web elements
import org.openqa.selenium.*;
// Import Selenium support classes for explicit waits
import org.openqa.selenium.support.ui.*;
// Import helper utilities for reusable functions like price extraction
import uk.co.twoitesting.utilities.Helpers;

// Import BigDecimal for precise financial calculations
import java.math.BigDecimal;

// Define CartPOM class for actions you can perform in the shopping cart
public class CartPOM {

    // WebDriver instance to control the browser
    private final WebDriver driver;
    // WebDriverWait instance to wait for elements to appear or become clickable
    private final WebDriverWait wait;

    // Locators for different elements in the cart
    private final By couponBox = By.id("coupon_code"); // Input field to type a coupon code
    private final By applyCouponButton = By.cssSelector("button.button[name='apply_coupon']"); // Button to apply the coupon
    private final By subtotalLocator = By.cssSelector(".cart-subtotal td span.woocommerce-Price-amount"); // Subtotal price element
    private final By discountLocator = By.cssSelector(".cart-discount td span.woocommerce-Price-amount"); // Discount price element
    private final By shippingLocator = By.cssSelector("tr.shipping td span.woocommerce-Price-amount"); // Shipping cost element
    private final By totalLocator = By.cssSelector(".order-total td strong span.woocommerce-Price-amount"); // Total price element

    // Constructor to initialize CartPOM with WebDriver and WebDriverWait
    public CartPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; // Assign the driver instance to the class variable
        this.wait = wait;     // Assign the wait instance to the class variable
    }

    // Method to apply a coupon and verify that discount and total are updated
    public void applyCoupon(String couponCode) {
        try {
            // Try to find the "Show Coupon" link if present
            WebElement showCoupon = driver.findElement(By.cssSelector(".showcoupon"));
            // Check if the "Show Coupon" link is visible
            if (showCoupon.isDisplayed()) {
                showCoupon.click(); // Click to reveal the coupon input field
                wait.until(ExpectedConditions.visibilityOfElementLocated(couponBox)); // Wait until coupon box is visible
            }
        } catch (NoSuchElementException ignored) {
            // Ignore if "Show Coupon" link is not found
        }

        // Wait until the coupon input box is visible
        WebElement coupon = wait.until(ExpectedConditions.visibilityOfElementLocated(couponBox));
        coupon.clear();          // Clear any pre-filled text in the coupon box
        coupon.sendKeys(couponCode); // Enter the coupon code

        // Wait until the Apply Coupon button is clickable
        WebElement applyBtn = wait.until(ExpectedConditions.elementToBeClickable(applyCouponButton));
        applyBtn.click();        // Click the Apply Coupon button

        // Wait until the discount element becomes visible (coupon applied)
        wait.until(ExpectedConditions.visibilityOfElementLocated(discountLocator));
    }

    // Helper method to fetch the subtotal from the cart as BigDecimal
    public BigDecimal getSubtotalBD() {
        return Helpers.extractPrice(driver.findElement(subtotalLocator).getText()); // Extract price text and convert to BigDecimal
    }

    // Helper method to fetch the discount from the cart as BigDecimal
    public BigDecimal getDiscountBD() {
        return Helpers.extractPrice(driver.findElement(discountLocator).getText());
    }

    // Helper method to fetch the shipping cost from the cart as BigDecimal
    public BigDecimal getShippingBD() {
        return Helpers.extractPrice(driver.findElement(shippingLocator).getText());
    }

    // Helper method to fetch the total from the cart as BigDecimal
    public BigDecimal getTotalBD() {
        return Helpers.extractPrice(driver.findElement(totalLocator).getText());
    }

    // Method to remove a specific coupon from the cart
    public void removeCoupon(String couponName) {
        try {
            // Build a CSS selector for the specific coupon row based on its name
            By couponLocator = By.cssSelector("tr.cart-discount.coupon-" + couponName.toLowerCase());
            // Find the coupon row element
            WebElement couponRow = driver.findElement(couponLocator);
            // Click the "remove" link inside that coupon row
            couponRow.findElement(By.cssSelector("td a")).click();

            // Wait until the coupon row is no longer visible (removed)
            wait.until(ExpectedConditions.invisibilityOfElementLocated(couponLocator));

            // Print a success message to the console
            System.out.println("Coupon removed: " + couponName);
        } catch (Exception e) {
            // If coupon is not found, print a message
            System.out.println("No coupon found: " + couponName);
        }
    }

    // Method to remove any product from the cart
    public void removeProduct() {
        try {
            // Locator for any product row in the cart
            By productLocator = By.cssSelector("tr.cart_item");
            // Find the remove button for the product
            WebElement removeBtn = driver.findElement(By.cssSelector("tr.cart_item td.product-remove a"));
            removeBtn.click(); // Click the remove button

            // Wait until the product row disappears from the cart
            wait.until(ExpectedConditions.invisibilityOfElementLocated(productLocator));

            // Print a success message
            System.out.println("Product removed from cart.");
        } catch (Exception e) {
            // If no product is found, print a message
            System.out.println("No product found in cart.");
        }
    }
}
