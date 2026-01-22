package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    protected void clickElement(WebElement element, String elementName) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        logger.info("Clicking on: " + elementName);
        element.click();
    }

    protected void enterText(WebElement element, String text, String elementName) {
        wait.until(ExpectedConditions.visibilityOf(element));
        logger.info("Entering text '" + text + "' into: " + elementName);
        element.clear();
        element.sendKeys(text);
    }

    protected String getElementText(WebElement element, String elementName) {
        wait.until(ExpectedConditions.visibilityOf(element));
        String text = element.getText();
        logger.info("Got text '" + text + "' from: " + elementName);
        return text;
    }

    protected boolean isElementDisplayed(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            logger.info(elementName + " is displayed");
            return true;
        } catch (Exception e) {
            logger.warn(elementName + " is not displayed");
            return false;
        }
    }
}