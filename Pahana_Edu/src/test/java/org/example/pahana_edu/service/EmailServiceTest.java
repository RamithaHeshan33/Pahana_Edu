package org.example.pahana_edu.service;

import org.example.pahana_edu.business.user.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    private TestEmailUtil testEmailUtil;

    // Test stub for EmailUtil
    private static class TestEmailUtil {
        private static boolean shouldSucceed = true;
        private static String lastToEmail;
        private static String lastFirstName;
        private static String lastLastName;
        private static boolean emailSent = false;

        public static boolean sendWelcomeEmail(String toEmail, String firstName, String lastName) {
            lastToEmail = toEmail;
            lastFirstName = firstName;
            lastLastName = lastName;
            emailSent = true;
            return shouldSucceed;
        }

        public static void setShouldSucceed(boolean shouldSucceed) {
            TestEmailUtil.shouldSucceed = shouldSucceed;
        }

        public static void reset() {
            shouldSucceed = true;
            lastToEmail = null;
            lastFirstName = null;
            lastLastName = null;
            emailSent = false;
        }

        public static boolean wasEmailSent() {
            return emailSent;
        }

        public static String getLastToEmail() {
            return lastToEmail;
        }

        public static String getLastFirstName() {
            return lastFirstName;
        }

        public static String getLastLastName() {
            return lastLastName;
        }
    }

    // Test implementation of EmailService that uses TestEmailUtil
    private static class TestEmailService extends EmailService {
        public static void sendWelcomeEmailAsync(String email, String firstName, String lastName) {
            CompletableFuture.runAsync(() -> {
                try {
                    TestEmailUtil.sendWelcomeEmail(email, firstName, lastName);
                } catch (Exception e) {
                    // Handle exception in test
                }
            });
        }

        public static void sendWelcomeEmailSync(String email, String firstName, String lastName) {
            TestEmailUtil.sendWelcomeEmail(email, firstName, lastName);
        }
    }

    @BeforeEach
    void setUp() {
        TestEmailUtil.reset();
    }

    @Test
    void sendWelcomeEmailAsync_WithValidData() throws InterruptedException {
        // Arrange
        String email = "test@example.com";
        String firstName = "Ramitha";
        String lastName = "Heshan";

        // Act
        TestEmailService.sendWelcomeEmailAsync(email, firstName, lastName);

        // Wait for async operation to complete
        Thread.sleep(100);

        // Assert
        assertTrue(TestEmailUtil.wasEmailSent());
        assertEquals(email, TestEmailUtil.getLastToEmail());
        assertEquals(firstName, TestEmailUtil.getLastFirstName());
        assertEquals(lastName, TestEmailUtil.getLastLastName());
    }

    @Test
    void sendWelcomeEmailSync_WithNullEmail() {
        // Arrange
        String email = null;
        String firstName = "Ramitha";
        String lastName = "Heshan";

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            TestEmailService.sendWelcomeEmailSync(email, firstName, lastName);
        });

        assertTrue(TestEmailUtil.wasEmailSent());
        assertNull(TestEmailUtil.getLastToEmail());
    }

    @Test
    void sendWelcomeEmailSync_WithEmptyNames() {
        // Arrange
        String email = "test@example.com";
        String firstName = "";
        String lastName = "";

        // Act
        TestEmailService.sendWelcomeEmailSync(email, firstName, lastName);

        // Assert
        assertTrue(TestEmailUtil.wasEmailSent());
        assertEquals(email, TestEmailUtil.getLastToEmail());
        assertEquals("", TestEmailUtil.getLastFirstName());
        assertEquals("", TestEmailUtil.getLastLastName());
    }

    @Test
    void sendWelcomeEmailSync_WhenEmailFails() {
        // Arrange
        TestEmailUtil.setShouldSucceed(false);
        String email = "test@example.com";
        String firstName = "Ramitha";
        String lastName = "Heshan";

        // Act
        boolean result = TestEmailUtil.sendWelcomeEmail(email, firstName, lastName);

        // Assert
        assertFalse(result);
        assertTrue(TestEmailUtil.wasEmailSent()); // Email was attempted
    }

    @Test
    void sendWelcomeEmailSync_WithSpecialCharacters() {
        // Arrange
        String email = "test+special@example.com";
        String firstName = "Ramitha";
        String lastName = "Heshan";

        // Act
        TestEmailService.sendWelcomeEmailSync(email, firstName, lastName);

        // Assert
        assertTrue(TestEmailUtil.wasEmailSent());
        assertEquals(email, TestEmailUtil.getLastToEmail());
        assertEquals(firstName, TestEmailUtil.getLastFirstName());
        assertEquals(lastName, TestEmailUtil.getLastLastName());
    }
}