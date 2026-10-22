package com.flipkart.automation;

import com.flipkart.automation.pages.FlipkartLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Collections;

public class FlipkartLoginTest {

    private WebDriver driver;
    private FlipkartLoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        loginPage = new FlipkartLoginPage(driver);
    }

    @Test(description = "Verify Flipkart Login Page loads and accepts input")
    public void testLoginInputAndRequestOtp() {
        loginPage.open();

        String samplePhoneNumber = "9999999999";
        loginPage.enterEmailOrPhone(samplePhoneNumber);
        loginPage.clickRequestOtp();

        // Verification: Check if either OTP input is prompted or validation message appears
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean otpOrErrorPrompted = wait.until(d -> {
            boolean hasOtpBox = !d.findElements(By.xpath("//span[contains(text(), 'OTP')] | //input[contains(@class, 'r4vIwl')]")).isEmpty();
            boolean hasError = !d.findElements(By.xpath("//span[contains(text(), 'valid')] | //span[contains(text(), 'Please enter')]")).isEmpty();
            return hasOtpBox || hasError;
        });

        Assert.assertTrue(otpOrErrorPrompted, "Expected OTP screen or validation message after submitting mobile number.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
