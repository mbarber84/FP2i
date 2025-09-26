// Put this test in the "test" package
package uk.co.twoitesting.test;

// Import Allure annotations (for nice test reports)
import io.qameta.allure.*;
// Import JUnit test tools
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
// Import base test class (gives driver, wait, setup, teardown)
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import uk.co.twoitesting.basetests.BaseTests;
// Import helper classes
import uk.co.twoitesting.utilities.ConfigLoader;
import uk.co.twoitesting.utilities.Helpers;
import java.util.List;
import java.util.stream.Stream;
import java.math.BigDecimal;
import java.math.RoundingMode;


// Create a test class that extends BaseTests
public class TestCase1 extends BaseTests {

    static Stream<TestData> dataProvider() {
        List<String> products = List.of("Polo"); // "Belt", "Cap", "Hoodie with Logo", "Hoodie", "Beanie" add as many as you like


        return Stream.of(
                new CouponData("coupon.edgewords",
                        ConfigLoader.get("coupon.edgewords"),
                        Double.parseDouble(ConfigLoader.get("coupon.edgewords.discount"))),
                new CouponData("coupon.2idiscount",
                        ConfigLoader.get("coupon.2idiscount"),
                        Double.parseDouble(ConfigLoader.get("coupon.2idiscount.discount")))
        ).flatMap(coupon ->
                products.stream().map(product -> new TestData(coupon, product))
        );
    }

    @Epic("Shop Tests")
    @Feature("Cart and Discount")
    @Story("Purchase Items with Discounts")
    @Tag("RunMe")
    @ParameterizedTest(name = "Test {0}")
    @MethodSource("dataProvider")
    void testPurchaseWithDiscount(TestData data) {

        // Login once per test run
        Allure.step("Login to site", () -> {
            loginPOM.open();
            loginPOM.login();
            Helpers.takeScreenshot(driver, "Login Success");
            Assertions.assertTrue(driver.getPageSource().contains("Logout"),
                    "User should be logged in after login");
            navPOM.goToCart();
            cartPOM.removeCoupon(data.coupon.code());
            cartPOM.removeProduct();
        });

        Allure.step("Add " + data.product + " and apply discount " + data.coupon.key(), () -> {
            navPOM.goToShop();
            shopPOM.dismissPopupIfPresent();
            shopPOM.addProductToCart(data.product);   // dynamic product
            navPOM.goToCart();

            Helpers.takeScreenshot(driver,
                    "Cart Before Applying " + data.coupon.key() + " for " + data.product);

            System.out.println("Testing " + data.product +
                    " with discount: " + data.coupon.code() +
                    " (" + (data.coupon.discount() * 100) + "%)");

            // Apply coupon (no assertions here)
            cartPOM.applyCoupon(data.coupon.code());

            // Fetch values and assert
            BigDecimal subtotal = cartPOM.getSubtotalBD();
            BigDecimal discount = cartPOM.getDiscountBD();
            BigDecimal shipping = cartPOM.getShippingBD();
            BigDecimal total = cartPOM.getTotalBD();

            BigDecimal discountRate = BigDecimal.valueOf(data.coupon.discount());
            BigDecimal expectedDiscount = subtotal.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal expectedTotal = subtotal.subtract(expectedDiscount).add(shipping).setScale(2, RoundingMode.HALF_UP);

            Assertions.assertEquals(0, expectedDiscount.compareTo(discount), "Discount should match expected");
            Assertions.assertEquals(0, expectedTotal.compareTo(total), "Total should match expected");

            BigDecimal actualDiscount = cartPOM.getDiscountBD(); // POM returns BigDecimal
            BigDecimal actualTotal = cartPOM.getTotalBD();

            System.out.printf("Subtotal: £%.2f | Discount: £%.2f (Expected: £%.2f) | Total: £%.2f (Expected: £%.2f)%n",
                    subtotal, discount, expectedDiscount, total, expectedTotal);



            // Remove coupon & product
            cartPOM.removeCoupon(data.coupon.code());
            cartPOM.removeProduct();

            // Verify cart empty
            int cartItems = driver.findElements(By.cssSelector("tr.cart_item")).size();
            Assertions.assertEquals(0, cartItems, "Cart should be empty after removing product");
        });
    }

    // Records for clean data handling
    record CouponData(String key, String code, double discount) {}
    record TestData(CouponData coupon, String product) {
        @Override
        public String toString() {
            return product + " with " + coupon.key();
        }
    }
}





