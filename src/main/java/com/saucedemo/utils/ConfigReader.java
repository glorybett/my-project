package com.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Unable to find config.properties");
                // Set default values
                properties.setProperty("browser", "chrome");
                properties.setProperty("headless", "false");
                properties.setProperty("timeout", "10");
                properties.setProperty("base.url", "https://www.saucedemo.com/");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    public static int getTimeout() {
        return Integer.parseInt(getProperty("timeout"));
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }
}