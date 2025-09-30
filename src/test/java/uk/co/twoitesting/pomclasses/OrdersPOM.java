package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
import uk.co.twoitesting.utilities.Helpers;

import java.util.List;

public class OrdersPOM {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final NavPOM navPOM;

    private final By ordersLink = By.cssSelector(
            "#post-7 > div > div > nav > ul > li.woocommerce-MyAccount-navigation-link.woocommerce-MyAccount-navigation-link--orders > a"
    );

    private final By orderRowsLocator = By.cssSelector("table.woocommerce-orders-table tbody tr");
    private final By orderNumbersLocator = By.cssSelector("td.woocommerce-orders-table__cell-order-number a");

    public OrdersPOM(WebDriver driver, WebDriverWait wait, NavPOM navPOM) {
        this.driver = driver;
        this.wait = wait;
        this.navPOM = navPOM;
    }

    public boolean isOrderPresent(String orderNumber) {
        try {
            // Navigate to My Account -> Orders
            navPOM.goToMyAccount();

            wait.until(ExpectedConditions.elementToBeClickable(ordersLink)).click();

            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(orderRowsLocator, 0));

            List<WebElement> orders = driver.findElements(orderNumbersLocator);

            System.out.println("Orders found on My Orders page:");
            orders.forEach(e -> System.out.println(" - " + e.getText().trim()));

            boolean found = orders.stream()
                    .map(e -> e.getText().trim().replace("#", ""))
                    .anyMatch(text -> text.equals(orderNumber));

            if (!found) {
                System.out.println("Order number " + orderNumber + " not found in My Orders.");
                Helpers.takeScreenshot(driver, "OrderNotFound");
            } else {
                System.out.println("Order number " + orderNumber + " successfully found!");
            }

            return found;

        } catch (Exception e) {
            Helpers.takeScreenshot(driver, "VerifyOrderError");
            System.out.println("Error verifying order: " + e.getMessage());
            return false;
        }
    }
}
