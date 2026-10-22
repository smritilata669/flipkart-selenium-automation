package com.flipkart.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model representing the Flipkart Login Page / Modal.
 */
public class FlipkartLoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Locators (Resilient to Flipkart UI updates) ---
    // Direct input field for Email or Mobile Number
    private final By emailOrPhoneInput = By.xpath(
            "//form//input[@type='text'] | " +
            "//input[contains(@class, 'r4vIwl')] | " +
            "//span[contains(text(), 'Email') or contains(text(), 'Mobile')]/ancestor::div[contains(@class, 'form')]//input"
    );

    // "Request OTP" or "Continue" button
    private final By requestOtpButton = By.xpath(
            "//button[contains(text(), 'Request OTP')] | " +
            "//form//button[@type='submit']"
    );

    // Password input field (if password login is prompted)
    private final By passwordInput = By.xpath("//input[@type='password']");

    // "Login" submit button when password flow is used
    private final By loginSubmitButton = By.xpath("//button[@type='submit' and contains(., 'Login')]");

    // Close button on login popup (if accessed from homepage)
    private final By closePopupBtn = By.xpath("//button[contains(text(), '✕')] | //span[@role='button']");

    // Indicator of successful login (e.g., My Account dropdown / User Profile Name)
    private final By myAccountDropdown = By.xpath(
            "//div[contains(text(), 'My Account')] | " +
            "//span[contains(text(), 'Account')] | " +
            "//div[contains(@class, 'header')]//a[contains(@href, '/account')]"
    );

    public FlipkartLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates directly to the Flipkart login page.
     */
    public void open() {
        driver.get("https://www.flipkart.com/account/login");
    }

    /**
     * Enters mobile number or email ID into the input field.
     *
     * @param identifier Mobile number (10 digits) or Email ID
     */
    public void enterEmailOrPhone(String identifier) {
        WebElement inputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(emailOrPhoneInput));
        inputElement.clear();
        inputElement.sendKeys(identifier);
    }

    /**
     * Clicks the 'Request OTP' or submission button.
     */
    public void clickRequestOtp() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(requestOtpButton));
        btn.click();
    }

    /**
     * Enters password if the account triggers a password prompt.
     *
     * @param password Account password
     */
    public void enterPassword(String password) {
        WebElement passElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passElement.clear();
        passElement.sendKeys(password);
    }

    /**
     * Clicks the final Login button on password submission.
     */
    public void clickLogin() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loginSubmitButton));
        btn.click();
    }

    /**
     * Checks if login was successful by waiting for the My Account element.
     *
     * @param timeoutSeconds Duration to wait for login confirmation
     * @return true if logged in, false otherwise
     */
    public boolean isUserLoggedIn(int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement accountElement = customWait.until(ExpectedConditions.visibilityOfElementLocated(myAccountDropdown));
            return accountElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Dismisses the login popup if browsing as a guest from the homepage.
     */
    public void closePopupIfPresent() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement close = shortWait.until(ExpectedConditions.elementToBeClickable(closePopupBtn));
            close.click();
        } catch (Exception ignored) {
            // Popup not displayed
        }
    }
}
