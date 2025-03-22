package View;

import Model.*;

import java.util.*;

import Model.Dragon;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Класс ConsoleView отвечает за взаимодействие с пользователем через консоль.
 * Реализует выполнение набора команд для управления коллекцией объектов Dragon.
 */
public class ConsoleView {
    private Scanner scanner;
    private Stack<String> commandHistory;
    private static final int HISTORY_SIZE = 15;

    public ConsoleView() {
        scanner = new Scanner(System.in);
        commandHistory = new Stack<>();
    }

    /**
     * Выводит справку по доступным командам.
     */
    public void help() {
        System.out.println("Доступные команды:");
        System.out.println("help : вывести справку по доступным командам");
        System.out.println("info : вывести информацию о коллекции");
        System.out.println("show : вывести все элементы коллекции");
        System.out.println("add {element} : добавить новый элемент в коллекцию");
        System.out.println("update id {element} : обновить элемент по id");
        System.out.println("remove_by_id id : удалить элемент по id");
        System.out.println("clear : очистить коллекцию");
        System.out.println("save : сохранить коллекцию в файл");
        System.out.println("execute_script file_name : выполнить скрипт из файла");
        System.out.println("exit : завершить программу");
        System.out.println("add_if_max {element} : добавить элемент, если он больше максимального");
        System.out.println("remove_greater {element} : удалить элементы, превышающие заданный");
        System.out.println("history : вывести последние 15 команд");
        System.out.println("group_counting_by_color : сгруппировать элементы по цвету");
        System.out.println("count_greater_than_age age : вывести количество элементов с age больше заданного");
        System.out.println("filter_less_than_character character : вывести элементы с character меньше заданного");
    }

    /**
     * Выводит информацию о коллекции.
     *
     * @param collectionType тип коллекции
     * @param initDate       дата инициализации коллекции
     * @param size           количество элементов в коллекции
     */
    public void info(String collectionType, String initDate, int size) {
        System.out.println("Тип коллекции: " + collectionType);
        System.out.println("Дата инициализации: " + initDate);
        System.out.println("Количество элементов: " + size);
    }

    /**
     * Выводит все элементы коллекции в строковом представлении.
     *
     * @param dragons коллекция объектов Dragon
     */
    public void show(List<Dragon> dragons) {
        if (dragons.isEmpty()) {
            System.out.println("Коллекция пуста.");
        } else {
            for (Dragon dragon : dragons) {
                System.out.println(dragon);
            }
        }
    }

    /**
     * Читает команду от пользователя.
     *
     * @return введенная команда
     */
    public String readCommand() {
        System.out.print("Введите команду: ");
        String command = scanner.nextLine();
        addToHistory(command);
        return command;
    }

    /**
     * Добавляет команду в историю.
     *
     * @param command команда для добавления
     */
    private void addToHistory(String command) {
        if (commandHistory.size() >= HISTORY_SIZE) {
            commandHistory.remove(0);
        }
        commandHistory.push(command);
    }

    /**
     * Выводит последние 15 команд.
     */
    public void history() {
        System.out.println("Последние " + HISTORY_SIZE + " команд:");
        for (String cmd : commandHistory) {
            System.out.println(cmd.split(" ")[0]); // Выводим только команду без аргументов
        }
    }

    /**
     * Читает элемент Dragon от пользователя.
     *
     * @return объект Dragon
     */
    public Dragon readDragon() {
        System.out.print("Введите имя дракона: ");
        String name = scanner.nextLine();
        System.out.print("Введите возраст дракона: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите цвет дракона: ");
        String color = scanner.nextLine();
        System.out.print("Введите характер дракона: ");
        String character = scanner.nextLine();
        return new Dragon();
    }

    /**
     * Читает ID от пользователя.
     *
     * @return ID
     */
    public int readId() {
        System.out.print("Введите ID: ");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Читает возраст от пользователя.
     *
     * @return возраст
     */
    public int readAge() {
        System.out.print("Введите возраст: ");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Читает характер от пользователя.
     *
     * @return характер
     */
    public String readCharacter() {
        System.out.print("Введите характер: ");
        return scanner.nextLine();
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
    public void groupCountingByColor(java.util.Map<String, Long> colorCountMap) {
        System.out.println("Количество элементов по цвету:");
        for (java.util.Map.Entry<String, Long> entry : colorCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Выводит количество элементов, возраст которых больше заданного.
     *
     * @param count количество элементов
     */
    public void countGreaterThanAge(int count) {
        System.out.println("Количество элементов с возрастом больше заданного: " + count);
    }

    /**
     * Выводит элементы, значение поля character которых меньше заданного.
     *
     * @param dragons список драконов
     */
    public void filterLessThanCharacter(List<Dragon> dragons) {
        if (dragons.isEmpty()) {
            System.out.println("Нет элементов с character меньше заданного.");
        } else {
            for (Dragon dragon : dragons) {
                System.out.println(dragon);
            }
        }
    }
}