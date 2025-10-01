// Define the package where this class belongs
package uk.co.twoitesting.pomclasses.componentPOM;

// Import Selenium WebDriver classes for browser interaction
import org.openqa.selenium.*;
// Import Selenium support classes for explicit waits
import org.openqa.selenium.support.ui.*;
// Import Allure annotation for test reporting steps
import io.qameta.allure.Step;
// Import Duration class to define timeout durations
import java.time.Duration;

// Define the PopUpPOM class to handle popup banners on the website
public class PopUpPOM {

    // WebDriver instance used to interact with the browser
    private WebDriver driver;
    // WebDriverWait instance used to wait for elements to appear or become clickable
    private WebDriverWait wait;

    // Locator for the popup dismiss button using CSS selector
    private By dismissBanner = By.cssSelector(".woocommerce-store-notice__dismiss-link");
    // Note: Replace this selector with the actual selector for the popup on your site

    // Constructor for PopUpPOM that takes WebDriver and WebDriverWait as parameters
    public PopUpPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; // Assign the driver instance to the class variable
        this.wait = wait;     // Assign the wait instance to the class variable
    }

    // Method annotated with @Step for Allure reporting, describing its purpose
    @Step("Dismiss popup if present")
    public void dismissPopupIfPresent() {
        try {
            // Create a local WebDriverWait with a timeout of 10 seconds
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // Wait until the dismiss button is clickable, then assign it to 'dismiss' variable
            WebElement dismiss = wait.until(ExpectedConditions.elementToBeClickable(dismissBanner));
            // Click the dismiss button to close the popup
            dismiss.click();
            // Print a success message to the console
            System.out.println("Popup dismissed successfully.");
        } catch (TimeoutException e) {
            // Catch TimeoutException if the popup is not present
            System.out.println("No dismiss banner found."); // Print a message if no popup was found
        }
    }

}
