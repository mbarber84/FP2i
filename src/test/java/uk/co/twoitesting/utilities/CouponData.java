package uk.co.twoitesting.utilities;

public record CouponData(String key, String code, double discount) {
    @Override
    public String toString() {
        return key + " (" + code + " : " + discount + ")";
    }
}
