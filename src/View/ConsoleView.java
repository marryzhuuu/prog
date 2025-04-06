package View;

import Model.*;

import java.util.*;

import Model.Dragon;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import java.util.Optional;

/**
 * Класс ConsoleView отвечает за взаимодействие с пользователем через консоль.
 * Реализует выполнение набора команд для управления коллекцией объектов Dragon.
 */
public class ConsoleView {
    private static final String commandPrompt = "$ ";
    private static final String attributePrompt = "> ";
    private Scanner scanner;
    private static boolean fileMode = false;
    private Stack<String> commandHistory;
    private static final int HISTORY_SIZE = 15;

    public ConsoleView() {
        scanner = new Scanner(System.in);
        commandHistory = new Stack<>();
    }

    /**
     * Выводит obj.toString() + \n в консоль
     * @param obj Объект для печати
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Выводит ошибка: obj.toString() в консоль
     * @param obj Ошибка для печати
     */
    public void printError(Object obj) {
        System.out.println("ошибка: " + obj);
    }

    public Scanner getScanner() { return this.scanner; }

    public void setScanner(Scanner scanner) { this.scanner = scanner;}

    public boolean getFileMode() { return fileMode; }

    public void setFileMode(boolean fileMode) { ConsoleView.fileMode = fileMode;}

    public void commandPrompt() { System.out.print(commandPrompt); }

    public void attributePrompt() { System.out.print(attributePrompt); }

    public String getCommandPrompt() { return commandPrompt;}

    public String getAttributePrompt() { return attributePrompt; }

    /**
     * Метод для чтения данных дракона с консоли.
     * Требует ввода всех полей в соответствии с ограничениями класса Dragon.
     *
     * @return Объект Dragon с корректно заполненными полями.
     */
    public Dragon readDragon() {

        System.out.println("Введите данные дракона:");


        // Ввод имени (не может быть null или пустым)
        String name;
        while (true) {
            System.out.print("Имя (не может быть пустым): ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                break;
            }
            System.out.println("Ошибка: Имя не может быть пустым.");
        }

        // Ввод координат (не может быть null)
        Coordinates coordinates = readCoordinates(Optional.empty());

        // Ввод возраста (должен быть больше 0 и не null)
        Integer age;
        while (true) {
            System.out.print("Возраст (должен быть больше 0): ");
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (age > 0) {
                    break;
                }
                System.out.println("Ошибка: Возраст должен быть больше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите корректное число.");
            }
        }

        // Ввод описания (не может быть null)
        String description;
        while (true) {
            System.out.print("Описание (не может быть пустым): ");
            description = scanner.nextLine().trim();
            if (!description.isEmpty()) {
                break;
            }
            System.out.println("Ошибка: Описание не может быть пустым.");
        }

        // Ввод цвета (не может быть null, должен быть одним из значений enum Color)
        Color color;
        while (true) {
            System.out.print("Цвет (RED, ORANGE, WHITE, BROWN): ");
            try {
                color = Color.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Введите корректный цвет.");
            }
        }

        // Ввод характера (не может быть null, должен быть одним из значений enum DragonCharacter)
        DragonCharacter character;
        while (true) {
            System.out.print("Характер (CUNNING, EVIL, CHAOTIC_EVIL, FICKLE, GOOD): ");
            try {
                character = DragonCharacter.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Введите корректный характер.");
            }
        }

        // Ввод пещеры (не может быть null)
        DragonCave cave = readDragonCave(Optional.empty());

        // Создание и возврат объекта Dragon
        return new Dragon(name, coordinates, age, description, color, character, cave);

    }


    /**
     * Метод для чтения данных дракона с консоли.
     * Осуществляет ввод полей класса Dragon.
     *
     * @param dragon исходный элемент коллекции
     * @return Объект Dragon с корректно заполненными полями.
     */
    public Dragon readDragonParams(Dragon dragon) {

        System.out.println("Введите данные дракона для изменения:");


        // Ввод имени (не может быть null или пустым)
        String name;
        String currentName = dragon.getName();
        System.out.println("Текущее имя: " + currentName);
        name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = currentName;
        }
        System.out.println("Новое имя: " + name);

        // Ввод координат (не может быть null)
        Coordinates currentCoordinates = dragon.getCoordinates();
        System.out.println("Текущие координаты: " + dragon.getCoordinates());
        Coordinates coordinates = readCoordinates(Optional.of(currentCoordinates));
        System.out.println("Новые координаты: " + coordinates);

        // Ввод возраста (должен быть больше 0 и не null)
        int currentAge = dragon.getAge();
        int age=currentAge;
        System.out.println("Текущий возраст: " + currentAge);
        while(true) {
            try {
                String ageString = scanner.nextLine().trim();
                if (ageString.isEmpty()) {
                    break;
                }
                age = Integer.parseInt(ageString);
                if (age > 0) {
                    break;
                }
                System.out.println("Ошибка: Возраст должен быть больше 0.");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Возраст (должен быть больше 0): ");
            }

        }
        System.out.println("Новый возраст: " + age);


        // Ввод описания (не может быть null)
        String currentDescription = dragon.getDescription();
        String description;
        System.out.println("Текущее описание: " + currentDescription);
        description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description=currentDescription;
        }
        System.out.println("Новое описание: " + description);

        // Ввод цвета (не может быть null, должен быть одним из значений enum Color)
        Color color;
        Color currentColor=dragon.getColor();
        System.out.println("Текущий цвет: " + dragon.getColor());
        while (true) {
            System.out.print("Цвет (RED, ORANGE, WHITE, BROWN): ");
            try {
                String colorString = scanner.nextLine().trim();
                if (colorString.isEmpty()) {
                    color = currentColor;
                    break;
                }
                color = Color.valueOf(colorString.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Введите корректный цвет.");
            }
        }
        System.out.println("Новый цвет: " + color);

        // Ввод характера (не может быть null, должен быть одним из значений enum DragonCharacter)
        DragonCharacter character;
        DragonCharacter currentCharacter=dragon.getCharacter();
        System.out.println("Текущий характер: " + currentCharacter);
        while (true) {
            System.out.print("Характер (CUNNING, EVIL, GOOD, CHAOTIC_EVIL, FICKLE): ");
            try {
                String characterString = scanner.nextLine().trim();
                if (characterString.isEmpty()) {
                    character = currentCharacter;
                    break;
                }
                character = DragonCharacter.valueOf(characterString.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Введите корректный характер.");
            }
        }
        System.out.println("Новый характер: " + character);

        // Ввод пещеры (не может быть null)
        DragonCave currentCave = dragon.getCave();
        System.out.println("Текущая пещера: " + dragon.getCave());
        DragonCave cave = readDragonCave(Optional.of(currentCave));
        System.out.println("Новая пещера: " + cave);

        // Создание и возврат объекта Dragon
        return new Dragon(name, coordinates, age, description, color, character, cave);

    }

    /**
     * Метод для чтения координат дракона.
     *
     * @return Объект Coordinates с корректно заполненными полями.
     */
    private Coordinates readCoordinates(Optional<Coordinates> current) {
        double x, y;
        while (true) {
            System.out.print("Координата X: ");
            try {
                String coordString = scanner.nextLine().trim();
                if (coordString.isEmpty()) {
                    x = current.orElse(null).getX();
                    break;
                }
                x = Double.parseDouble(coordString);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите корректное число.");
            }
        }
        while (true) {
            System.out.print("Координата Y: ");
            try {
                String coordString = scanner.nextLine().trim();
                if (coordString.isEmpty()) {
                    y = current.orElse(null).getY();
                    break;
                }
                y = Double.parseDouble(coordString);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите корректное число.");
            }
        }
        return new Coordinates(x, y);
    }

    /**
     * Метод для чтения данных пещеры дракона.
     *
     * @return Объект DragonCave с корректно заполненными полями.
     */
    private DragonCave readDragonCave(Optional<DragonCave> current) {
        int depth;
        while (true) {
            System.out.print("Глубина пещеры: ");
            try {
                String depthString = scanner.nextLine().trim();
                if (depthString.isEmpty()) {
                    depth = current.orElse(null).getDepth();
                    break;
                }
                depth = Integer.parseInt(depthString);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите целое число.");
            }
        }

        Long treasures;
        while (true) {
            System.out.print("Количество сокровищ: ");
            try {
                String treasuresString = scanner.nextLine().trim();
                if (treasuresString.isEmpty()) {
                    treasures = current.orElse(null).getTreasures();
                    break;
                }
                treasures = Long.parseLong(treasuresString);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите целое число.");
            }
        }

        return new DragonCave(depth, treasures);
    }

    /**
     * Выводит сообщение об ошибке.
     *
     * @param message сообщение об ошибке
     */
    public void showError(String message) {
        System.out.println("Ошибка: " + message);
    }

    /**
     * Выводит количество элементов, сгруппированных по цвету.
     *
     * @param colorCountMap карта с количеством элементов по цвету
     */
    public void displayGroupedByColor(java.util.Map<Color, Long> colorCountMap) {
        System.out.println("Количество элементов по цвету:");
        for (java.util.Map.Entry<Color, Long> entry : colorCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Выводит элементы
     *
     * @param dragons список драконов
     */
    public void displayElementList(List<Dragon> dragons) {
        if (dragons.isEmpty()) {
            System.out.println("Нет элементов ");
        } else {
            for (Dragon dragon : dragons) {
                System.out.println(dragon);
            }
        }
    }
}