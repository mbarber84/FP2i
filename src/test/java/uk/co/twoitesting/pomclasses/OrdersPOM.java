// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Selenium WebDriver classes for browser interaction
import org.openqa.selenium.*;
// Import Selenium support classes for explicit waits
import org.openqa.selenium.support.ui.*;
// Import NavPOM to navigate through the site
import uk.co.twoitesting.pomclasses.componentPOM.NavPOM;
// Import helper utilities such as screenshot capture
import uk.co.twoitesting.utilities.Helpers;

// Import List collection for storing multiple WebElements
import java.util.List;

// Define OrdersPOM class for interacting with the "My Orders" page
public class OrdersPOM {

    // WebDriver instance to control the browser
    private final WebDriver driver;
    // WebDriverWait instance to wait for elements to appear or become clickable
    private final WebDriverWait wait;
    // NavPOM instance to perform navigation actions
    private final NavPOM navPOM;

    // Locator for the "Orders" link in the My Account navigation menu
    private final By ordersLink = By.cssSelector(
            "#post-7 > div > div > nav > ul > li.woocommerce-MyAccount-navigation-link.woocommerce-MyAccount-navigation-link--orders > a"
    );

    // Locator for all order rows in the orders table
    private final By orderRowsLocator = By.cssSelector("table.woocommerce-orders-table tbody tr");
    // Locator for all order numbers in the orders table
    private final By orderNumbersLocator = By.cssSelector("td.woocommerce-orders-table__cell-order-number a");

    // Constructor for OrdersPOM
    public OrdersPOM(WebDriver driver, WebDriverWait wait, NavPOM navPOM) {
        this.driver = driver;   // Assign driver instance to class variable
        this.wait = wait;       // Assign wait instance to class variable
        this.navPOM = navPOM;   // Assign navPOM instance to class variable
    }

    // Method to check if a specific order is present in "My Orders"
    public boolean isOrderPresent(String orderNumber) {
        try {
            // Navigate to "My Account" page using NavPOM
            navPOM.goToMyAccount();

            // Wait until the "Orders" link is clickable and click it
            wait.until(ExpectedConditions.elementToBeClickable(ordersLink)).click();

            // Wait until there is at least one order row in the table
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(orderRowsLocator, 0));

            // Find all order number elements on the page
            List<WebElement> orders = driver.findElements(orderNumbersLocator);

            // Print all orders found on the page
            System.out.println("Orders found on My Orders page:");
            orders.forEach(e -> System.out.println(" - " + e.getText().trim()));

            // Check if any of the order numbers match the given orderNumber (removing # symbol)
            boolean found = orders.stream()
                    .map(e -> e.getText().trim().replace("#", ""))
                    .anyMatch(text -> text.equals(orderNumber));

            // Print appropriate message based on whether the order was found
            if (!found) {
                System.out.println("Order number " + orderNumber + " not found in My Orders.");
                Helpers.takeScreenshot(driver, "OrderNotFound"); // Take screenshot if order not found
            } else {
                System.out.println("Order number " + orderNumber + " successfully found!");
            }

            // Return true if order is found, false otherwise
            return found;

        } catch (Exception e) {
            // Catch any exceptions during verification, print error, and take screenshot
            Helpers.takeScreenshot(driver, "VerifyOrderError");
            System.out.println("Error verifying order: " + e.getMessage());
            return false; // Return false if any error occurs
        }
    }
}
