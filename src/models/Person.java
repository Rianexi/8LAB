package models;

import java.io.Serializable;

/**@author Rianexi
Класс персональных данных
 */
public class Person implements Serializable, Comparable<Person> {
    private static final long serialVersionUID = 1L;
    private float weight;
    private String passportID;
    private Color hairColor;
    private Country nationality;

    /**Конструктор для создания объекта Person
     @param weight Вес личности (> 0)
     @param passportID Номер паспорта (может быть null)
     @param hairColor Цвет волос (может быть null)
     @param nationality Национальность (не null)
     @throws IllegalArgumentException Если weight <= 0
     */
    public Person(float weight, String passportID, Color hairColor, Country nationality) {
        if (weight <= 0) throw new IllegalArgumentException("weight должен быть больше 0");
        this.weight = weight;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }
    public String getPassportID() { return passportID; }
    public void setPassportID(String passportID) { this.passportID = passportID; }
    public Color getHairColor() { return hairColor; }
    public void setHairColor(Color hairColor) { this.hairColor = hairColor; }
    public Country getNationality() { return nationality; }
    public void setNationality(Country nationality) { this.nationality = nationality; }

    @Override
    public int compareTo(Person other) {
        return Float.compare(this.weight, other.weight);
    }

    /**Возвращает строковое представление объекта
     @return Строковое представление объекта Person
     */
    @Override
    public String toString() {
        return "Person{" +
                "weight=" + weight +
                ", passportID='" + passportID + '\'' +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                '}';
    }
}