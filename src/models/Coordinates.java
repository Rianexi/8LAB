package models;

import java.io.Serializable;

/**@author Rianexi
Класс координат работника
 */
public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private float x;
    private long y;

    /**Конструктор для создания объекта Coordinates
     @param x Координата X (> -817)
     @param y Координата Y (> -166, не null)
     @throws IllegalArgumentException Если x <= -817 или y <= -166 или y == null
     */
    public Coordinates(float x, Long y) {
        if (x <= -817) throw new IllegalArgumentException("x должен быть больше -817");
        if (y == null || y <= -166) throw new IllegalArgumentException("y должен быть больше -166 и не null");
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public Long getY() { return y; }

    /**Возвращает строковое представление объекта
     @return Строковое представление объекта Coordinates
     */
    @Override
    public String toString() {
        return String.format("Coordinates{x=%.1f, y=%d}", x, y);
    }
}