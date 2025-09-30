// Define the package where this class belongs
package uk.co.twoitesting.utilities;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

// Define Helpers class with utility methods
public class Helpers {

    // Method to extract numeric value from a price string like "£12.34"
    public static BigDecimal extractPrice(String priceText) {
        // Remove £ sign, minus sign, extra spaces
        String cleaned = priceText.replace("£", "").replace("-", "").trim();
        // Convert to BigDecimal with 2 decimal places
        return new BigDecimal(cleaned).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // Method to take a screenshot, save locally, and attach to Allure report
    public static void takeScreenshot(WebDriver driver, String name) {
        try {
            // Take screenshot and store it as a temporary file
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Create folder "screenshots" if it doesn't exist
            File destDir = new File("target/screenshots");
            if (!destDir.exists()) {
                destDir.mkdir();
            }

            // Generate a filename with timestamp to avoid overwriting
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File destFile = new File(destDir, name + "_" + timestamp + ".png");

            // Copy the screenshot file to the screenshots folder
            FileHandler.copy(src, destFile);

            // Print message with screenshot path
            System.out.println(" Screenshot saved: " + destFile.getAbsolutePath());

            // Attach the screenshot to Allure report for test evidence
            Allure.addAttachment(name + "_" + timestamp,
                    new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        } catch (IOException e) {
            // Print error if saving screenshot fails
            System.out.println(" Failed to save screenshot: " + e.getMessage());
        }
    }
    // Scroll an element into view (centered)
    public static void scrollIntoView(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
    }


}
