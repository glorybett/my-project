package com.saucedemo.tests.base;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.WebDriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

public class BaseTest {
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp(Method method) {
        logger.info("Starting test: " + method.getName());
        Allure.description("Тест: " + method.getName());

        driver = WebDriverFactory.createDriver();
        loginPage = new LoginPage(driver);
        loginPage.open();
        logger.info("Browser opened and page loaded");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                takeScreenshot(result.getName());
                logger.error("Test failed: " + result.getName());

                // Attach page source on failure
                try {
                    String pageSource = driver.getPageSource();
                    Allure.addAttachment("Page Source", "text/html", pageSource, ".html");
                } catch (Exception e) {
                    logger.error("Failed to capture page source: " + e.getMessage());
                }
            }
            driver.quit();
            logger.info("Browser closed");
        }
    }

    private void takeScreenshot(String testName) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(testName + " Screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
            logger.info("Screenshot taken for failed test: " + testName);
        } catch (Exception e) {
            logger.error("Failed to take screenshot: " + e.getMessage());
        }
    }
}