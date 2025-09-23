// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Allure annotations and classes for reporting
import io.qameta.allure.Step;
import io.qameta.allure.Allure;
// Import Selenium classes for web elements and waits
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.twoitesting.utilities.Helpers;
// Import Java classes for file handling
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


// Define ShopPOM class for actions on the shop page
public class ShopPOM {

    // Store the browser driver to control the browser
    private final WebDriver driver;
    // Store WebDriverWait to wait for elements to appear
    private final WebDriverWait wait;

    // Locators for different elements on the shop page
    private final By shopLink = By.linkText("Shop");             // Link to open Shop page
    private final By dismissBanner = By.linkText("Dismiss");     // Link to dismiss popup/banner
    private final By poloAddButton = By.cssSelector("li.product:nth-child(9) > a:nth-child(2)"); // Button to add Polo Shirt

    // Constructor to set up ShopPOM with browser driver and wait
    public ShopPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; // Save driver
        this.wait = wait;     // Save wait
    }

    // Method to open the Shop page, annotated for Allure reporting
    @Step("Open the Shop page")
    public void openShop() {
        WebElement shop = wait.until(ExpectedConditions.elementToBeClickable(shopLink));
        Helpers.scrollIntoView(driver, shop);
        try {
            shop.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", shop);
        }
    }

    // Method to dismiss popup/banner if it appears
    @Step("Dismiss popup if present")
    public void dismissPopupIfPresent() {
        try {
            // Wait until dismiss banner is clickable, then click it
            WebElement dismiss = wait.until(ExpectedConditions.elementToBeClickable(dismissBanner));
            dismiss.click();
        } catch (Exception ignored) {
            // If no popup appears, print message
            System.out.println("No dismiss banner found.");
        }
    }

    // Method to add Polo Shirt to the cart
//    @Step("Add Polo Shirt to cart")
//    public void addPoloToCart() {
//        // Wait until the add button is present in the page DOM
//        WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(poloAddButton));
//        // Scroll the add button into view using JavaScript
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addButton);
//        // Wait until the add button is clickable, then click it
//        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
//    }

    @Step("Add {productName} to cart")
    public void addProductToCart(String productName) {
        // Build dynamic aria-label locator
        String ariaLabel = String.format("Add “%s” to your cart", productName);
        By addButton = By.cssSelector("[aria-label='" + ariaLabel + "']");

        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(addButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    // Method to click the "View cart" link
    @Step("View the cart")
    public void viewCart() {
        // Wait until "View cart" link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("View cart"))).click();
    }

    // Method to attach a screenshot to Allure report
    @Step("Attach screenshot: {name}")
    public void attachScreenshot(String name, String path) {
        try (InputStream is = new FileInputStream(path)) { // Open file input stream
            Allure.addAttachment(name, is);               // Attach file to Allure report
        } catch (IOException e) {                         // Handle exceptions if file not found
            e.printStackTrace();
        }
    }
}
