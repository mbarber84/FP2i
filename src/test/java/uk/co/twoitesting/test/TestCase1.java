// Put this test in the "test" package
package uk.co.twoitesting.test;

// Import Allure annotations (for nice test reports)
import io.qameta.allure.*;
// Import JUnit test tools
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
// Import base test class (gives driver, wait, setup, teardown)
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import uk.co.twoitesting.basetests.BaseTests;
// Import helper classes
import uk.co.twoitesting.utilities.ConfigLoader;
import uk.co.twoitesting.utilities.Helpers;

import java.util.Map;
import java.util.stream.Stream;

// Create a test class that extends BaseTests
public class TestCase1 extends BaseTests {

    static Stream<CouponData> couponProvider() {
        return Stream.of(
                new CouponData("coupon.edgewords",
                        ConfigLoader.get("coupon.edgewords"),
                        Double.parseDouble(ConfigLoader.get("coupon.edgewords.discount"))),
                new CouponData("coupon.2idiscount",
                        ConfigLoader.get("coupon.2idiscount"),
                        Double.parseDouble(ConfigLoader.get("coupon.2idiscount.discount")))
        );
    }
    // Define one test method
    //@Test
    @Epic("Shop Tests")      // Big category for reporting
    @Feature("Cart and Discount") // Smaller feature area
    @Story("Purchase Polo Shirts with Discounts") // Specific user story
    @Tag("RunMe")
    @ParameterizedTest(name = "Test {0}")   // will display coupon key in test name
    @MethodSource("couponProvider")// Lets us filter this test by tag
    void testPurchaseWith2iDiscount(CouponData coupon) {

        //Login
        Allure.step("Login to site", () -> {
            loginPOM.open();  // Open the login page
            loginPOM.login(); // Type username & password and click login
            Helpers.takeScreenshot(driver, "Login Success"); // Take a screenshot for proof

            // Check if page contains "Logout" to confirm login worked
            Assertions.assertTrue(driver.getPageSource().contains("Logout"), "User should be logged in after login");
        });

            Allure.step("Add Polo Shirt and apply discount: " + coupon.key(), () -> {
                navPOM.goToShop();
                shopPOM.dismissPopupIfPresent();
                shopPOM.addProductToCart("Belt");
                navPOM.goToCart();

                Helpers.takeScreenshot(driver, "Cart Before Applying " + coupon.key());

                System.out.println("Testing Discount: " + coupon.code() + " (" + (coupon.discount() * 100) + "%)");

                // Apply and validate coupon
                cartPOM.applyCouponAndValidate(coupon.code(), coupon.discount());

                // Remove coupon & product
                cartPOM.removeCoupon(coupon.code());
                cartPOM.removeProduct();

                // Assert cart is empty
                int cartItems = driver.findElements(By.cssSelector("tr.cart_item")).size();
                Assertions.assertEquals(0, cartItems, "Cart should be empty after removing product");
            });
        };

        // 🔹 Record for coupon data (Java 16+)
        record CouponData(String key, String code, double discount) { }
        }


//        //First coupon test
//        Allure.step("Add item and apply first discount", () -> {
//            navPOM.goToShop();
//            //shopPOM.openShop();              // Go to the shop page
//            shopPOM.dismissPopupIfPresent();// Close popup if it appears
//            shopPOM.addProductToCart("Polo"); //add any product name here !!! NEW CODE
//            //shopPOM.addPoloToCart();         // Add Polo Shirt to cart
//           // shopPOM.viewCart();              // Go to cart page
//            navPOM.goToCart();
//
//            Helpers.takeScreenshot(driver, "Cart With Polo"); // Save screenshot of cart
//
//            // Get coupon code & discount value from config file
//            String coupon1 = ConfigLoader.get("coupon.edgewords");
//            double discount1 = Double.parseDouble(ConfigLoader.get("coupon.edgewords.discount"));
//
//            // Print message in console (for debugging/info)
//            System.out.println("Testing Discount: " + coupon1 + " (" + (discount1 * 100) + "%)");
//
//            // Apply coupon in the cart and check discount is correct
//            cartPOM.applyCouponAndValidate(coupon1, discount1);
//
//            // Remove coupon & remove product from cart
//            cartPOM.removeCoupon(coupon1);
//            cartPOM.removeProduct();
//
//            // Count items left in cart (should be 0 after removal)
//            int cartItems = driver.findElements(By.cssSelector("tr.cart_item")).size();
//            Assertions.assertEquals(0, cartItems, "Cart should be empty after removing product");
//        });
//
//        //Second coupon test
//        Allure.step("Add Polo Shirt again and apply second discount", () -> {
//            navPOM.goToShop();
//            //shopPOM.openShop();   // Go back to shop
//
//            shopPOM.addProductToCart("Polo"); // add any item name NEW CODE
//            navPOM.goToCart();
//            //shopPOM.addPoloToCart(); // Add Polo Shirt again
//            //shopPOM.viewCart();      // Open cart
//
//            Helpers.takeScreenshot(driver, "Cart With Polo Again"); // Screenshot again
//
//            // Get second coupon & discount from config file
//            String coupon2 = ConfigLoader.get("coupon.2idiscount");
//            double discount2 = Double.parseDouble(ConfigLoader.get("coupon.2idiscount.discount"));
//
//            // Print which coupon is being tested
//            System.out.println("Testing Discount: " + coupon2 + " (" + (discount2 * 100) + "%)");
//
//            // Apply coupon and check discount works
//            cartPOM.applyCouponAndValidate(coupon2, discount2);
//
//            // Remove coupon & product again
//            cartPOM.removeCoupon(coupon2);
//            cartPOM.removeProduct();




