package uk.co.twoitesting.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import uk.co.twoitesting.basetests.BaseTests;
import uk.co.twoitesting.pomclasses.*;
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
import uk.co.twoitesting.utilities.Helpers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestCase2 extends BaseTests {

    @Test
    @Tag("RunMe")
    void testCompletePurchase() {

        // Login
        LoginPOM loginPOM = new LoginPOM(driver, wait, null);
        loginPOM.open();
        loginPOM.login();
        Helpers.takeScreenshot(driver, "Login Success");
        Assertions.assertTrue(driver.getPageSource().contains("Logout"), "User should be logged in after login");

        NavPOM navPOM = new NavPOM(driver, wait);
        CartPOM cartPOM = new CartPOM(driver, wait);
        ShopPOM shopPOM = new ShopPOM(driver, wait);

        navPOM.goToCart();
        cartPOM.removeProduct();

        // Add product to cart
        navPOM.goToShop();
        shopPOM.dismissPopupIfPresent();
        shopPOM.addProductToCart("Polo");
        navPOM.goToCart();
        Helpers.takeScreenshot(driver, "Cart Ready");

        // Checkout
        CheckoutPOM checkoutPOM = new CheckoutPOM(driver, wait, navPOM);
        navPOM.goToCheckout();
        checkoutPOM.fillBillingDetailsFromConfig();
        Helpers.takeScreenshot(driver, "Billing Details Entered");
        checkoutPOM.selectCheckPayments();
        checkoutPOM.placeOrder();

        // Capture order number
        String orderNumber = checkoutPOM.captureOrderNumber();

        // Verify order in OrdersPOM
        OrdersPOM ordersPOM = new OrdersPOM(driver, wait, navPOM);
        assertThat("Order " + orderNumber + " should appear in My Account -> Orders",
                ordersPOM.isOrderPresent(orderNumber), is(true));

        // Cleanup
        cartPOM.removeProduct();
        Helpers.takeScreenshot(driver, "Cart Emptied");

        AccountPOM accountPOM = new AccountPOM(driver, wait, navPOM);
        accountPOM.logout();
        Helpers.takeScreenshot(driver, "Logged Out");
    }
}
