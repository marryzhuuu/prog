package server.managers;

import server.commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Менеджер команд.
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final List<String> commandHistory = new ArrayList<>();
    private static final int HISTORY_SIZE = 15;

    public static int getHistorySize() {return HISTORY_SIZE;}

    /**
     * Добавляет команду.
     * @param name Название команды.
     * @param command Команда.
     */
    public void addCommand(String name, Command command) {
        commands.put(name, command);
    }

    /**
     * @return Словарь команд.
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * @return История команд.
     */
    public List<String> getCommandHistory() {
        return commandHistory.subList(Math.max(0, commandHistory.size() - 10), commandHistory.size());
    }

    /**
     * Добавляет команду в историю.
     * @param command Команда.
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }
}
