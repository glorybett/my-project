package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.tests.base.BaseTest;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import java.util.concurrent.TimeUnit;

@Epic("Авторизация пользователя")
@Feature("Функционал входа в систему")
@Owner("Тестировщик")
public class LoginTests extends BaseTest {

    @Test(priority = 1)
    @Description("Тест успешного входа с валидными учетными данными")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Пользователь может войти с правильными учетными данными")
    @Tag("positive")
    @Tag("smoke")
    public void testSuccessfulLogin() {
        logger.info("Test: Successful login");

        // Act
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");

        // Assert
        Assert.assertTrue(inventoryPage.isPageLoaded(),
                "Inventory page should be loaded after successful login");
        Assert.assertTrue(inventoryPage.getCurrentUrl().contains("inventory.html"),
                "URL should contain 'inventory.html'");
        Assert.assertEquals(inventoryPage.getInventoryItemsCount(), 6,
                "Should display 6 inventory items");

        logger.info("Login successful - test passed");
    }

    @Test(priority = 2)
    @Description("Тест входа с неверным паролем")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Пользователь не может войти с неправильным паролем")
    @Tag("negative")
    public void testLoginWithInvalidPassword() {
        logger.info("Test: Login with invalid password");

        // Act
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLogin();

        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid credentials");
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Epic sadface"),
                "Error message should contain 'Epic sadface'");
        Assert.assertTrue(errorMessage.contains("Username and password do not match"),
                "Error message should indicate password mismatch");

        logger.info("Invalid password test passed");
    }

    @Test(priority = 3)
    @Description("Тест входа заблокированным пользователем")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Заблокированный пользователь не может войти в систему")
    @Tag("negative")
    @Tag("security")
    public void testLoginWithLockedOutUser() {
        logger.info("Test: Login with locked out user");

        // Act
        loginPage.enterUsername("locked_out_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for locked out user");
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Epic sadface"),
                "Error message should contain 'Epic sadface'");
        Assert.assertTrue(errorMessage.contains("Sorry, this user has been locked out"),
                "Error message should indicate user is locked out");

        logger.info("Locked out user test passed");
    }

    @Test(priority = 4)
    @Description("Тест входа с пустыми полями")
    @Severity(SeverityLevel.NORMAL)
    @Story("Система должна валидировать пустые поля")
    @Tag("negative")
    @Tag("validation")
    public void testLoginWithEmptyFields() {
        logger.info("Test: Login with empty fields");

        // Act
        loginPage.clickLogin();

        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for empty fields");
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Epic sadface"),
                "Error message should contain 'Epic sadface'");
        Assert.assertTrue(errorMessage.contains("Username is required"),
                "Error message should indicate username is required");

        logger.info("Empty fields test passed");
    }

    @Test(priority = 5)
    @Description("Тест входа пользователем performance_glitch_user")
    @Severity(SeverityLevel.NORMAL)
    @Story("Система должна корректно работать с пользователем, вызывающим задержки")
    @Tag("performance")
    @Tag("positive")
    public void testLoginWithPerformanceGlitchUser() {
        logger.info("Test: Login with performance glitch user");

        // Act
        loginPage.enterUsername("performance_glitch_user");
        loginPage.enterPassword("secret_sauce");
        long startTime = System.currentTimeMillis();
        loginPage.clickLogin();

        // Wait for page to load with Awaitility
        Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(Durations.ONE_SECOND)
                .untilAsserted(() -> {
                    InventoryPage inventoryPage = new InventoryPage(driver);
                    Assert.assertTrue(inventoryPage.isPageLoaded(),
                            "Inventory page should load despite glitches");
                });

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        // Assert
        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isPageLoaded(),
                "Inventory page should be loaded");
        Assert.assertTrue(inventoryPage.getCurrentUrl().contains("inventory.html"),
                "URL should contain 'inventory.html'");

        logger.info("Performance glitch user test passed. Page loaded in " + loadTime + "ms");

        // Optional: Assert that page loads within reasonable time
        Assert.assertTrue(loadTime < 15000,
                "Page should load within 15 seconds even with glitches. Actual: " + loadTime + "ms");
    }
}