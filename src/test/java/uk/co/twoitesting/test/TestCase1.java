package uk.co.twoitesting.test;

import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import uk.co.twoitesting.basetests.BaseTests;
import uk.co.twoitesting.pomclasses.*;
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
import uk.co.twoitesting.utilities.*;

import java.util.List;
import java.util.stream.Stream;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestCase1 extends BaseTests {

    static Stream<TestData> dataProvider() {
        List<String> products = List.of("Polo");
        List<CouponData> coupons =
                CsvCouponLoader.loadCoupons("src/test/resources/coupons.csv");

        return coupons.stream().flatMap(coupon ->
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

        Allure.step("Login to site", () -> {
            LoginPOM loginPOM = new LoginPOM(driver, wait, navPOM);
            loginPOM.open();
            loginPOM.login();
            Helpers.takeScreenshot(driver, "Login Success");

            Assertions.assertTrue(driver.getPageSource().contains("Logout"),
                    "User should be logged in after login");

            NavPOM navPOM = new NavPOM(driver, wait);
            CartPOM cartPOM = new CartPOM(driver, wait);
            navPOM.goToCart();
            cartPOM.removeCoupon(data.coupon.code());
            cartPOM.removeProduct();
        });

        Allure.step("Add " + data.product + " and apply discount " + data.coupon.key(), () -> {
            NavPOM navPOM = new NavPOM(driver, wait);
            ShopPOM shopPOM = new ShopPOM(driver, wait);
            CartPOM cartPOM = new CartPOM(driver, wait);

            navPOM.goToShop();
            shopPOM.dismissPopupIfPresent();
            shopPOM.addProductToCart(data.product);
            navPOM.goToCart();

            Helpers.takeScreenshot(driver,
                    "Cart Before Applying " + data.coupon.key() + " for " + data.product);

            System.out.println("Testing " + data.product +
                    " with discount: " + data.coupon.code() +
                    " (" + (data.coupon.discount() * 100) + "%)");

            cartPOM.applyCoupon(data.coupon.code());

            BigDecimal subtotal = cartPOM.getSubtotalBD();
            BigDecimal discount = cartPOM.getDiscountBD();
            BigDecimal shipping = cartPOM.getShippingBD();
            BigDecimal total = cartPOM.getTotalBD();

            BigDecimal discountRate = BigDecimal.valueOf(data.coupon.discount());
            BigDecimal expectedDiscount = subtotal.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal expectedTotal = subtotal.subtract(expectedDiscount).add(shipping).setScale(2, RoundingMode.HALF_UP);

            Assertions.assertEquals(0, expectedDiscount.compareTo(discount), "Discount should match expected");
            Assertions.assertEquals(0, expectedTotal.compareTo(total), "Total should match expected");

            System.out.printf("Subtotal: £%.2f | Discount: £%.2f (Expected: £%.2f) | Total: £%.2f (Expected: £%.2f)%n",
                    subtotal, discount, expectedDiscount, total, expectedTotal);

            cartPOM.removeCoupon(data.coupon.code());
            cartPOM.removeProduct();

            int cartItems = driver.findElements(By.cssSelector("tr.cart_item")).size();
            Assertions.assertEquals(0, cartItems, "Cart should be empty after removing product");
        });
    }

    record TestData(CouponData coupon, String product) {
        @Override
        public String toString() {
            return product + " with " + coupon.key();
        }
    }
}
