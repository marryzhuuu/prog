package client.commands;

import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.network.requests.SaveRequest;

import java.io.IOException;

/**
 * Команда 'save'. Сохраняет коллекцию в дамп.
 */
public class Save extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Save(ConsoleView console, UDPClient client) {
        super(CommandType.SAVE, "сохранить все элементы коллекции");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        try {
            client.sendAndReceiveCommand(new SaveRequest());
            console.println("Коллекция сохранена");
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
