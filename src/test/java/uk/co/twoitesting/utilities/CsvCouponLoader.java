// Define the package where this class belongs
package uk.co.twoitesting.utilities;

// Import classes for file reading
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
// Import List and ArrayList to store coupon data
import java.util.ArrayList;
import java.util.List;

// Define CsvCouponLoader utility class to read coupons from a CSV file
public class CsvCouponLoader {

    // Static method to load coupons from a CSV file
    public static List<CouponData> loadCoupons(String filePath) {
        // Initialize an empty list to store CouponData objects
        List<CouponData> coupons = new ArrayList<>();

        // Use try-with-resources to automatically close BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;                 // Variable to store each line of the CSV
            boolean firstLine = true;    // Flag to skip the header row

            // Read the CSV file line by line
            while ((line = br.readLine()) != null) {
                if (firstLine) {         // Skip the header line
                    firstLine = false;
                    continue;
                }

                // Split the CSV line into parts using comma as delimiter
                String[] parts = line.split(",");
                // Ensure the line has exactly 3 parts (key, code, discount)
                if (parts.length == 3) {
                    String key = parts[0].trim();           // First column: coupon key
                    String code = parts[1].trim();          // Second column: coupon code
                    double discount = Double.parseDouble(parts[2].trim()); // Third column: discount value

                    // Add a new CouponData object to the list
                    coupons.add(new CouponData(key, code, discount));
                }
            }
        } catch (IOException e) { // Handle exceptions if the file cannot be read
            throw new RuntimeException("Failed to load coupons from CSV", e);
        }

        // Return the list of loaded coupons
        return coupons;
    }
}
