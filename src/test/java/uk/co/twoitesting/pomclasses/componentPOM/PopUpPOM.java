package uk.co.twoitesting.pomclasses.componentPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Step;

import java.time.Duration;


public class PopUpPOM {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locator for the popup dismiss button
    private By dismissBanner = By.cssSelector(".woocommerce-store-notice__dismiss-link"); // Replace with your actual selector

    // Constructor
    public PopUpPOM(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    @Step("Dismiss popup if present")
    public void dismissPopupIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement dismiss = wait.until(ExpectedConditions.elementToBeClickable(dismissBanner));
            dismiss.click();
            System.out.println("Popup dismissed successfully.");
        } catch (TimeoutException e) {
            System.out.println("No dismiss banner found.");
        }
    }

}
