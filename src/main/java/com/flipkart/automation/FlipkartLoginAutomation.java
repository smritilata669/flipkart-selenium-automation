package com.flipkart.automation;

import com.flipkart.automation.pages.FlipkartLoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main execution class to demonstrate Flipkart Login Automation using Java Selenium.
 */
public class FlipkartLoginAutomation {

    public static void main(String[] args) {
        // --- 1. Set Up ChromeOptions to avoid automated bot flags ---
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

        // --- 2. Setup WebDriver ---
        // Selenium 4 automatically resolves driver binaries via Selenium Manager.
        // WebDriverManager can be used if running an older environment.
        WebDriver driver = null;
        try {
            driver = new ChromeDriver(options);
        } catch (Exception e) {
            System.out.println("Selenium Manager initialization fallback. Using WebDriverManager...");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        }

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // --- 3. Navigate to Flipkart Login ---
            System.out.println("[INFO] Navigating to Flipkart login page...");
            FlipkartLoginPage loginPage = new FlipkartLoginPage(driver);
            loginPage.open();

            // Provide your registered mobile number or email ID
            String testPhoneOrEmail = "9876543210"; // Replace with your test credential

            System.out.println("[INFO] Entering phone / email: " + testPhoneOrEmail);
            loginPage.enterEmailOrPhone(testPhoneOrEmail);

            // --- 4. Click 'Request OTP' / Submit ---
            System.out.println("[INFO] Clicking 'Request OTP'...");
            loginPage.clickRequestOtp();

            // --- 5. Handling 2-Factor Authentication (OTP) ---
            /*
             * Note on Live E-Commerce Sites:
             * Flipkart uses real-time SMS OTP and Akamai bot protection.
             * In test/staging environments, companies use fixed OTPs (e.g. 123456)
             * or third-party SMS APIs (Twilio).
             * For manual testing via automation, we prompt the tester in the console
             * or wait for the user to enter OTP.
             */
            System.out.println("\n=======================================================");
            System.out.println("[ACTION REQUIRED] An OTP has been dispatched by Flipkart.");
            System.out.println("Enter the 6-digit OTP received on your mobile in this console,");
            System.out.println("or type 'skip' if you entered it directly in the browser:");
            System.out.print("OTP: ");
            System.out.println("\n=======================================================");

            Scanner scanner = new Scanner(System.in);
            String otpInput = scanner.nextLine().trim();

            if (!otpInput.equalsIgnoreCase("skip") && otpInput.matches("\\d+")) {
                // Check if OTP input fields are available
                List<WebElement> otpBoxes = driver.findElements(By.xpath("//form//input[contains(@class, 'r4vIwl')] | //input[@type='text' and @maxlength='1']"));

                if (otpBoxes.size() == otpInput.length()) {
                    // Separate box for each digit
                    for (int i = 0; i < otpInput.length(); i++) {
                        otpBoxes.get(i).sendKeys(String.valueOf(otpInput.charAt(i)));
                    }
                } else {
                    // Single unified OTP text input field
                    WebElement singleOtpField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[@type='text' and not(@placeholder='Search for Products, Brands and More')]")
                    ));
                    singleOtpField.sendKeys(otpInput);
                }

                // Click Verify / Login if button is present
                try {
                    WebElement verifyButton = driver.findElement(By.xpath("//button[contains(text(), 'Verify') or contains(text(), 'Login')]"));
                    if (verifyButton.isDisplayed()) {
                        verifyButton.click();
                    }
                } catch (Exception ignored) {
                    // Sometimes submitting OTP happens automatically on filling 6 digits
                }
            }

            // --- 6. Verification ---
            System.out.println("[INFO] Verifying login status (waiting up to 30 seconds)...");
            boolean loggedIn = loginPage.isUserLoggedIn(30);

            if (loggedIn) {
                System.out.println("[SUCCESS] Successfully logged into Flipkart!");
            } else {
                System.out.println("[NOTE] Login verification timed out or requires additional verification (e.g., CAPTCHA).");
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Automation encountered an issue: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("[INFO] Test completed. Press Enter to close browser...");
            try {
                System.in.read();
            } catch (Exception ignored) {}

            if (driver != null) {
                driver.quit();
                System.out.println("[INFO] Browser closed.");
            }
        }
    }
}
