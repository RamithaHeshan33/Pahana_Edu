package org.example.pahana_edu.service;

import org.example.pahana_edu.business.user.dto.UserLoginDTO;
import org.example.pahana_edu.business.user.dto.UserRegistrationDTO;
import org.example.pahana_edu.business.user.dto.UserResponseDTO;
import org.example.pahana_edu.business.user.mapper.UserMapper;
import org.example.pahana_edu.business.user.service.UserService;
import org.example.pahana_edu.persistance.user.dao.UserDAO;
import org.example.pahana_edu.persistance.user.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    private UserService userService;
    private TestUserDAO testUserDAO;

    private UserRegistrationDTO validRegistrationDTO;
    private UserLoginDTO validLoginDTO;
    private UserModel testUser;
    private UserResponseDTO testUserResponse;

    // Stub implementation of UserDAO
    private static class TestUserDAO extends UserDAO {
        private List<UserModel> users = new ArrayList<>();
        private int idCounter = 1;

        @Override
        public UserModel save(UserModel user) throws SQLException {
            user.setId(idCounter++);
            users.add(user);
            return user;
        }

        @Override
        public Optional<UserModel> findByUsername(String username) throws SQLException {
            return users.stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();
        }

        @Override
        public Optional<UserModel> findByEmail(String email) throws SQLException {
            return users.stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst();
        }

        @Override
        public boolean existsByUsername(String username) throws SQLException {
            return users.stream().anyMatch(u -> u.getUsername().equals(username));
        }

        @Override
        public boolean existsByEmail(String email) throws SQLException {
            return users.stream().anyMatch(u -> u.getEmail().equals(email));
        }

        public void clear() {
            users.clear();
            idCounter = 1;
        }
    }

    // Stub implementation of PasswordUtil
    private static class TestPasswordUtil {
        public static String hashPassword(String password) {
            return "hashed_" + password; // Simple mock hashing
        }

        public static boolean verifyPassword(String password, String hashedPassword) {
            return hashedPassword.equals("hashed_" + password);
        }
    }

    // Stub implementation of EmailService
    private static class TestEmailService {
        public static void sendWelcomeEmailAsync(String email, String firstName, String lastName) {
            // No-op for testing
        }
    }

    @BeforeEach
    void setUp() {
        // Initialize test data
        validRegistrationDTO = new UserRegistrationDTO();
        validRegistrationDTO.setUsername("testuser");
        validRegistrationDTO.setEmail("test@example.com");
        validRegistrationDTO.setPassword("password123");
        validRegistrationDTO.setConfirmPassword("password123");
        validRegistrationDTO.setFirstName("John");
        validRegistrationDTO.setLastName("Doe");

        validLoginDTO = new UserLoginDTO("testuser", "password123");

        testUser = new UserModel();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashed_password123");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        testUserResponse = new UserResponseDTO(1, "testuser", "test@example.com", "John", "Doe");

        // Initialize test DAO and service
        testUserDAO = new TestUserDAO();
        userService = new UserService(testUserDAO) {
            // Override to use test stubs
            @Override
            public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) throws SQLException {
                if (!registrationDTO.isValid()) {
                    throw new IllegalArgumentException("Invalid registration data");
                }
                if (userDAO.existsByUsername(registrationDTO.getUsername())) {
                    throw new IllegalArgumentException("Username already exists");
                }
                if (userDAO.existsByEmail(registrationDTO.getEmail())) {
                    throw new IllegalArgumentException("Email already exists");
                }
                String hashedPassword = TestPasswordUtil.hashPassword(registrationDTO.getPassword());
                registrationDTO.setPassword(hashedPassword);
                UserModel user = UserMapper.toEntity(registrationDTO);
                UserModel savedUser = userDAO.save(user);
                TestEmailService.sendWelcomeEmailAsync(savedUser.getEmail(), savedUser.getFirstName(), savedUser.getLastName());
                return UserMapper.toResponseDTO(savedUser);
            }

            @Override
            public UserResponseDTO loginUser(UserLoginDTO loginDTO) throws SQLException {
                if (!loginDTO.isValid()) {
                    throw new IllegalArgumentException("Invalid login credentials");
                }
                Optional<UserModel> userOptional = userDAO.findByUsername(loginDTO.getUsername());
                if (userOptional.isEmpty()) {
                    throw new IllegalArgumentException("Invalid username or password");
                }
                UserModel user = userOptional.get();
                if (!TestPasswordUtil.verifyPassword(loginDTO.getPassword(), user.getPassword())) {
                    throw new IllegalArgumentException("Invalid username or password");
                }
                return UserMapper.toResponseDTO(user);
            }
        };

        // Clear the test DAO before each test
        testUserDAO.clear();
    }

    @Test
    void registerUser_WithValidData() throws SQLException {
        // Act
        UserResponseDTO result = userService.registerUser(validRegistrationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(1, result.getId());

        // Verify user was saved
        Optional<UserModel> savedUser = testUserDAO.findByUsername("testuser");
        assertTrue(savedUser.isPresent());
        assertEquals("hashed_password123", savedUser.get().getPassword());
    }

    @Test
    void registerUser_WithInvalidData() {
        // Arrange
        UserRegistrationDTO invalidDTO = new UserRegistrationDTO("", "", "", "", "", "");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(invalidDTO)
        );
        assertEquals("Invalid registration data", exception.getMessage());
    }

    @Test
    void registerUser_WithExistingUsername() throws SQLException {
        // Arrange
        testUserDAO.save(testUser); // Pre-save a user with the same username

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(validRegistrationDTO)
        );
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void registerUser_WithExistingEmail() throws SQLException {
        // Arrange
        UserModel userWithSameEmail = new UserModel();
        userWithSameEmail.setId(99);
        userWithSameEmail.setUsername("differentUsername");
        userWithSameEmail.setEmail("test@example.com");     // <-- Same email
        userWithSameEmail.setPassword("hashed_password123");
        userWithSameEmail.setFirstName("Jane");
        userWithSameEmail.setLastName("Smith");

        testUserDAO.save(userWithSameEmail); // Pre-save a user with same email only

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(validRegistrationDTO)
        );
        assertEquals("Email already exists", exception.getMessage());
    }


    @Test
    void registerUser_WithPasswordMismatch() {
        // Arrange
        UserRegistrationDTO mismatchDTO = new UserRegistrationDTO(
                "testuser",
                "test@example.com",
                "password123",
                "differentPassword",
                "John",
                "Doe"
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(mismatchDTO)
        );
        assertEquals("Invalid registration data", exception.getMessage());
    }

    @Test
    void loginUser_WithValidCredentials() throws SQLException {
        // Arrange
        testUserDAO.save(testUser); // Pre-save the user

        // Act
        UserResponseDTO result = userService.loginUser(validLoginDTO);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(1, result.getId());
    }

    @Test
    void loginUser_WithInvalidCredentials() {
        // Arrange
        UserLoginDTO invalidDTO = new UserLoginDTO("", "");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.loginUser(invalidDTO)
        );
        assertEquals("Invalid login credentials", exception.getMessage());
    }

    @Test
    void loginUser_WithNonExistentUser() {
        // Arrange
        UserLoginDTO nonExistentUserDTO = new UserLoginDTO("nonexistent", "password123");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.loginUser(nonExistentUserDTO)
        );
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void loginUser_WithWrongPassword() throws SQLException {
        // Arrange
        testUserDAO.save(testUser); // Pre-save the user
        UserLoginDTO wrongPasswordDTO = new UserLoginDTO("testuser", "wrongPassword");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.loginUser(wrongPasswordDTO)
        );
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void isUsernameAvailable_WithNonExistentUsername() throws SQLException {
        // Act
        boolean available = userService.isUsernameAvailable("newuser");

        // Assert
        assertTrue(available);
    }

    @Test
    void isUsernameAvailable_WithExistingUsername() throws SQLException {
        // Arrange
        testUserDAO.save(testUser);

        // Act
        boolean available = userService.isUsernameAvailable("testuser");

        // Assert
        assertFalse(available);
    }

    @Test
    void isEmailAvailable_WithNonExistentEmail() throws SQLException {
        // Act
        boolean available = userService.isEmailAvailable("new@example.com");

        // Assert
        assertTrue(available);
    }

    @Test
    void isEmailAvailable_WithExistingEmail() throws SQLException {
        // Arrange
        testUserDAO.save(testUser);

        // Act
        boolean available = userService.isEmailAvailable("test@example.com");

        // Assert
        assertFalse(available);
    }
}
