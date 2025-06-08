package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**@author Rianexi
Утилита для хэширования паролей
 */
public class PasswordHasher {
    /**Хэширует пароль с использованием алгоритма MD2
     @param password Пароль для хэширования
     @return Хэшированная строка
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка хэширования пароля: " + e.getMessage(), e);
        }
    }
}