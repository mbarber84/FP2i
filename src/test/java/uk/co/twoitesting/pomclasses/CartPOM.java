// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Selenium classes for web elements and waits
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
// Import helper utilities
import uk.co.twoitesting.utilities.Helpers;

// Import assertion classes from Hamcrest for validating values
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

// Define CartPOM class for actions you can do in the shopping cart
public class CartPOM {

    // Store the browser driver to control the browser
    private final WebDriver driver;
    // Store WebDriverWait to wait for elements to appear
    private final WebDriverWait wait;

    // Locators for different elements in the cart
    private final By couponBox = By.id("coupon_code"); // Where you type a coupon
    private final By applyCouponButton = By.cssSelector("button.button[name='apply_coupon']"); // Button to apply coupon
    private final By subtotalLocator = By.cssSelector(".cart-subtotal td span.woocommerce-Price-amount"); // Subtotal amount
    private final By discountLocator = By.cssSelector(".cart-discount td span.woocommerce-Price-amount"); // Discount amount
    private final By shippingLocator = By.cssSelector("tr.shipping td span.woocommerce-Price-amount"); // Shipping cost
    private final By totalLocator = By.cssSelector(".order-total td strong span.woocommerce-Price-amount"); // Total cost

    // Constructor to set up CartPOM with browser driver and wait
    public CartPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; // Save driver
        this.wait = wait;     // Save wait
    }

    // Method to apply a coupon and check that discount and total are correct
    public void applyCoupon(String couponCode) {
        try {
            WebElement showCoupon = driver.findElement(By.cssSelector(".showcoupon"));
            if (showCoupon.isDisplayed()) {
                showCoupon.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(couponBox));
            }
        } catch (NoSuchElementException ignored) { }

        WebElement coupon = wait.until(ExpectedConditions.visibilityOfElementLocated(couponBox));
        coupon.clear();
        coupon.sendKeys(couponCode);

        WebElement applyBtn = wait.until(ExpectedConditions.elementToBeClickable(applyCouponButton));
        applyBtn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(discountLocator));
    }

    // Helper methods to fetch cart values
    public BigDecimal getSubtotalBD() {
        return Helpers.extractPrice(driver.findElement(subtotalLocator).getText());
    }

    public BigDecimal getDiscountBD() {
        return Helpers.extractPrice(driver.findElement(discountLocator).getText());
    }

    public BigDecimal getShippingBD() {
        return Helpers.extractPrice(driver.findElement(shippingLocator).getText());
    }

    public BigDecimal getTotalBD() {
        return Helpers.extractPrice(driver.findElement(totalLocator).getText());
    }

    // Method to remove a specific coupon from the cart
    public void removeCoupon(String couponName) {
        try {
            // Find the coupon row by its name
            By couponLocator = By.cssSelector("tr.cart-discount.coupon-" + couponName.toLowerCase());
            WebElement couponRow = driver.findElement(couponLocator);
            // Click the "remove" link for that coupon
            couponRow.findElement(By.cssSelector("td a")).click();

            // Wait until the coupon disappears from the page
            wait.until(ExpectedConditions.invisibilityOfElementLocated(couponLocator));

            System.out.println("Coupon removed: " + couponName);
        } catch (Exception e) {
            // If coupon is not found, print message
            System.out.println("No coupon found: " + couponName);
        }
    }

    // Method to remove a product from the cart
    public void removeProduct() {
        try {
            // Locator for any product in the cart
            By productLocator = By.cssSelector("tr.cart_item");
            // Click the "remove" link for the product
            WebElement removeBtn = driver.findElement(By.cssSelector("tr.cart_item td.product-remove a"));
            removeBtn.click();

            // Wait until the product disappears from the cart
            wait.until(ExpectedConditions.invisibilityOfElementLocated(productLocator));

            System.out.println("Product removed from cart.");
        } catch (Exception e) {
            // If no product is found, print message
            System.out.println("No product found in cart.");
        }
    }


}
