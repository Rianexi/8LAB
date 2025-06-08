package utility;

import models.User;
import models.Worker;

/**@author Rianexi
Интерфейс для взаимодействия с пользователем
 */
public interface Console {
    void print(String text);
    void println(String text);
    String readln();
    Worker readWorker(int id, User user);
}