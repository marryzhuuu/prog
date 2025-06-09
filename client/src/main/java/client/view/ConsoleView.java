package client.view;

import client.controller.DragonController;

import java.util.*;

/**
 * Класс ConsoleView отвечает за взаимодействие с пользователем через консоль.
 * Реализует выполнение набора команд для управления коллекцией объектов Dragon.
 */
public class ConsoleView {
    private static final String commandPrompt = "$ ";
    private static final String attributePrompt = "> ";
    private Scanner scanner;
    private boolean scriptMode;

    public ConsoleView() {
        scanner = new Scanner(System.in);
        scriptMode = false;
    }

    /**
     * Основной цикл ввода данных из сканера
     */
    public void run(DragonController controller) {
        try {
            DragonController.ExitCode commandStatus;
            String[] userCommand;

            do {
                commandPrompt();
                userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                commandStatus = controller.executeCommand(userCommand);
            } while (commandStatus != DragonController.ExitCode.EXIT);

        } catch (NoSuchElementException exception) {
            printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            printError("Непредвиденная ошибка!");
        }

    }

    /**
     * Переключает script mode, заменяет текущий сканер, возвращает предыдущий сканер
     */
    public Scanner selectScanner(Scanner scanner, boolean scriptMode) {
        Scanner currentScanner = this.scanner;
        this.scriptMode = scriptMode;
        this.scanner = scanner;
        return currentScanner;
    }

    /**
     * Возвращает scriptMode
     */
    public boolean isScriptMode() {
        return scriptMode;
    }

    /**
     * Читает команду из сканера
     */
    public String[] getCommand() {
        String[] command = (scanner.nextLine().trim() + " ").split(" ", 2);
        command[1] = command[1].trim();
        while (scanner.hasNextLine() && command[0].isEmpty()) {
            command = (scanner.nextLine().trim() + " ").split(" ", 2);
            command[1] = command[1].trim();
        }
        println(commandPrompt + String.join(" ", command));
        return command;
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

    public void commandPrompt() { System.out.print(commandPrompt); }

    public void attributePrompt() { System.out.print(attributePrompt); }
}