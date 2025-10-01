package uk.co.twoitesting.utilities;

// Define a record class named CouponData to store coupon information
// Record automatically creates immutable fields, constructor, getters, equals(), hashCode(), and toString()
public record CouponData(String key, String code, double discount) {

    // Override the default toString() method to provide a custom string representation
    @Override
    public String toString() {
        // Return a formatted string showing the coupon key, code, and discount value
        return key + " (" + code + " : " + discount + ")";
    }
}
