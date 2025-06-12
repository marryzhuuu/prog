package client.commands;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.model.DragonCharacter;
import share.network.requests.FilterLessThanCharacterRequest;
import share.network.responses.FilterLessThanCharacterResponse;

import java.io.IOException;

/**
 * Команда 'filter_less_than_character'. Выводит элементы с character меньше заданного.
 */
public class FilterLessThanCharacter extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public FilterLessThanCharacter(ConsoleView console, UDPClient client) {
        super(CommandType.FILTER_LESS_THAN_CHARACTER + " character", "вывести элементы с character меньше заданного");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + " character'");
            return false;
        }

        try {
            String characterString = arguments[1].split(" ")[0];
            DragonCharacter character = DragonCharacter.valueOf(characterString.toUpperCase());
            var response = (FilterLessThanCharacterResponse) client.sendAndReceiveCommand(new FilterLessThanCharacterRequest(character, SessionHandler.getCurrentUser()));
            for (var dragon : response.dragons) {
                console.println(dragon + "\n");
            }
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (Exception e) {
            console.printError("Введите корректный характер (CUNNING, EVIL, CHAOTIC_EVIL, FICKLE, GOOD)");
        }
        return false;
    }
}
