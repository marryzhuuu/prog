package Controller;


import Model.*;
import View.ConsoleView;
import commands.CommandManager;
import exceptions.ScriptRecursionException;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class DragonController {
    public enum ExitCode {
        OK,
        ERROR,
        EXIT,
    }
    private DragonCollection dragonCollection;
    private ConsoleView consoleView;
    private FileManager fileManager;
    private String filename;
    private CommandManager commandManager;
    private final List<String> scriptStack = new ArrayList<>();

    public DragonController(DragonCollection dragonCollection, ConsoleView consoleView, FileManager fileManager, CommandManager commandManager) {
        this.dragonCollection = dragonCollection;
        this.consoleView = consoleView;
        this.fileManager = fileManager;
        this.commandManager = commandManager;
    }

    public void run() throws IOException, ParseException {
        var scanner = consoleView.getScanner();
        try {
            ExitCode commandStatus;
            String[] userCommand = {"", ""};

            do {
                consoleView.commandPrompt();
                userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                commandManager.addToHistory(userCommand[0]);
                commandStatus = executeCommand(userCommand);
            } while (commandStatus != ExitCode.EXIT);

        } catch (NoSuchElementException exception) {
            consoleView.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            consoleView.printError("Непредвиденная ошибка!");
        }
    }

    /**
     * Выполняет команды из файла скрипта.
     *
     * @param filename Имя файла скрипта.
     */
    public ExitCode executeScript(String filename) {

        String[] userCommand = {"", ""};
        ExitCode commandStatus;
        scriptStack.add(filename);
        if (!new File(filename).exists()) {
            filename = "../" + filename;
        }
        try (Scanner scriptScanner = new Scanner(new File(filename))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            Scanner tmpScanner = consoleView.getScanner();
            consoleView.setScanner(scriptScanner);
            consoleView.setFileMode(true);

            do {
                userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (scriptScanner.hasNextLine() && userCommand[0].isEmpty()) {
                    userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                consoleView.println(consoleView.getCommandPrompt() + String.join(" ", userCommand));
                if (userCommand[0].equals("execute_script")) {
                    for (String script : scriptStack) {
                        if (userCommand[1].equals(script)) throw new ScriptRecursionException();
                    }
                }
                commandStatus = executeCommand(userCommand);
            } while (commandStatus == ExitCode.OK && scriptScanner.hasNextLine());

            consoleView.setScanner(tmpScanner);
            consoleView.setFileMode(false);

            if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                consoleView.println("Проверьте скрипт на корректность введенных данных!");
            }

            return commandStatus;

        } catch (FileNotFoundException exception) {
            consoleView.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            consoleView.printError("Файл со скриптом пуст!");
        } catch (ScriptRecursionException exception) {
            consoleView.printError("Скрипты не могут вызываться рекурсивно!");
        } catch (IllegalStateException exception) {
            consoleView.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return ExitCode.ERROR;
    }

    /**
     * Выполняет команду.
     * @param userCommand Команда для запуска
     * @return Код завершения.
     */
    private ExitCode executeCommand(String[] userCommand) {
        if (userCommand[0].equals("")) return ExitCode.OK;
        var command = commandManager.getCommands().get(userCommand[0]);

        if (command == null) {
            consoleView.printError("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }

        switch (userCommand[0]) {
            case "exit" -> {
                if (!commandManager.getCommands().get("exit").apply(userCommand)) return ExitCode.ERROR;
                else return ExitCode.EXIT;
            }
            case "execute_script" -> {
                if (!commandManager.getCommands().get("execute_script").apply(userCommand)) return ExitCode.ERROR;
                else return executeScript(userCommand[1]);
            }
            default -> { if (!command.apply(userCommand)) return ExitCode.ERROR; }
        };

        return ExitCode.OK;
    }

}