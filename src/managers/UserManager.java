package managers;

import models.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class UserManager {
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());
    private final DatabaseManager databaseManager;

    public UserManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public User registerUser(String login, String password) {
        try {
            String passwordHash = hashPassword(password);
            return databaseManager.registerUser(login, passwordHash);
        } catch (Exception e) {
            logger.severe("Ошибка регистрации: " + e.getMessage());
            return null;
        }
    }

    public User loginUser(String login, String password) {
        try {
            String passwordHash = hashPassword(password);
            return databaseManager.loginUser(login, passwordHash);
        } catch (Exception e) {
            logger.severe("Ошибка авторизации: " + e.getMessage());
            return null;
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}