# Flipkart Login Automation using Java Selenium

A complete, production-grade test automation project for the Flipkart login page using **Java**, **Selenium 4**, and **Page Object Model (POM)**.

---

## 📁 Project Structure

```
flipkart-selenium-automation/
├── pom.xml                                   # Maven dependencies & build configuration
├── README.md                                 # Documentation & run instructions
└── src
    ├── main/java/com/flipkart/automation
    │   ├── FlipkartLoginAutomation.java      # Executable standalone runner with interactive OTP prompt
    │   └── pages
    │       └── FlipkartLoginPage.java        # Page Object Model class with resilient locators
    └── test/java/com/flipkart/automation
        └── FlipkartLoginTest.java            # TestNG automated test case
```

---

## ⚙️ Prerequisites

1. **Java JDK 17 or higher** installed and added to `PATH`.
2. **Apache Maven 3.8+** installed.
3. **Google Chrome** browser installed (Selenium 4 automatically downloads matching `chromedriver` via built-in Selenium Manager).

---

## 🚀 How to Run

### Option 1: Run the Standalone Script (with Console OTP Entry)
Compile and run using Maven exec:
```bash
mvn compile exec:java -Dexec.mainClass="com.flipkart.automation.FlipkartLoginAutomation"
```
Or open the project in **IntelliJ IDEA** / **Eclipse** / **VS Code** and run `FlipkartLoginAutomation.main()`.

### Option 2: Run with TestNG
```bash
mvn test
```

---

## 🛡️ Critical Considerations for Flipkart Automation

1. **Bot Protection (Akamai Shield)**:
   Flipkart employs Akamai Bot Manager. Standard Selenium drivers reveal `navigator.webdriver = true`. This project configures `ChromeOptions` (`--disable-blink-features=AutomationControlled`, `excludeSwitches: enable-automation`) to bypass basic automated browser flags.
2. **Two-Factor Authentication (OTP)**:
   Flipkart primarily uses SMS OTP for authentication.
   - For automated CI/CD pipelines, QA teams typically use staging mock accounts with static OTPs or integrate SMS APIs (e.g., Twilio).
   - In this script, a console prompt allows you to enter the real OTP received during manual testing.
