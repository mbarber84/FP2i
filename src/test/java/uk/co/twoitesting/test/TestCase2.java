// Define the package where this test class belongs
package uk.co.twoitesting.test;

// Import JUnit assertion and test annotations
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
// Import base test class containing setup and teardown
import uk.co.twoitesting.basetests.BaseTests;
// Import POM classes for page interactions
import uk.co.twoitesting.pomclasses.*;
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
// Import helper utilities for screenshots and other actions
import uk.co.twoitesting.utilities.Helpers;

// Import Hamcrest assertion for readable assertion syntax
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// Define the TestCase2 class, extending BaseTests for setup/teardown
public class TestCase2 extends BaseTests {

    // Annotate method as a JUnit test and assign a tag
    @Test
    @Tag("RunMe")
    void testCompletePurchase() {

        //LOGIN
        // Initialize LoginPOM with driver and wait (NavPOM is not needed here)
        LoginPOM loginPOM = new LoginPOM(driver, wait, null);
        loginPOM.open(); // Open the login page
        loginPOM.login(); // Perform login
        Helpers.takeScreenshot(driver, "Login Success"); // Take screenshot after login

        // Assert that "Logout" appears in page source to confirm login success
        Assertions.assertTrue(driver.getPageSource().contains("Logout"),
                "User should be logged in after login");

        // Initialize POM objects for navigation, cart, and shop
        NavPOM navPOM = new NavPOM(driver, wait);
        CartPOM cartPOM = new CartPOM(driver, wait);
        ShopPOM shopPOM = new ShopPOM(driver, wait);

        //CLEAN CART
        navPOM.goToCart(); // Navigate to the cart page
        cartPOM.removeProduct(); // Remove any existing products from cart

        //ADD PRODUCT
        navPOM.goToShop(); // Navigate to the Shop page
        shopPOM.dismissPopupIfPresent(); // Dismiss any popup if present
        shopPOM.addProductToCart("Polo"); // Add "Polo" product to cart
        navPOM.goToCart(); // Return to cart page
        Helpers.takeScreenshot(driver, "Cart Ready"); // Take screenshot of cart ready for checkout

        //CHECKOUT
        CheckoutPOM checkoutPOM = new CheckoutPOM(driver, wait, navPOM); // Initialize CheckoutPOM
        navPOM.goToCheckout(); // Navigate to checkout page
        checkoutPOM.fillBillingDetailsFromConfig(); // Fill billing details from config
        Helpers.takeScreenshot(driver, "Billing Details Entered"); // Screenshot billing details
        checkoutPOM.selectCheckPayments(); // Select "Check Payments" option
        checkoutPOM.placeOrder(); // Place the order

        //CAPTURE ORDER NUMBER
        String orderNumber = checkoutPOM.captureOrderNumber(); // Capture order number for verification

        //VERIFY ORDER
        OrdersPOM ordersPOM = new OrdersPOM(driver, wait, navPOM); // Initialize OrdersPOM
        // Assert that the order number appears in "My Account -> Orders"
        assertThat("Order " + orderNumber + " should appear in My Account -> Orders",
                ordersPOM.isOrderPresent(orderNumber), is(true));

        //CLEANUP
        cartPOM.removeProduct(); // Remove product from cart
        Helpers.takeScreenshot(driver, "Cart Emptied"); // Screenshot cart emptied

        AccountPOM accountPOM = new AccountPOM(driver, wait, navPOM); // Initialize AccountPOM
        accountPOM.logout(); // Log out from account
        Helpers.takeScreenshot(driver, "Logged Out"); // Screenshot after logout
    }
}
