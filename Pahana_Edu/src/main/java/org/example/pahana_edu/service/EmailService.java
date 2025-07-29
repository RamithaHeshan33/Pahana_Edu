package org.example.pahana_edu.service;

import org.example.pahana_edu.util.EmailUtil;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    public static void sendWelcomeEmailAsync(String email, String firstName, String lastName) {
        CompletableFuture.runAsync(() -> {
            try {
                boolean emailSent = EmailUtil.sendWelcomeEmail(email, firstName, lastName);
                if (emailSent) {
                    LOGGER.info("Welcome email sent successfully to: " + email);
                } else {
                    LOGGER.warning("Failed to send welcome email to: " + email);
                }
            } catch (Exception e) {
                LOGGER.severe("Error sending welcome email to " + email + ": " + e.getMessage());
            }
        });
    }
}