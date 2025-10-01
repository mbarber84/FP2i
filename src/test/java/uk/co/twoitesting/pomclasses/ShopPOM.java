// Define the package where this class belongs
package uk.co.twoitesting.pomclasses;

// Import Allure annotations and classes for reporting steps and attaching files
import io.qameta.allure.Step;
import io.qameta.allure.Allure;
// Import Selenium WebDriver classes for interacting with web elements
import org.openqa.selenium.*;
// Import Selenium support classes for explicit waits
import org.openqa.selenium.support.ui.*;
// Import PopUpPOM for handling popups on the shop page
import uk.co.twoitesting.pomclasses.componentPOM.PopUpPOM;
// Import helper utilities such as scrolling into view
import uk.co.twoitesting.utilities.Helpers;
// Import Java classes for file input/output
import java.io.*;

// Define ShopPOM class for performing actions on the shop page
public class ShopPOM {

    // WebDriver instance to control the browser
    private final WebDriver driver;
    // WebDriverWait instance to wait for elements to appear or become clickable
    private final WebDriverWait wait;

    // Locators for elements on the shop page
    private final By shopLink = By.linkText("Shop");             // Link to navigate to the Shop page
    private final By dismissBanner = By.linkText("Dismiss");     // Link to dismiss any popup/banner
    private final By poloAddButton = By.cssSelector("li.product:nth-child(9) > a:nth-child(2)"); // Button to add Polo Shirt to cart
    private final PopUpPOM popupPom;                             // Instance of PopUpPOM to handle popups

    // Constructor for ShopPOM
    public ShopPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; // Assign WebDriver instance to class variable
        this.wait = wait;     // Assign WebDriverWait instance to class variable
        this.popupPom = new PopUpPOM(driver, wait); // Initialize PopUpPOM for popup handling
    }

    // Method to open the Shop page, annotated with @Step for Allure reporting
    @Step("Open the Shop page")
    public void openShop() {
        // Wait until the Shop link is clickable
        WebElement shop = wait.until(ExpectedConditions.elementToBeClickable(shopLink));
        // Scroll the Shop link into view using helper method
        Helpers.scrollIntoView(driver, shop);
        try {
            shop.click(); // Attempt to click the Shop link
        } catch (ElementClickInterceptedException e) {
            // If normal click is intercepted (e.g., by popup), click using JavaScript
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", shop);
        }
    }

    // Method to dismiss a popup if it appears on the page
    public void dismissPopupIfPresent() {
        popupPom.dismissPopupIfPresent(); // Delegate to PopUpPOM
    }

    // Method to add a specific product to the cart
    @Step("Add {productName} to cart")
    public void addProductToCart(String productName) {
        // Build a dynamic CSS selector based on the product's aria-label
        String ariaLabel = String.format("Add “%s” to your cart", productName);
        By addButton = By.cssSelector("[aria-label='" + ariaLabel + "']");

        // Wait until the add-to-cart button is present in the DOM
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(addButton));
        // Scroll the button into view using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        // Wait until the button is clickable and click it
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    // Method to click the "View cart" link, annotated for Allure reporting
    @Step("View the cart")
    public void viewCart() {
        // Wait until "View cart" link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("View cart"))).click();
    }

    // Method to attach a screenshot to the Allure report
    @Step("Attach screenshot: {name}")
    public void attachScreenshot(String name, String path) {
        try (InputStream is = new FileInputStream(path)) { // Open file input stream to read screenshot
            Allure.addAttachment(name, is);               // Attach screenshot to Allure report
        } catch (IOException e) {                         // Handle exceptions (e.g., file not found)
            e.printStackTrace();                          // Print stack trace for debugging
        }
    }
}
