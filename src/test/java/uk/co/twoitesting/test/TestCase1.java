// Define the package where this test class belongs
package uk.co.twoitesting.test;

// Import Allure annotations for reporting test steps, features, and stories
import io.qameta.allure.*;
// Import JUnit assertions
import org.junit.jupiter.api.Assertions;
// Import JUnit Tag annotation
import org.junit.jupiter.api.Tag;
// Import JUnit 5 parameterized test support
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
// Import Selenium By class to locate elements
import org.openqa.selenium.By;
// Import base test class containing setup and teardown
import uk.co.twoitesting.basetests.BaseTests;
// Import POM classes for page interactions
import uk.co.twoitesting.pomclasses.*;
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
// Import helper utilities
import uk.co.twoitesting.utilities.*;

// Import Java utility classes
import java.util.List;
import java.util.stream.Stream;
// Import BigDecimal for precise calculations
import java.math.BigDecimal;
import java.math.RoundingMode;

// Define the TestCase1 class which extends BaseTests
public class TestCase1 extends BaseTests {

    // Data provider method to supply test data for parameterized test
    static Stream<TestData> dataProvider() {
        List<String> products = List.of("Polo"); // List of products to test
        // Load coupon data from a CSV file
        List<CouponData> coupons =
                CsvCouponLoader.loadCoupons("src/test/resources/coupons.csv");

        // Combine products and coupons to generate test data
        return coupons.stream().flatMap(coupon ->
                products.stream().map(product -> new TestData(coupon, product))
        );
    }

    // Annotate with Allure metadata for reporting
    @Epic("Shop Tests")
    @Feature("Cart and Discount")
    @Story("Purchase Items with Discounts")
    @Tag("RunMe") // JUnit tag to selectively run tests
    @ParameterizedTest(name = "Test {0}") // Parameterized test with custom name
    @MethodSource("dataProvider") // Use the dataProvider method for test data
    void testPurchaseWithDiscount(TestData data) {

        // Allure step: login to the site
        Allure.step("Login to site", () -> {
            // Initialize LoginPOM with driver, wait, and navPOM
            LoginPOM loginPOM = new LoginPOM(driver, wait, navPOM);
            loginPOM.open(); // Open login page
            loginPOM.login(); // Perform login
            Helpers.takeScreenshot(driver, "Login Success"); // Take screenshot after login

            // Assert that the page contains "Logout" to verify login success
            Assertions.assertTrue(driver.getPageSource().contains("Logout"),
                    "User should be logged in after login");

            // Initialize page objects for navigation and cart
            NavPOM navPOM = new NavPOM(driver, wait);
            CartPOM cartPOM = new CartPOM(driver, wait);
            navPOM.goToCart(); // Navigate to cart
            cartPOM.removeCoupon(data.coupon.code()); // Remove any existing coupon
            cartPOM.removeProduct(); // Remove any existing products
        });

        // Allure step: add product to cart and apply discount
        Allure.step("Add " + data.product + " and apply discount " + data.coupon.key(), () -> {
            // Initialize page objects
            NavPOM navPOM = new NavPOM(driver, wait);
            ShopPOM shopPOM = new ShopPOM(driver, wait);
            CartPOM cartPOM = new CartPOM(driver, wait);

            navPOM.goToShop(); // Navigate to Shop page
            shopPOM.dismissPopupIfPresent(); // Dismiss popup if it exists
            shopPOM.addProductToCart(data.product); // Add product to cart
            navPOM.goToCart(); // Go to the cart page

            // Take screenshot before applying coupon
            Helpers.takeScreenshot(driver,
                    "Cart Before Applying " + data.coupon.key() + " for " + data.product);

            // Print test info
            System.out.println("Testing " + data.product +
                    " with discount: " + data.coupon.code() +
                    " (" + (data.coupon.discount() * 100) + "%)");

            cartPOM.applyCoupon(data.coupon.code()); // Apply the coupon

            // Fetch cart amounts using CartPOM
            BigDecimal subtotal = cartPOM.getSubtotalBD();
            BigDecimal discount = cartPOM.getDiscountBD();
            BigDecimal shipping = cartPOM.getShippingBD();
            BigDecimal total = cartPOM.getTotalBD();

            // Calculate expected discount and total
            BigDecimal discountRate = BigDecimal.valueOf(data.coupon.discount());
            BigDecimal expectedDiscount = subtotal.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal expectedTotal = subtotal.subtract(expectedDiscount).add(shipping).setScale(2, RoundingMode.HALF_UP);

            // Assert that discount and total are correct
            Assertions.assertEquals(0, expectedDiscount.compareTo(discount), "Discount should match expected");
            Assertions.assertEquals(0, expectedTotal.compareTo(total), "Total should match expected");

            // Print detailed cart amounts to console
            System.out.printf("Subtotal: £%.2f | Discount: £%.2f (Expected: £%.2f) | Total: £%.2f (Expected: £%.2f)%n",
                    subtotal, discount, expectedDiscount, total, expectedTotal);

            // Clean up cart by removing coupon and product
            cartPOM.removeCoupon(data.coupon.code());
            cartPOM.removeProduct();

            // Verify cart is empty
            int cartItems = driver.findElements(By.cssSelector("tr.cart_item")).size();
            Assertions.assertEquals(0, cartItems, "Cart should be empty after removing product");

            AccountPOM accountPOM = new AccountPOM(driver, wait, navPOM); // Initialize AccountPOM
            accountPOM.logout(); // Log out from account
            Helpers.takeScreenshot(driver, "Logged Out"); // Screenshot after logout
        });
    }

    // Record class to hold test data (coupon + product)
    record TestData(CouponData coupon, String product) {
        @Override
        public String toString() {
            return product + " with " + coupon.key(); // String representation for parameterized test name
        }
    }
}
