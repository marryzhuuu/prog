package client.commands;

import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.network.requests.CountGreaterThanAgeRequest;
import share.network.responses.CountGreaterThanAgeResponse;

import java.io.IOException;

/**
 * Команда 'count_greater_than_age'. Выводит количество элементов с age больше заданного.
 */
public class CountGreaterThanAge extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public CountGreaterThanAge(ConsoleView console, UDPClient client) {
        super( CommandType.COUNT_GREATER_THAN_AGE + " age", "вывести количество элементов с age больше заданного");
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
            console.println("Использование: '" + getName() + " age'");
            return false;
        }

        try {
            int age = Integer.parseInt(arguments[1].split(" ")[0]);

            var response = (CountGreaterThanAgeResponse) client.sendAndReceiveCommand(new CountGreaterThanAgeRequest(age));

            console.println("Количество элементов старше " + age + response.count);

            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (NumberFormatException e) {
            console.printError("возраст должен быть целым числом");
        }
        return false;
    }
}
