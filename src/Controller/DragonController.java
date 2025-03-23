package Controller;


import Model.Color;
import Model.Dragon;
import Model.DragonCollection;
import Model.FileManager;
import View.ConsoleView;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

public class DragonController {
    private DragonCollection dragonCollection;
    private ConsoleView consoleView;
    private FileManager fileManager;

    public DragonController(DragonCollection dragonCollection, ConsoleView consoleView, FileManager fileManager) {
        this.dragonCollection = dragonCollection;
        this.consoleView = consoleView;
        this.fileManager = fileManager;
    }

    public void start() throws IOException, ParseException {
        String filename = System.getenv("DRAGON_FILE");

        if (filename == null || filename.isEmpty()) {
            filename = "dragon_collection.json";
        }

        dragonCollection = fileManager.loadFromFile(filename);

        while (true) {
            String command = consoleView.readCommand();
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
                        break;
                    }
                    catch (Exception e) {
                        consoleView.showMessage("usage: update ID, где ID - корректный индекс элемента в коллекции");
                    }
                case "remove_by_id":
                    try {
                        int id = Integer.parseInt(parts[1].split(" ")[0]);
                        dragonCollection.removeDragon(id);
                        consoleView.showMessage("Удален элемент: " + id);
                        break;
                    }
                    catch (Exception e) {
                        consoleView.showMessage("usage: remove_by_id ID, где ID - корректный индекс элемента в коллекции");
                    }
                case "clear":
                    dragonCollection.clear();
                    consoleView.showMessage("Коллекция очищена");
                    break;
                case "save":
                    fileManager.saveToFile(filename, dragonCollection);
                    break;
                case "execute_script":
//                    fileManager.executeScript(parts[1]);
//                    break;
                case "exit":
                    return;
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
//                case "count_greater_than_age":
//                    int age = Integer.parseInt(parts[1]);
//                    int count = dragonCollection.countGreaterThanAge(age);
//                    consoleView.displayCountGreaterThanAge(count);
//                    break;
//                case "filter_less_than_character":
//                    DragonCharacter character = DragonCharacter.valueOf(parts[1].toUpperCase());
//                    List<Dragon> filteredDragons = dragonCollection.filterLessThanCharacter(character);
//                    consoleView.displayFilteredByCharacter(filteredDragons);
//                    break;
                default:
//                    consoleView.showMessage("Неизвестная команда");
            }
        }
    }
}