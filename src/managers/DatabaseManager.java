package managers;

import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**@author Rianexi
Менеджер для работы с базой данных PostgreSQL
 */
public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres"; // Измените на ваш пароль от PostgreSQL

    public DatabaseManager() {
        try {
            // Регистрация драйвера JDBC
            Class.forName("org.postgresql.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            logger.severe("Драйвер PostgreSQL не найден: " + e.getMessage());
            throw new RuntimeException("Не удалось загрузить драйвер PostgreSQL", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Создание таблицы users
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    login VARCHAR(255) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL
                );
                """;
            stmt.executeUpdate(createUsersTable);

            // Создание таблицы workers
            String createWorkersTable = """
                CREATE TABLE IF NOT EXISTS workers (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    coord_x FLOAT NOT NULL,
                    coord_y BIGINT NOT NULL,
                    creation_date TIMESTAMP NOT NULL,
                    salary INTEGER NOT NULL CHECK (salary > 0),
                    position VARCHAR(50) CHECK (position IN ('MANAGER', 'HEAD_OF_DEPARTMENT', 'POSITIONER')),
                    status VARCHAR(50) CHECK (status IN ('HIRED', 'FIRED', 'RECOMMENDED_FOR_PROMOTION', 'REGULAR', 'PROBATION')),
                    person_weight FLOAT CHECK (person_weight > 0),
                    person_passport_id VARCHAR(255),
                    person_hair_color VARCHAR(50) CHECK (person_hair_color IN ('GREEN', 'RED', 'BLACK', 'BLUE', 'YELLOW')),
                    person_nationality VARCHAR(50) CHECK (person_nationality IN ('RUSSIA', 'FRANCE', 'SPAIN', 'CHINA', 'NORTH_KOREA')),
                    owner_id INTEGER NOT NULL REFERENCES users(id)
                );
                """;
            stmt.executeUpdate(createWorkersTable);

            // Проверка существования таблиц
            ResultSet rs = stmt.executeQuery("SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'workers')");
            if (rs.next() && !rs.getBoolean(1)) {
                logger.severe("Таблица workers не существует после попытки создания!");
            }

            logger.info("База данных инициализирована успешно: таблицы users и workers готовы");
        } catch (SQLException e) {
            logger.severe("Ошибка инициализации базы данных: " + e.getMessage());
            throw new RuntimeException("Не удалось инициализировать базу данных", e);
        }
    }

    public User registerUser(String login, String passwordHash) {
        try {
            String sql = "INSERT INTO users (login, password_hash) VALUES (?, ?) RETURNING id";
            try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, passwordHash);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return new User(id, login, passwordHash);
                }
            }
        } catch (SQLException e) {
            logger.severe("Ошибка регистрации пользователя: " + e.getMessage());
        }
        return null;
    }

    public User loginUser(String login, String passwordHash) throws SQLException {
        String sql = "SELECT id, login, password_hash FROM users WHERE login = ? AND password_hash = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("Пользователь " + login + " успешно авторизован, ID: " + rs.getInt("id"));
                return new User(rs.getInt("id"), rs.getString("login"), rs.getString("password_hash")); // Строка 103
            }
            logger.warning("Не удалось авторизовать пользователя " + login + ": неверный логин или пароль");
            return null;
        } catch (SQLException e) {
            logger.severe("Ошибка авторизации пользователя " + login + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean addWorker(Worker worker) throws SQLException {
        String sql = """
            INSERT INTO workers (name, coord_x, coord_y, creation_date, salary, position, status,
                                person_weight, person_passport_id, person_hair_color, person_nationality, owner_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            try {
                // Проверка существования пользователя
                try (PreparedStatement checkUser = conn.prepareStatement("SELECT id FROM users WHERE id = ?")) {
                    checkUser.setInt(1, worker.getOwner().getId());
                    ResultSet userRs = checkUser.executeQuery();
                    if (!userRs.next()) {
                        logger.severe("Пользователь с ID " + worker.getOwner().getId() + " не существует!");
                        return false;
                    }
                }

                stmt.setString(1, worker.getName());
                stmt.setFloat(2, worker.getCoordinates().getX());
                stmt.setLong(3, worker.getCoordinates().getY());
                stmt.setTimestamp(4, new Timestamp(worker.getCreationDate().getTime()));
                stmt.setInt(5, worker.getSalary());
                stmt.setString(6, worker.getPosition() != null ? worker.getPosition().name() : null);
                stmt.setString(7, worker.getStatus() != null ? worker.getStatus().name() : null);
                if (worker.getPerson() != null) {
                    stmt.setFloat(8, worker.getPerson().getWeight());
                    stmt.setString(9, worker.getPerson().getPassportID());
                    stmt.setString(10, worker.getPerson().getHairColor() != null ? worker.getPerson().getHairColor().name() : null);
                    stmt.setString(11, worker.getPerson().getNationality() != null ? worker.getPerson().getNationality().name() : null);
                } else {
                    stmt.setNull(8, Types.FLOAT);
                    stmt.setNull(9, Types.VARCHAR);
                    stmt.setNull(10, Types.VARCHAR);
                    stmt.setNull(11, Types.VARCHAR);
                }
                stmt.setInt(12, worker.getOwner().getId());
                
                logger.info("Попытка добавления работника: " + worker);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    worker.setId(id);
                    logger.info("Работник с ID " + id + " добавлен в базу");
                    return true;
                }
                logger.warning("Не удалось получить ID после вставки работника");
                return false;
            } catch (SQLException e) {
                logger.severe("Ошибка при установке параметров запроса: " + e.getMessage());
                logger.severe("SQL State: " + e.getSQLState());
                logger.severe("Error Code: " + e.getErrorCode());
                throw e;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка добавления работника: " + e.getMessage());
            logger.severe("SQL State: " + e.getSQLState());
            logger.severe("Error Code: " + e.getErrorCode());
            throw e;
        }
    }

    public boolean removeById(int id, User user) throws SQLException {
        String sql = "DELETE FROM workers WHERE id = ? AND owner_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            stmt.setInt(1, id);
            stmt.setInt(2, user.getId());
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            if (success) {
                logger.info("Работник с ID " + id + " удален пользователем " + user.getLogin());
            }
            return success;
        } catch (SQLException e) {
            logger.severe("Ошибка удаления работника с ID " + id + ": " + e.getMessage());
            throw e;
        }
    }

    public List<Worker> loadWorkers() throws SQLException {
        List<Worker> workers = new ArrayList<>();
        String sql = """
            SELECT w.*, u.id AS user_id, u.login, u.password_hash
            FROM workers w
            JOIN users u ON w.owner_id = u.id
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User owner = new User(rs.getInt("user_id"), rs.getString("login"), rs.getString("password_hash"));
                Coordinates coordinates = new Coordinates(rs.getFloat("coord_x"), rs.getLong("coord_y"));
                Position position = rs.getString("position") != null ? Position.valueOf(rs.getString("position")) : null;
                Status status = rs.getString("status") != null ? Status.valueOf(rs.getString("status")) : null;
                Person person = null;
                if (rs.getObject("person_weight") != null) {
                    Color hairColor = rs.getString("person_hair_color") != null ? Color.valueOf(rs.getString("person_hair_color")) : null;
                    Country nationality = rs.getString("person_nationality") != null ? Country.valueOf(rs.getString("person_nationality")) : null;
                    person = new Person(rs.getFloat("person_weight"), rs.getString("person_passport_id"), hairColor, nationality);
                }
                Worker worker = new Worker(
                        rs.getInt("id"),
                        rs.getString("name"),
                        coordinates,
                        rs.getTimestamp("creation_date"),
                        rs.getInt("salary"),
                        position,
                        status,
                        person,
                        owner
                );
                workers.add(worker);
            }
            logger.info("Загружено " + workers.size() + " работников из базы");
            return workers;
        } catch (SQLException e) {
            logger.severe("Ошибка загрузки работников: " + e.getMessage());
            throw e;
        }
    }

    public boolean updateWorker(Worker worker, User user) throws SQLException {
        String sql = """
            UPDATE workers
            SET name = ?, coord_x = ?, coord_y = ?, creation_date = ?, salary = ?,
                position = ?, status = ?, person_weight = ?, person_passport_id = ?,
                person_hair_color = ?, person_nationality = ?
            WHERE id = ? AND owner_id = ?
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            stmt.setString(1, worker.getName());
            stmt.setFloat(2, worker.getCoordinates().getX());
            stmt.setLong(3, worker.getCoordinates().getY());
            stmt.setTimestamp(4, new Timestamp(worker.getCreationDate().getTime()));
            stmt.setInt(5, worker.getSalary());
            stmt.setString(6, worker.getPosition() != null ? worker.getPosition().name() : null);
            stmt.setString(7, worker.getStatus() != null ? worker.getStatus().name() : null);
            if (worker.getPerson() != null) {
                stmt.setFloat(8, worker.getPerson().getWeight());
                stmt.setString(9, worker.getPerson().getPassportID());
                stmt.setString(10, worker.getPerson().getHairColor() != null ? worker.getPerson().getHairColor().name() : null);
                stmt.setString(11, worker.getPerson().getNationality() != null ? worker.getPerson().getNationality().name() : null);
            } else {
                stmt.setNull(8, Types.FLOAT);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            }
            stmt.setInt(12, worker.getId());
            stmt.setInt(13, user.getId());
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            if (success) {
                logger.info("Работник с ID " + worker.getId() + " обновлен пользователем " + user.getLogin());
            }
            return success;
        } catch (SQLException e) {
            logger.severe("Ошибка обновления работника с ID " + worker.getId() + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean clearWorkers(User user) throws SQLException {
        String sql = "DELETE FROM workers WHERE owner_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            stmt.setInt(1, user.getId());
            int rowsAffected = stmt.executeUpdate();
            logger.info("Удалено " + rowsAffected + " работников для пользователя " + user.getLogin());
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Ошибка очистки работников для пользователя " + user.getLogin() + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean removeBySalary(int salary, User user) throws SQLException {
        String sql = "DELETE FROM workers WHERE salary = ? AND owner_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            stmt.setInt(1, salary);
            stmt.setInt(2, user.getId());
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            if (success) {
                logger.info("Удалено " + rowsAffected + " работников с зарплатой " + salary + " пользователем " + user.getLogin());
            }
            return success;
        } catch (SQLException e) {
            logger.severe("Ошибка удаления работников с зарплатой " + salary + ": " + e.getMessage());
            throw e;
        }
    }
}