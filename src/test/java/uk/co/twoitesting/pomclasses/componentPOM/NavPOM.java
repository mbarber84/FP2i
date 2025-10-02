// Define the package where this class belongs
package uk.co.twoitesting.pomclasses.componentPOM;

// Import Selenium WebDriver classes for browser interaction
import org.openqa.selenium.*;
// Import Selenium support classes for explicit waits
import org.openqa.selenium.support.ui.*;

// Define the NavPOM class for navigation bar actions
public class NavPOM {

    // WebDriver instance used to interact with the browser
    private WebDriver driver;
    // WebDriverWait instance used to wait for elements to be clickable or visible
    private final WebDriverWait wait;

    // Locators for navigation links using By strategy
    private final By homeLink = By.linkText("Home");         // Locator for Home link
    private final By shopLink = By.linkText("Shop");         // Locator for Shop link
    private final By cartLink = By.linkText("Cart");         // Locator for Cart link
    private final By checkoutLink = By.linkText("Checkout"); // Locator for Checkout link
    private final By myAccountLink = By.linkText("My account"); // Locator for My Account link
    private final By blogLink = By.linkText("Blog");         // Locator for Blog link

    // Constructor for NavPOM that takes WebDriver and WebDriverWait as parameters
    public NavPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; // Assign the driver instance to the class variable
        this.wait = wait;     // Assign the wait instance to the class variable
    }

    // Method to navigate to the Home page
    public void goToHome() {
        // Wait until the Home link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(homeLink)).click();
    }

    // Method to navigate to the Shop page
    public void goToShop() {
        // Wait until the Shop link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(shopLink)).click();
    }

    // Method to navigate to the Cart page
    public void goToCart() {
        // Wait until the Cart link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }

    // Method to navigate to the Checkout page
    public void goToCheckout() {
        // Wait until the Checkout link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(checkoutLink)).click();
    }

    // Method to navigate to the My Account page
    public void goToMyAccount() {
        // Wait until the My Account link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(myAccountLink)).click();
    }

    // Method to navigate to the Blog page
    public void goToBlog() {
        // Wait until the Blog link is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(blogLink)).click();
    }

    // Optional method to dynamically navigate to a link based on its name
    public void goTo(String linkName) {
        By locator = null; // Initialize a locator variable to null

        // Determine which locator to use based on the linkName argument
        switch (linkName.toLowerCase()) {
            case "home":
                locator = homeLink; // Set locator for Home
                break;
            case "shop":
                locator = shopLink; // Set locator for Shop
                break;
            case "cart":
                locator = cartLink; // Set locator for Cart
                break;
            case "checkout":
                locator = checkoutLink; // Set locator for Checkout
                break;
            case "my account":
            case "my-account":
                locator = myAccountLink; // Set locator for My Account (allowing multiple formats)
                break;
            case "blog":
                locator = blogLink; // Set locator for Blog
                break;
            default:
                // Throw an exception if the linkName does not match any known link
                throw new IllegalArgumentException("No nav link found for: " + linkName);
        }

        // Wait until the determined locator is clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
}
