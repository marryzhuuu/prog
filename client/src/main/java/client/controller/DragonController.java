package client.controller;

import client.commands.Command;
import client.commands.Help;
import client.network.UDPClient;
import client.view.ConsoleView;

import java.io.*;
import java.util.*;

public class DragonController {
    public enum ExitCode {
        OK,
        ERROR,
        EXIT,
    }
    private final ConsoleView consoleView;
    private final Map<String, Command> commands;
    private final List<String> scriptStack = new ArrayList<>();

    public DragonController(ConsoleView console, UDPClient client) {
        this.consoleView = console;
        this.commands = new HashMap<>() {{
            put("help", new Help(console, client));
        }};
    }

    /**
     * Выполняет команду.
     * @param userCommand Команда для запуска
     * @return Код завершения.
     */
    public ExitCode executeCommand(String[] userCommand) {

        if (userCommand[0].isEmpty()) return ExitCode.OK;
        var command = commands.get(userCommand[0]);

        if (command == null) {
            consoleView.printError("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }

        switch (userCommand[0]) {
            case "exit" -> {
                if (!commands.get("exit").apply(userCommand)) return ExitCode.ERROR;
                else return ExitCode.EXIT;
            }
            case "execute_script" -> {
                if (!commands.get("execute_script").apply(userCommand)) return ExitCode.ERROR;
                else return executeScript(userCommand[1]);
            }
            default -> { if (!command.apply(userCommand)) return ExitCode.ERROR; }
        }

        return ExitCode.OK;
    }

    /**
     * Выполняет команды из файла скрипта.
     *
     * @param filename Имя файла скрипта.
     */
    private ExitCode executeScript(String filename) {

        String[] userCommand;
        ExitCode commandStatus = null;
        scriptStack.add(filename);
        if (!new File(filename).exists()) {
            filename = "../" + filename;
        }
        try (Scanner scriptScanner = new Scanner(new File(filename))) {
            Scanner prevScanner = consoleView.selectScanner(scriptScanner, true);
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            do {
                userCommand = consoleView.getCommand();
                boolean recursiveScript = false;
                if (userCommand[0].equals("execute_script")) {
                    for (String script : scriptStack) {
                        if (userCommand[1].equals(script)) {
                            consoleView.printError("Скрипты не могут вызываться рекурсивно!");
                            recursiveScript = true;
                            break;
                        }
                    }
                }
                if (recursiveScript) continue;
                commandStatus = executeCommand(userCommand);
            } while (commandStatus == ExitCode.OK && scriptScanner.hasNextLine());

            consoleView.selectScanner(prevScanner, false);

            if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                consoleView.println("Проверьте скрипт на корректность введенных данных!");
            }

            return commandStatus;

        } catch (FileNotFoundException exception) {
            consoleView.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            consoleView.printError("Файл со скриптом пуст!");
        } catch (IllegalStateException exception) {
            consoleView.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return ExitCode.ERROR;
    }
}