package org.example.pahana_edu.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailUtil {
    private static final Logger LOGGER = Logger.getLogger(EmailUtil.class.getName());

    /**
     * welcome email subject
     */
    public static boolean sendWelcomeEmail(String toEmail, String firstName, String lastName) {
        String subject = EmailConfigUtil.getProperty("email.welcome.subject", "Welcome to Pahana Edu!");
        String content = buildWelcomeEmailContent(firstName, lastName);
        return sendEmail(toEmail, subject, content);
    }

    /**
     * Send an email
     */
    public static boolean sendEmail(String toEmail, String subject, String content) {
        try {
            // Get email configuration
            Properties props = new Properties();
            props.put("mail.smtp.host", EmailConfigUtil.getProperty("smtp.host"));
            props.put("mail.smtp.port", EmailConfigUtil.getProperty("smtp.port"));
            props.put("mail.smtp.auth", EmailConfigUtil.getProperty("smtp.auth"));
            props.put("mail.smtp.starttls.enable", EmailConfigUtil.getProperty("smtp.starttls.enable"));

            // Create authenticator
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            EmailConfigUtil.getProperty("email.username"),
                            EmailConfigUtil.getProperty("email.password")
                    );
                }
            };

            // Create session
            Session session = Session.getInstance(props, authenticator);

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(
                    EmailConfigUtil.getProperty("email.from"),
                    EmailConfigUtil.getProperty("email.from.name")
            ));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);

            LOGGER.info("Email sent successfully to: " + toEmail);
            return true;

        } catch (Exception e) {
            LOGGER.severe("Failed to send email to " + toEmail + ": " + e.getMessage());
            return false;
        }
    }

    private static String buildWelcomeEmailContent(String firstName, String lastName) {
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Welcome to Pahana Edu</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                "        .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 0; }" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }" +
                "        .header h1 { margin: 0; font-size: 28px; }" +
                "        .header .logo { font-size: 40px; margin-bottom: 10px; }" +
                "        .content { padding: 30px; }" +
                "        .welcome-message { font-size: 18px; margin-bottom: 20px; color: #2c3e50; }" +
                "        .features { background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; }" +
                "        .feature-item { margin: 10px 0; padding: 10px 0; border-bottom: 1px solid #e9ecef; }" +
                "        .feature-item:last-child { border-bottom: none; }" +
                "        .feature-icon { color: #667eea; font-weight: bold; margin-right: 10px; }" +
                "        .cta-button { display: inline-block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 12px 30px; text-decoration: none; border-radius: 25px; margin: 20px 0; font-weight: bold; }" +
                "        .footer { background-color: #2c3e50; color: white; padding: 20px; text-align: center; font-size: 14px; }" +
                "        .social-links { margin: 15px 0; }" +
                "        .social-links a { color: white; text-decoration: none; margin: 0 10px; }" +
                "        @media (max-width: 600px) { .container { width: 100% !important; } .content { padding: 20px !important; } }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "        <div class='logo'>📘</div>" +
                "        <h1>Welcome to Pahana Edu</h1>" +
                "    </div>" +
                "    <div class='content'>" +
                "        <div class='welcome-message'>" +
                "            <h2>Hello " + firstName + " " + lastName + "!</h2>" +
                "            <p>You've added as an <strong>Administrator</strong> for the <strong>Pahana Edu Bookshop Billing System</strong>.</p>" +
                "            <p>This system is designed to help you efficiently manage day-to-day operations including inventory, billing, and customer accounts.</p>" +
                "        </div>" +
                "        <div class='features'>" +
                "            <h3 style='color: #2c3e50; margin-top: 0;'>Key Admin Features:</h3>" +
                "            <div class='feature-item'>" +
                "                <span class='feature-icon'>📦</span>" +
                "                <strong>Inventory Management:</strong> Add, update, and monitor book stock levels." +
                "            </div>" +
                "            <div class='feature-item'>" +
                "                <span class='feature-icon'>🧾</span>" +
                "                <strong>Billing:</strong> Generate bills, apply discounts, and manage transactions securely." +
                "            </div>" +
                "            <div class='feature-item'>" +
                "                <span class='feature-icon'>👥</span>" +
                "                <strong>Customer Accounts:</strong> Add customer accounts" +
                "            </div>" +
                "            <div class='feature-item'>" +
                "                <span class='feature-icon'>📊</span>" +
                "                <strong>Analytics:</strong> View Transaction History." +
                "            </div>" +
                "        </div>" +
                "        <div style='text-align: center;'>" +
                "            <p>Get started with your admin dashboard today.</p>" +
                "        </div>" +
                "       </div>" +
                "       <div class='footer'>" +
                "           <p><strong>Pahana Edu Billing System</strong> – Powering Your Bookshop’s Operations</p>" +
                "           <div class='social-links'>" +
                "               <a href='#'>📧 Contact Support</a>" +
                "               <a href='#'>📄 Documentation</a>" +
                "               <a href='#'>🌐 Visit Website</a>" +
                "           </div>" +
                "           <p style='font-size: 12px; margin-top: 15px; color: #bdc3c7;'>" +
                "               © 2025 Pahana Edu. All rights reserved.<br>" +
                "               This message was sent because you were added as an admin on Pahana Edu Billing System." +
                "           </p>" +
                "       </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}