package Controller;


import Model.*;
import View.ConsoleView;
import commands.CommandManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DragonController {
    public enum ExitCode {
        OK,
        ERROR,
        EXIT,
    }
    private DragonCollection dragonCollection;
    private ConsoleView consoleView;
    private FileManager fileManager;
    private String filename;
    private CommandManager commandManager;

    public DragonController(DragonCollection dragonCollection, ConsoleView consoleView, FileManager fileManager, CommandManager commandManager) {
        this.dragonCollection = dragonCollection;
        this.consoleView = consoleView;
        this.fileManager = fileManager;
        this.commandManager = commandManager;
    }

    public boolean executeCommand(String command) throws IOException {
        String[] parts = command.split(" ", 2);

        switch (parts[0]) {
            case "help":
                consoleView.help();
                break;
            case "info":
                consoleView.info(dragonCollection.getClass().getSimpleName(), dragonCollection.getDate(), dragonCollection.size());
                break;
            case "show":
                consoleView.showCollection(dragonCollection.getDragons());
                break;
            case "add":
                Dragon dragon = consoleView.readDragon();
                dragonCollection.addDragon(dragon);
                consoleView.showMessage("В коллекцию добавлен элемент:");
                consoleView.showElement(dragon);
                break;
            case "update":
                try {
                    int id = Integer.parseInt(parts[1].split(" ")[0]);
                    dragon = dragonCollection.findDragonById(id);
                    if (dragon == null) {
                        throw new NumberFormatException();
                    }
                    Dragon updatedDragon = consoleView.readDragonParams(dragon);
                    dragon = dragonCollection.updateDragon(id, updatedDragon);
                    consoleView.showMessage("Обновленный элемент:\n" + dragon);
                }
                catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    consoleView.showMessage("usage: update ID, где ID - корректный индекс элемента в коллекции");
                }
                catch (IllegalArgumentException e) {
                    consoleView.showMessage(e.getMessage());
                }
                break;
            case "remove_by_id":
                try {
                    int id = Integer.parseInt(parts[1].split(" ")[0]);
                    dragonCollection.removeDragon(id);
                    consoleView.showMessage("Удален элемент: " + id);
                }
                catch (Exception e) {
                    consoleView.showMessage("usage: remove_by_id ID, где ID - корректный индекс элемента в коллекции");
                }
                break;
            case "clear":
                dragonCollection.clear();
                consoleView.showMessage("Коллекция очищена");
                break;
            case "save":
                fileManager.saveToFile(filename, dragonCollection);
                break;
            case "execute_script":
                try {
                    String fileString = parts[1].split(" ")[0];
                    if(executeScript(fileString) == ExitCode.EXIT){
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка: Введите корректное имя файла скрипта:");
                }
                break;
            case "exit":
                return false;
            case "add_if_max":
                Dragon newDragon = consoleView.readDragon();
                Dragon addedDragon = dragonCollection.addIfMax(newDragon);
                if(addedDragon != null) {
                    consoleView.showMessage("Добавлен элемент:\n" + addedDragon);
                }
                else {
                    consoleView.showMessage("В коллекции есть драконы не младше");
                }
                break;
            case "remove_greater":
                Dragon greaterDragon = consoleView.readDragon();
                int initialSize = dragonCollection.size();
                int newSize = dragonCollection.removeGreater(greaterDragon);
                consoleView.showMessage("Удалено " + (initialSize-newSize) + " элементов");
                break;
            case "history":
                consoleView.history();
                break;
            case "group_counting_by_color":
                Map<Color, Long> groupedByColor = dragonCollection.groupCountingByColor();
                consoleView.displayGroupedByColor(groupedByColor);
                break;
            case "count_greater_than_age":
                try {
                    long age = Long.parseLong(parts[1].split(" ")[0]);
                    if (age <= 0) {
                        throw new Exception();
                    }
                    long count = dragonCollection.countGreaterThanAge(age);
                    consoleView.showMessage("Количество элементов старше " + age + ": " + count);
                }
                catch (Exception e) {
                    consoleView.showMessage("usage: count_greater_than_age age, где age - корректный возраст элемента");
                }
                break;
            case "filter_less_than_character":
                try {
                    String characterString = parts[1].split(" ")[0];
                    DragonCharacter character = DragonCharacter.valueOf(characterString.toUpperCase());
                    List<Dragon> filteredDragons = dragonCollection.filterLessThanCharacter(character);
                    consoleView.displayElementList(filteredDragons);
                } catch (Exception e) {
                    System.out.println("Ошибка: Введите корректный характер (CUNNING, EVIL, CHAOTIC_EVIL, FICKLE, GOOD):");
                }
                break;
            default:
                consoleView.showMessage("Неизвестная команда");
        }
        return true;
    }


    public void run() throws IOException, ParseException {
        var scanner = consoleView.getScanner();
        try {
            ExitCode commandStatus;
            String[] userCommand = {"", ""};

            do {
                consoleView.commandPrompt();
                userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                commandManager.addToHistory(userCommand[0]);
                commandStatus = executeCommand(userCommand);
            } while (commandStatus != ExitCode.EXIT);

        } catch (NoSuchElementException exception) {
            consoleView.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            consoleView.printError("Непредвиденная ошибка!");
        }
    }

    /**
     * Выполняет команды из файла скрипта.
     *
     * @param filename Имя файла скрипта.
     */
    public ExitCode executeScript(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Убираем лишние пробелы
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Пропускаем пустые строки и комментарии
                }

                // Выполняем команду
                boolean result = executeCommand(line);
                consoleView.showMessage("\n");
                if(!result) {
                    return ExitCode.ERROR;
                }
            }
        } catch (IOException e) {
            consoleView.showMessage("Ошибка при чтении файла скрипта: " + e.getMessage());
        }
        return ExitCode.EXIT;
    }
    /**
     * Launchs the command.
     * @param userCommand Команда для запуска
     * @return Код завершения.
     */
    private ExitCode executeCommand(String[] userCommand) {
        if (userCommand[0].equals("")) return ExitCode.OK;
        var command = commandManager.getCommands().get(userCommand[0]);

        if (command == null) {
            consoleView.printError("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }

        switch (userCommand[0]) {
            case "exit" -> {
                if (!commandManager.getCommands().get("exit").apply(userCommand)) return ExitCode.ERROR;
                else return ExitCode.EXIT;
            }
            case "execute_script" -> {
                if (!commandManager.getCommands().get("execute_script").apply(userCommand)) return ExitCode.ERROR;
                else return executeScript(userCommand[1]);
            }
            default -> { if (!command.apply(userCommand)) return ExitCode.ERROR; }
        };

        return ExitCode.OK;
    }

}