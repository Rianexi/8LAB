package models;

import java.io.Serializable;

/**@author Rianexi
Перечисление статусов работника
 */
public enum Status implements Serializable {
    HIRED,
    FIRED,
    RECOMMENDED_FOR_PROMOTION,
    REGULAR,
    PROBATION;
}