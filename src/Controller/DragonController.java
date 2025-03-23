package Controller;


import Model.*;
import View.ConsoleView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DragonController {
    private DragonCollection dragonCollection;
    private ConsoleView consoleView;
    private FileManager fileManager;
    private String filename;

    public DragonController(DragonCollection dragonCollection, ConsoleView consoleView, FileManager fileManager) {
        this.dragonCollection = dragonCollection;
        this.consoleView = consoleView;
        this.fileManager = fileManager;
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
                        throw new Exception();
                    }
                    Dragon updatedDragon = consoleView.readDragonParams(dragon);
                    dragon = dragonCollection.updateDragon(id, updatedDragon);
                    consoleView.showMessage("Обновленный элемент:\n" + dragon);
                }
                catch (Exception e) {
                    consoleView.showMessage("usage: update ID, где ID - корректный индекс элемента в коллекции");
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
                    if(!executeScript(fileString)){
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
        filename = System.getenv("DRAGON_FILE");

        if (filename == null || filename.isEmpty()) {
            filename = "dragon_collection.json";
        }

        dragonCollection = fileManager.loadFromFile(filename);

        while (true) {
            String command = consoleView.readCommand();
            boolean result = executeCommand(command);
            if(!result) {
                break;
            };
       }
    }

    /**
     * Выполняет команды из файла скрипта.
     *
     * @param filename Имя файла скрипта.
     */
    public boolean executeScript(String filename) {
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
                    return false;
                }
            }
        } catch (IOException e) {
            consoleView.showMessage("Ошибка при чтении файла скрипта: " + e.getMessage());
        }
        return true;
    }

}