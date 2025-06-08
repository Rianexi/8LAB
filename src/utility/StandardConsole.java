package utility;

import models.*;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**@author Rianexi
Стандартная консоль для взаимодействия с пользователем
 */
public class StandardConsole implements Console {
    private final Scanner scanner;

    public StandardConsole() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void println(String text) {
        System.out.println(text);
    }

    @Override
    public String readln() {
        return scanner.nextLine();
    }

    @Override
    public Worker readWorker(int id, User user) {
        String name = readString("Введите имя: ", false);
        float coordX = readFloat("Введите X координаты (float > -817): ", -817);
        long coordY = readLong("Введите Y координаты (long > -166): ", -166);
        Date creationDate = readDate("Введите дату создания (YYYY-MM-DD): ");
        int salary = readInt("Введите зарплату (int > 0): ", 0);
        Position position = readPosition("Введите должность (MANAGER, HEAD_OF_DEPARTMENT, POSITIONER): ");
        Status status = readStatus("Введите статус (HIRED, FIRED, RECOMMENDED_FOR_PROMOTION, REGULAR, PROBATION): ");
        Person person = readPerson();

        Worker worker = new Worker(id, name, new Coordinates(coordX, coordY), creationDate, salary, position, status, person, user);
        worker.setOwner(user);
        return worker;
    }

    private String readString(String prompt, boolean canBeEmpty) {
        while (true) {
            print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty() || canBeEmpty) {
                return input;
            }
            println("Ошибка: ввод не может быть пустым. Попробуйте снова.");
        }
    }

    private float readFloat(String prompt, float minValue) {
        while (true) {
            try {
                print(prompt);
                String input = readln().trim();
                if (input.isEmpty()) {
                    println("Значение не может быть пустым. Попробуйте снова.");
                    continue;
                }
                float value = Float.parseFloat(input);
                if (value > minValue) {
                    return value;
                }
                println("Значение должно быть больше " + minValue + ". Попробуйте снова.");
            } catch (NumberFormatException e) {
                println("Введите корректное число. Попробуйте снова.");
            }
        }
    }

    private long readLong(String prompt, long minValue) {
        while (true) {
            try {
                print(prompt);
                String input = readln().trim();
                if (input.isEmpty()) {
                    println("Значение не может быть пустым. Попробуйте снова.");
                    continue;
                }
                long value = Long.parseLong(input);
                if (value > minValue) {
                    return value;
                }
                println("Значение должно быть больше " + minValue + ". Попробуйте снова.");
            } catch (NumberFormatException e) {
                println("Введите корректное число. Попробуйте снова.");
            }
        }
    }

    private int readInt(String prompt, int minValue) {
        while (true) {
            try {
                print(prompt);
                String input = readln().trim();
                if (input.isEmpty()) {
                    println("Значение не может быть пустым. Попробуйте снова.");
                    continue;
                }
                int value = Integer.parseInt(input);
                if (value > minValue) {
                    return value;
                }
                println("Значение должно быть больше " + minValue + ". Попробуйте снова.");
            } catch (NumberFormatException e) {
                println("Введите корректное число. Попробуйте снова.");
            }
        }
    }

    private Date readDate(String prompt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        while (true) {
            try {
                print(prompt);
                String input = readln().trim();
                if (input.isEmpty()) {
                    println("Дата не может быть пустой. Попробуйте снова.");
                    continue;
                }
                return dateFormat.parse(input);
            } catch (ParseException e) {
                println("Введите дату в формате YYYY-MM-DD. Попробуйте снова.");
            }
        }
    }

    private Position readPosition(String prompt) {
        while (true) {
            try {
                print(prompt);
                String input = readln().trim().toUpperCase();
                if (input.isEmpty()) {
                    println("Должность не может быть пустой. Попробуйте снова.");
                    continue;
                }
                return Position.valueOf(input);
            } catch (IllegalArgumentException e) {
                println("Введите одно из значений: MANAGER, HEAD_OF_DEPARTMENT, POSITIONER. Попробуйте снова.");
            }
        }
    }

    private Status readStatus(String prompt) {
        while (true) {
            try {
                print(prompt);
                String input = readln().trim().toUpperCase();
                if (input.isEmpty()) {
                    println("Статус не может быть пустым. Попробуйте снова.");
                    continue;
                }
                return Status.valueOf(input);
            } catch (IllegalArgumentException e) {
                println("Введите одно из значений: HIRED, FIRED, RECOMMENDED_FOR_PROMOTION, REGULAR, PROBATION. Попробуйте снова.");
            }
        }
    }

    private Person readPerson() {
        float weight = 0;
        boolean hasWeight = false;
        
        while (!hasWeight) {
            print("Введите вес личности (float > 0, может быть пустым): ");
            String weightInput = readln().trim();
            if (weightInput.isEmpty()) {
                return null;
            }
            try {
                weight = Float.parseFloat(weightInput);
                if (weight > 0) {
                    hasWeight = true;
                } else {
                    println("Вес должен быть больше 0. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                println("Введите корректное число. Попробуйте снова.");
            }
        }

        String passportID = readString("Введите номер паспорта (может быть пустым): ", true);
        Color hairColor = readColor("Введите цвет волос (GREEN, RED, BLACK, BLUE, YELLOW, может быть пустым): ");
        Country nationality = readCountry("Введите национальность (SPAIN, CHINA, INDIA, THAILAND): ");

        return new Person(weight, passportID, hairColor, nationality);
    }

    private Color readColor(String prompt) {
        while (true) {
            print(prompt);
            String input = readln().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return Color.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                println("Введите одно из значений: GREEN, RED, BLACK, BLUE, YELLOW. Попробуйте снова.");
            }
        }
    }

    private Country readCountry(String prompt) {
        while (true) {
            try {
                print(prompt);
                String input = readln().trim().toUpperCase();
                if (input.isEmpty()) {
                    println("Национальность не может быть пустой. Попробуйте снова.");
                    continue;
                }
                return Country.valueOf(input);
            } catch (IllegalArgumentException e) {
                println("Введите одно из значений: SPAIN, CHINA, INDIA, THAILAND. Попробуйте снова.");
            }
        }
    }
}