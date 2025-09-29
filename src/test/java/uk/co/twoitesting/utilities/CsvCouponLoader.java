package uk.co.twoitesting.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvCouponLoader {

    public static List<CouponData> loadCoupons(String filePath) {
        List<CouponData> coupons = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true; // skip header
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String key = parts[0].trim();
                    String code = parts[1].trim();
                    double discount = Double.parseDouble(parts[2].trim());
                    coupons.add(new CouponData(key, code, discount));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load coupons from CSV", e);
        }
        return coupons;
    }
}