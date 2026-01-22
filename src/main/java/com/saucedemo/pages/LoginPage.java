package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = ".error-message-container")
    private WebElement errorMessage;

    @FindBy(className = "login_logo")
    private WebElement pageLogo;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу логина")
    public void open() {
        driver.get("https://www.saucedemo.com/");
        logger.info("Opened login page");
    }

    @Step("Ввести имя пользователя: {username}")
    public void enterUsername(String username) {
        enterText(usernameField, username, "Username field");
    }

    @Step("Ввести пароль: {password}")
    public void enterPassword(String password) {
        enterText(passwordField, password, "Password field");
    }

    @Step("Нажать кнопку логина")
    public void clickLogin() {
        clickElement(loginButton, "Login button");
    }

    @Step("Выполнить вход с именем пользователя: {username} и паролем: {password}")
    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new InventoryPage(driver);
    }

    @Step("Получить сообщение об ошибке")
    public String getErrorMessage() {
        return getElementText(errorMessage, "Error message");
    }

    @Step("Проверить, отображается ли сообщение об ошибке")
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage, "Error message");
    }

    @Step("Проверить, загружена ли страница логина")
    public boolean isPageLoaded() {
        return isElementDisplayed(pageLogo, "Page logo");
    }
}