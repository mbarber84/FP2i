# Edgewords Demo E-Commerce Automated Test Framework

This project is an end-to-end automated testing framework for the [Edgewords Demo E-Commerce site](https://www.edgewordstraining.co.uk/demo-site).  
It uses **Java, Selenium WebDriver, JUnit 5, and Allure Reports** to validate critical e-commerce functionality such as login, discounts, and checkout.

---

## ✨ Features

- **Page Object Model (POM):** Clean separation of test logic and page interactions.
- **Data-driven Testing:** Test data such as credentials, coupons, and billing details stored in `config.properties` and `coupons.csv`.
- **Reusable Base Setup:** `BaseTests` handles browser setup, teardown, and shared POM initialization.
- **Allure Reporting:** Step-driven reports with automatic screenshots on failures or key actions.
- **Error Handling:** Graceful handling of common issues, e.g., popups, empty carts, or missing elements.
- **Utilities:** `Helpers` for screenshots, price parsing, scrolling, and reusable functions.

---

## 📂 Project Structure

src
├── main
│ └── java
│ └── uk.co.twoitesting
│ ├── pomclasses # Page Object Model classes
│ │ ├── componentPOM # Components like NavPOM, PopUpPOM
│ │ ├── LoginPOM.java
│ │ ├── ShopPOM.java
│ │ ├── CartPOM.java
│ │ ├── CheckoutPOM.java
│ │ ├── AccountPOM.java
│ │ └── OrdersPOM.java
│ └── utilities # Helpers, ConfigLoader, CsvCouponLoader, CouponData
└── test
└── java
└── uk.co.twoitesting
├── basetests # BaseTests.java
└── test # TestCase1.java, TestCase2.java

pgsql
Copy code

- **BaseTests.java** – Sets up browser driver, initializes POMs, handles cleanup and common cart/account actions.
- **POM Classes** – Encapsulate interactions for Login, Shop, Cart, Checkout, Orders, Account pages, and reusable components (navigation, popups).
- **Helpers.java** – Utility functions for screenshots, scrolling, and parsing prices to `BigDecimal`.
- **ConfigLoader.java** – Loads configuration from `config.properties` (URLs, credentials, billing info).
- **CsvCouponLoader.java / CouponData.java** – Load coupon data from `coupons.csv` and represent them as objects.
- **TestCase1.java** – Validates coupon discounts and cart totals using data-driven tests.
- **TestCase2.java** – Executes a full checkout and verifies the order appears in My Account.

---

## 🧪 Test Scenarios

### Test Case 1 – Coupon Discounts
1. Login with a registered user.
2. Navigate to Shop, add **Polo Shirt** to the cart.
3. Apply discount codes (`Edgewords` and `2idiscount`).
4. Verify that discounts and total price calculations are correct.
5. Remove product and coupon, assert cart is empty.

### Test Case 2 – Complete Checkout
1. Login with a registered user.
2. Add **Polo Shirt** to the cart and proceed to checkout.
3. Fill billing details from `config.properties`.
4. Select **Check payments** as payment method.
5. Place the order and capture the order number.
6. Verify order appears in **My Account → Orders**.
7. Clean up: remove product from cart and log out.

---

## ⚙ Configuration Files

**config.properties**

```properties
# Base URL
base.url=https://www.edgewordstraining.co.uk/demo-site


csv
Copy code
# key, code, discount
coupon.edgewords,Edgewords,0.15
coupon.2idiscount,2idiscount,0.25
🚀 Setup & Running Tests
Prerequisites
Java 11+

Maven

Chrome or Firefox browser

Git

Install Dependencies
bash
Copy code
mvn clean install
Run Tests
Run all tests

bash
Copy code
mvn test
Generate Allure Reports

bash
Copy code
mvn allure:serve
This will build and open a detailed interactive report with screenshots.

📝 Notes
The framework uses a Page Object Model (POM) design for maintainable and reusable page interactions.

Tests are data-driven, supporting multiple products and coupon combinations.

Screenshots are automatically captured on errors and at key steps.

The framework handles popups, empty carts, and other site-specific behaviors to make tests reliable.

You can extend the tests easily by adding new POM methods, scenarios, or CSV-based coupons.