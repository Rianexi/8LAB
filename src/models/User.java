package models;

import java.io.Serializable;

/**@author Rianexi
Класс пользователя
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id; // Добавлено поле id
    private final String login;
    private final String passwordHash;

    public User(int id, String login, String passwordHash) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public User(String login, String passwordHash) {
        this(0, login, passwordHash); // id=0 для случаев, когда ID еще не известен
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", login='" + login + "'}";
    }
}