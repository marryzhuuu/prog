import Controller.DragonController;
import Model.DragonCollection;
import Model.FileManager;
import View.ConsoleView;
import commands.CommandManager;
import commands.Exit;
import commands.Help;

import java.io.IOException;
import java.text.ParseException;

/**
 * Главный класс приложения.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        FileManager fileManager = new FileManager();
        DragonCollection dragonCollection = new DragonCollection(fileManager);
        ConsoleView consoleView = new ConsoleView();

        var commandManager = new CommandManager() {{
            addCommand("exit", new Exit(consoleView));
            addCommand("help", new Help(consoleView, this));
//            register("info", new Info(console, collectionManager));
//            register("show", new Show(console, collectionManager));
//            register("add", new Add(console, collectionManager));
//            register("update", new Update(console, collectionManager));
//            register("remove_by_id", new RemoveById(console, collectionManager));
//            register("clear", new Clear(console, collectionManager));
//            register("save", new Save(console, collectionManager));
//            register("execute_script", new ExecuteScript(console));
//            register("exit", new Exit(console));
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
