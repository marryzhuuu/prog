import Controller.DragonController;
import Model.DragonCollection;
import Model.FileManager;
import View.ConsoleView;
import commands.*;

import java.io.IOException;
import java.text.ParseException;

/**
 * Главный класс приложения.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        ConsoleView consoleView = new ConsoleView();
        FileManager fileManager = new FileManager(consoleView);
        DragonCollection dragonCollection = new DragonCollection(fileManager);

        var commandManager = new CommandManager() {{
            addCommand("exit", new Exit(consoleView));
            addCommand("help", new Help(consoleView, this));
            addCommand("info", new Info(consoleView, dragonCollection));
            addCommand("show", new Show(consoleView, dragonCollection));
//            register("add", new Add(console, collectionManager));
//            register("update", new Update(console, collectionManager));
//            register("remove_by_id", new RemoveById(console, collectionManager));
            addCommand("clear", new Clear(consoleView, dragonCollection));
            addCommand("save", new Save(consoleView, dragonCollection));
//            register("execute_script", new ExecuteScript(console));
//            register("head", new Head(console, collectionManager));
//            register("add_if_max", new AddIfMax(console, collectionManager));
//            register("add_if_min", new AddIfMin(console, collectionManager));
//            register("sum_of_price", new SumOfPrice(console, collectionManager));
//            register("filter_by_price", new FilterByPrice(console, collectionManager));
//            register("filter_contains_part_number", new FilterContainsPartNumber(console, collectionManager));
        }};

        DragonController controller = new DragonController(dragonCollection, consoleView, fileManager, commandManager);
        controller.run();
    }
}
