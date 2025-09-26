// Define the package where this test class belongs
package uk.co.twoitesting.test;

// Import JUnit annotations
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
// Import base test class for setup/teardown
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.twoitesting.basetests.BaseTests;
// Import Checkout page object
import uk.co.twoitesting.pomclasses.CheckoutPOM;
// Import helper utilities
import uk.co.twoitesting.utilities.ConfigLoader;
import uk.co.twoitesting.utilities.Helpers;

import java.time.Duration;
import java.util.List;

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

        navPOM.goToCart();
        cartPOM.removeProduct();

        //Add Polo Shirt to cart
        navPOM.goToShop();
        //shopPOM.openShop();               // Open shop page
        shopPOM.dismissPopupIfPresent();  // Close any popup if it appears
        shopPOM.addProductToCart("Polo");

        //shopPOM.addPoloToCart();          // Add Polo Shirt to cart
        //shopPOM.viewCart();               // View the cart
        navPOM.goToCart();
        Helpers.takeScreenshot(driver, "Cart Ready"); // Take screenshot of cart

        //Initialize checkout page object
        checkoutPOM = new CheckoutPOM(driver, wait, navPOM);

        //Go to the checkout page
        //driver.get(ConfigLoader.get("base.url") + "/checkout/");

        navPOM.goToCheckout();

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
        navPOM.goToMyAccount();
        // Assert the order appears in My Account -> Orders
        assertThat("Order " + orderNumber + " should appear in My Account -> Orders",
                checkoutPOM.isOrderPresentInMyOrders(orderNumber), is(true));

        // Empty the cart before logging out
        emptyCart();
        Helpers.takeScreenshot(driver, "Cart Emptied");

        //Log out from account
        accountPOM.logout();
        Helpers.takeScreenshot(driver, "Logged Out"); // Take screenshot after logout
    }

}
