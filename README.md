# E-Commerce Test Automation Framework  

This project is an end-to-end automated testing framework for the [Edgewords Demo E-Commerce site](https://www.edgewordstraining.co.uk/demo-site).  
It uses **Java, Selenium WebDriver, JUnit 5, and Allure Reports** to validate critical e-commerce functionality such as login, discounts, and checkout.  

---

## ✨ Features  
- **Page Object Model (POM):** Clean separation of test logic and page interactions.  
- **Data-driven:** Credentials, coupons, and billing details stored in `config.properties`.  
- **Reusable Base Setup:** Shared setup/teardown for all tests.  
- **Allure Reporting:** Detailed, step-driven test reports with screenshots.  
- **Error Handling:** Automatic screenshots on errors and resilience for common issues.  

---

## 📂 Project Structure  

src
├── main
│ └── java
│ └── uk.co.twoitesting
│ ├── pomclasses # Page Object Model classes
│ └── utilities # ConfigLoader, Helpers
└── test
└── java
└── uk.co.twoitesting
├── basetests # BaseTests (setup/teardown)
└── test # TestCase1 & TestCase2

markdown
Copy code

- **BaseTests.java** – Sets up browser driver, initializes POMs, handles cleanup.  
- **POM Classes** – Encapsulate functionality of Login, Shop, Cart, Checkout, and Account pages.  
- **Helpers.java** – Utility functions (screenshots, price parsing).  
- **ConfigLoader.java** – Loads test data (URLs, credentials, coupons, billing info).  
- **TestCase1.java** – Validates coupon discounts and totals in the cart.  
- **TestCase2.java** – Completes a full checkout and verifies the order in My Account.  

---

## 🧪 Test Scenarios  

### Test Case 1 – Coupon Discounts  
1. Login with a registered user.  
2. Add Polo Shirt to the cart.  
3. Apply discount codes (`Edgewords` and `2idiscount`).  
4. Verify correct discount & total price calculation.  
5. Remove product and coupon, assert cart is empty.  

### Test Case 2 – Complete Checkout  
1. Login with a registered user.  
2. Add Polo Shirt to the cart and proceed to checkout.  
3. Fill billing details from config.  
4. Select *Check payments* as payment method.  
5. Place the order and capture the order number.  
6. Verify order appears in **My Account → Orders**.  

---

Setup & Running Tests  
Prerequisites  
- Java 11+  
- Maven  
- Chrome or Firefox browser  
- Git  

Install Dependencies  
mvn clean install
Run Tests
Run all tests




