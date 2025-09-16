// Define the package where this test class belongs
package uk.co.twoitesting.test;

// Import JUnit annotations
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
// Import base test class for setup/teardown
import uk.co.twoitesting.basetests.BaseTests;
// Import Checkout page object
import uk.co.twoitesting.pomclasses.CheckoutPOM;
// Import helper utilities
import uk.co.twoitesting.utilities.ConfigLoader;
import uk.co.twoitesting.utilities.Helpers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// Define the test class which extends BaseTests
public class TestCase2 extends BaseTests {

    // Declare CheckoutPOM object for interacting with checkout page
    private CheckoutPOM checkoutPOM;

    // Define a test method
    @Test
    @Tag("RunMe") // JUnit tag to filter tests
    void testCompletePurchase() {

        //Login to the website
        loginPOM.open(); // Open login page
        loginPOM.login(); // Enter credentials and log in
        Helpers.takeScreenshot(driver, "Login Success"); // Take screenshot after login

        // Assert user is logged in
        Assertions.assertTrue(driver.getPageSource().contains("Logout"), "User should be logged in after login");

        //Add Polo Shirt to cart
        shopPOM.openShop();               // Open shop page
        shopPOM.dismissPopupIfPresent();  // Close any popup if it appears
        shopPOM.addPoloToCart();          // Add Polo Shirt to cart
        shopPOM.viewCart();               // View the cart
        Helpers.takeScreenshot(driver, "Cart Ready"); // Take screenshot of cart

        //Initialize checkout page object
        checkoutPOM = new CheckoutPOM(driver, wait);

        //Go to the checkout page
        driver.get(ConfigLoader.get("base.url") + "/checkout/");

        //Fill in billing details from config.properties
        checkoutPOM.fillBillingDetailsFromConfig();
        Helpers.takeScreenshot(driver, "Billing Details Entered"); // Take screenshot

        //Select check payment method
        checkoutPOM.selectCheckPayments();

        //Place the order
        checkoutPOM.placeOrder();

        //Capture the order number for validation
        String orderNumber = checkoutPOM.captureOrderNumber();

        //Go to My Account -> Orders to check if order appears
        driver.get("https://www.edgewordstraining.co.uk/demo-site/my-account/orders/");
        boolean orderFound = driver.getPageSource().contains(orderNumber); // Check if page source contains order number
        assertThat("Order " + orderNumber + " should appear in My Account -> Orders", orderFound, is(true));

        //Log out from account
        accountPOM.logout();
        Helpers.takeScreenshot(driver, "Logged Out"); // Take screenshot after logout
    }
}
