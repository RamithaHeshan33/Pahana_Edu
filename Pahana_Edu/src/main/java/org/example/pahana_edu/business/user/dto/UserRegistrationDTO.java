package org.example.pahana_edu.business.user.dto;

public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

    // Default constructor
    public UserRegistrationDTO() {}

    // Constructor
    public UserRegistrationDTO(String username, String email, String password,
                               String confirmPassword, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Validation method
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() &&
                password != null && !password.trim().isEmpty() &&
                confirmPassword != null && password.equals(confirmPassword) &&
                firstName != null && !firstName.trim().isEmpty() &&
                lastName != null && !lastName.trim().isEmpty();
    }
}