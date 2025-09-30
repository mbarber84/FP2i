package uk.co.twoitesting.pomclasses.componentPOM;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class NavPOM {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for navigation links
    private By homeLink = By.linkText("Home");
    private By shopLink = By.linkText("Shop");
    private By cartLink = By.linkText("Cart");
    private By checkoutLink = By.linkText("Checkout");
    private By myAccountLink = By.linkText("My account");
    private By blogLink = By.linkText("Blog");

    public NavPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void goToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(homeLink)).click();
    }

    public void goToShop() {
        wait.until(ExpectedConditions.elementToBeClickable(shopLink)).click();
    }

    public void goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }

    public void goToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutLink)).click();
    }

    public void goToMyAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountLink)).click();
    }

    public void goToBlog() {
        wait.until(ExpectedConditions.elementToBeClickable(blogLink)).click();
    }

    // Optional: a method that takes link name and clicks dynamically
    public void goTo(String linkName) {
        By locator = null;
        switch (linkName.toLowerCase()) {
            case "home":
                locator = homeLink;
                break;
            case "shop":
                locator = shopLink;
                break;
            case "cart":
                locator = cartLink;
                break;
            case "checkout":
                locator = checkoutLink;
                break;
            case "my account":
            case "my-account":
                locator = myAccountLink;
                break;
            case "blog":
                locator = blogLink;
                break;
            default:
                throw new IllegalArgumentException("No nav link found for: " + linkName);
        }
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
}
