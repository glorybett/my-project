package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import io.qameta.allure.Step;
import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(className = "app_logo")
    private WebElement appLogo;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    @Step("Проверить, загружена ли страница инвентаря")
    public boolean isPageLoaded() {
        return isElementDisplayed(appLogo, "App logo");
    }

    @Step("Получить количество товаров в инвентаре")
    public int getInventoryItemsCount() {
        wait.until(driver -> inventoryItems.size() > 0);
        return inventoryItems.size();
    }

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return driver.getTitle();
    }

    @Step("Выйти из системы")
    public void logout() {
        clickElement(menuButton, "Menu button");
        wait.until(driver -> logoutLink.isDisplayed());
        clickElement(logoutLink, "Logout link");
    }

    @Step("Получить текущий URL")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}