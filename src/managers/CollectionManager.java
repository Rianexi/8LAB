package managers;

import models.Worker;
import models.User;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

/**
 * @author Rianexi
 * Менеджер коллекции работников
 */
public class CollectionManager {
    private final LinkedList<Worker> collection;
    private final Date initializationDate;
    private final DatabaseManager databaseManager;

    /**
     * Конструктор для инициализации менеджера коллекции
     * @param databaseManager Менеджер базы данных для загрузки и сохранения коллекции
     */
    public CollectionManager(DatabaseManager databaseManager) {
        this.collection = new LinkedList<>();
        this.initializationDate = new Date();
        this.databaseManager = databaseManager;
        loadCollection();
    }

    private void loadCollection() {
        try {
            synchronized (collection) {
                collection.addAll(databaseManager.loadWorkers());
            }
        } catch (SQLException e) {
            System.err.println("Не удалось загрузить коллекцию: " + e.getMessage());
        }
    }

    /**
     * Генерирует уникальный идентификатор для нового работника
     * @return Уникальный идентификатор (генерируется базой данных)
     */
    public int generateId() {
        // ID генерируется базой данных через sequence
        return 0; // Не используется, так как ID будет устанавливаться при добавлении в БД
    }

    /**
     * Добавляет работника в коллекцию
     * @param worker Работник для добавления
     * @param user Пользователь, добавляющий работника
     * @return true, если добавление успешно
     */
    public synchronized boolean add(Worker worker, User user) {
        try {
            worker.setOwner(user);
            if (databaseManager.addWorker(worker)) {
                synchronized (collection) {
                    collection.add(worker);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Ошибка добавления работника: " + e.getMessage());
            return false;
        }
    }

    /**
     * Удаляет работника по идентификатору
     * @param id Идентификатор работника для удаления
     * @param user Пользователь, выполняющий удаление
     * @return true, если удаление успешно
     */
    public synchronized boolean removeById(int id, User user) {
        try {
            if (databaseManager.removeById(id, user)) {
                synchronized (collection) {
                    collection.removeIf(w -> w.getId() == id && w.getOwner().getLogin().equals(user.getLogin()));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Ошибка удаления работника: " + e.getMessage());
            return false;
        }
    }

    /**
     * Очищает коллекцию для данного пользователя
     * @param user Пользователь, очищающий коллекцию
     * @return Количество удаленных элементов
     */
    public synchronized int clear(User user) {
        try {
            if (databaseManager.clearWorkers(user)) {
                synchronized (collection) {
                    int count = (int) collection.stream()
                            .filter(w -> w.getOwner().getLogin().equals(user.getLogin()))
                            .count();
                    collection.removeIf(w -> w.getOwner().getLogin().equals(user.getLogin()));
                    return count;
                }
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("Ошибка очистки коллекции: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Возвращает работника по идентификатору
     * @param id Идентификатор работника
     * @return Объект Worker или null, если не найден
     */
    public synchronized Worker getById(int id) {
        synchronized (collection) {
            return collection.stream().filter(w -> w.getId() == id).findFirst().orElse(null);
        }
    }

    /**
     * Возвращает коллекцию работников
     * @return Список работников
     */
    public synchronized LinkedList<Worker> getCollection() {
        synchronized (collection) {
            return new LinkedList<>(collection);
        }
    }

    /**
     * Возвращает дату инициализации коллекции
     * @return Дата инициализации
     */
    public Date getInitializationDate() {
        return initializationDate;
    }

    /**
     * Обновляет работника
     * @param worker Работник для обновления
     * @param user Пользователь, выполняющий обновление
     * @return true, если обновление успешно
     */
    public synchronized boolean update(Worker worker, User user) {
        try {
            if (databaseManager.updateWorker(worker, user)) {
                synchronized (collection) {
                    collection.removeIf(w -> w.getId() == worker.getId());
                    collection.add(worker);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Ошибка обновления работника: " + e.getMessage());
            return false;
        }
    }

    /**
     * Удаляет всех работников с заданной зарплатой
     * @param salary Зарплата
     * @param user Пользователь
     * @return Количество удаленных элементов
     */
    public synchronized int removeBySalary(int salary, User user) {
        try {
            if (databaseManager.removeBySalary(salary, user)) {
                synchronized (collection) {
                    int count = (int) collection.stream()
                            .filter(w -> w.getSalary().equals(salary) && w.getOwner().getLogin().equals(user.getLogin()))
                            .count();
                    collection.removeIf(w -> w.getSalary().equals(salary) && w.getOwner().getLogin().equals(user.getLogin()));
                    return count;
                }
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("Ошибка удаления по зарплате: " + e.getMessage());
            return 0;
        }
    }
}