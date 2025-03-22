import Controller.DragonController;
import Model.DragonCollection;
import Model.FileManager;
import View.ConsoleView;

/**
 * Главный класс приложения.
 */
public class Main {
    public static void main(String[] args) {
        DragonCollection dragonCollection = new DragonCollection();
        ConsoleView consoleView = new ConsoleView();
        FileManager fileManager = new FileManager();

        DragonController controller = new DragonController(dragonCollection, consoleView, fileManager);
        controller.start();
    }
}
