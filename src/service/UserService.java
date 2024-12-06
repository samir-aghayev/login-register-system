package service;

import dao.model.User;
import dao.repository.UserRepository;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public boolean registerUser(User user) {
        if (user == null) {
            System.err.println("The data was not entered correctly");
            return false;
        }
        if (isEmailRegistered(user.getEmail())) {
            System.err.println("Email already registered");
            return false;
        }
        if (!isValidUser(user)) {
            System.err.println("The email does not comply with the rules");
            return false;
        }
        user.setPassword(encryptPassword(user.getPassword()));
        userRepository.getUsers().add(user);
        System.out.println("User registered successfully");
        return true;
    }

    public boolean loginUser(String email, String password) {
        User user = getUserDetails(email);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }
        if (verifyUserCredentials(password, user)) {
            return true;
        }
        System.err.println("Email Or Password Is Wrong");
        return false;
    }

    private boolean isEmailRegistered(String email) {
        return userRepository.getUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean verifyUserCredentials(String password, User user) {
        return decryptPassword(password, user.getPassword());
    }

    private boolean isValidUser(User user) {
        String email = user.getEmail();
        return email != null && !email.isBlank() && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public User getUserDetails(String email) {
        return userRepository.getUsers().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    private String encryptPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        Argon2Parameters parameters = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withIterations(3)
                .withMemoryAsKB(64 * 1024)
                .withParallelism(4)
                .withSalt(salt)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);
        byte[] hash = new byte[32];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), hash);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);

    }

    private boolean decryptPassword(String password, String storedHash) {
        String[] parts = storedHash.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

        Argon2Parameters parameters = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withIterations(3)
                .withMemoryAsKB(64 * 1024)
                .withParallelism(4)
                .withSalt(salt)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);

        byte[] actualHash = new byte[32];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), actualHash);
        return java.util.Arrays.equals(expectedHash, actualHash);

    }

}

