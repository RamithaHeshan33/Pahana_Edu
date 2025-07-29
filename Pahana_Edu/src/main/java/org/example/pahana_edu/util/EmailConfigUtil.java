package org.example.pahana_edu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailConfigUtil {
    private static final Logger LOGGER = Logger.getLogger(EmailConfigUtil.class.getName());
    private static Properties emailProperties;
    
    static {
        loadEmailProperties();
    }
    
    private static void loadEmailProperties() {
        emailProperties = new Properties();
        try (InputStream input = EmailConfigUtil.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (input != null) {
                emailProperties.load(input);
                LOGGER.info("Email properties loaded successfully");
            } else {
                LOGGER.warning("email.properties file not found, using default values");
                setDefaultProperties();
            }
        } catch (IOException e) {
            LOGGER.severe("Error loading email properties: " + e.getMessage());
            setDefaultProperties();
        }
    }
    
    private static void setDefaultProperties() {
        emailProperties.setProperty("smtp.host", "smtp.gmail.com");
        emailProperties.setProperty("smtp.port", "587");
        emailProperties.setProperty("smtp.auth", "true");
        emailProperties.setProperty("smtp.starttls.enable", "true");
        emailProperties.setProperty("email.username", "your-email@gmail.com");
        emailProperties.setProperty("email.password", "your-app-password");
        emailProperties.setProperty("email.from", "noreply@pahanaedu.com");
        emailProperties.setProperty("email.from.name", "Pahana Edu");
        emailProperties.setProperty("email.welcome.subject", "Welcome to Pahana Edu!");
    }
    
    public static String getProperty(String key) {
        return emailProperties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return emailProperties.getProperty(key, defaultValue);
    }
    
    public static Properties getEmailProperties() {
        return emailProperties;
    }
}