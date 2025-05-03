package client;

import client.controller.DragonController;
import client.model.DragonCollection;
import client.model.FileManager;
import client.view.ConsoleView;
import client.commands.*;

import java.io.IOException;
import java.text.ParseException;

/**
 * Главный класс приложения.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        ConsoleView consoleView = new ConsoleView();
        DragonCollection dragonCollection = new DragonCollection(new FileManager());

        var commandManager = new CommandManager() {{
            addCommand("exit", new Exit(consoleView));
            addCommand("help", new Help(consoleView, this));
            addCommand("info", new Info(consoleView, dragonCollection));
            addCommand("show", new Show(consoleView, dragonCollection));
            addCommand("add", new Add(consoleView, dragonCollection));
            addCommand("update", new Update(consoleView, dragonCollection));
            addCommand("remove_by_id", new RemoveById(consoleView, dragonCollection));
            addCommand("clear", new Clear(consoleView, dragonCollection));
            addCommand("save", new Save(consoleView, dragonCollection));
            addCommand("add_if_max", new AddIfMax(consoleView, dragonCollection));
            addCommand("remove_greater", new RemoveGreater(consoleView, dragonCollection));
            addCommand("history", new History(consoleView, this));
            addCommand("group_counting_by_color", new GroupCountingByColor(consoleView, dragonCollection));
            addCommand("count_greater_than_age", new CountGreaterThanAge(consoleView, dragonCollection));
            addCommand("filter_less_than_character", new FilterLessThanCharacter(consoleView, dragonCollection));
            addCommand("execute_script", new ExecuteScript(consoleView));
        }};

        DragonController controller = new DragonController(consoleView, commandManager);

        consoleView.run(controller);
    }
}
