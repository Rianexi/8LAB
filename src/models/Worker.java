package models;

import java.io.Serializable;
import java.util.Date;

/**@author Rianexi
Класс работника, хранимого в коллекции
 */
public class Worker implements Comparable<Worker>, Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private Integer salary;
    private Position position;
    private Status status;
    private Person person;
    private User owner; // Пользователь, создавший объект

    /**Конструктор для создания объекта Worker
     @param id Уникальный идентификатор
     @param name Имя работника
     @param coordinates Координаты работника
     @param creationDate Дата создания
     @param salary Зарплата
     @param position Должность
     @param status Статус
     @param person Личность работника
     @param owner Пользователь, создавший объект
     */
    public Worker(int id, String name, Coordinates coordinates, Date creationDate, Integer salary,
                  Position position, Status status, Person person, User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.position = position;
        this.status = status;
        this.person = person;
        this.owner = owner;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public Integer getSalary() { return salary; }
    public void setSalary(Integer salary) { this.salary = salary; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    @Override
    public int compareTo(Worker other) {
        return Integer.compare(this.salary, other.salary);
    }

    /**Возвращает строковое представление объекта
     @return Строковое представление объекта Worker
     */
    @Override
    public String toString() {
        return String.format("Worker{id=%d, name='%s', coordinates=%s, creationDate=%s, salary=%d, position=%s, status=%s, person=%s, owner=%s}",
                id, name, coordinates, creationDate, salary, position, status, person, owner != null ? owner.getLogin() : "null");
    }
}