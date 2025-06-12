package client.builders;

import client.view.ConsoleView;
import share.model.User;

public class RegisterBuilder extends Builder<User> {
    private final ConsoleView console;

    public RegisterBuilder(ConsoleView console) {
        this.console = console;
    }

    @Override
    public User build() {
        return new User(
                0,
            getName(),
            getPassword()
        );
    }

    /**
     * Ввод имени (не может быть null или пустым)
     */
    String getName() {
        String name;
        while (true) {
            console.println("Введите имя пользователя: ");
            console.attributePrompt();
            name = console.getScanner().nextLine().trim();
            if (!name.isEmpty()) {
                if (console.isScriptMode()) {
                    console.println(name);
                }
                break;
            }
            console.printError("Имя не может быть пустым.");
        }
        return name;
    }

    /**
     * Ввод пароля (не может быть null или пустым)
     */
    String getPassword() {
        String password;
        while (true) {
            console.println("Введите пароль: ");
            console.attributePrompt();
            password = console.getScanner().nextLine().trim();
            if (console.isScriptMode()) {
                console.println(password);
            }
            String repeat;
            console.println("Повторите пароль: ");
            console.attributePrompt();
            repeat = console.getScanner().nextLine().trim();
            if (!repeat.isEmpty()) {
                if (console.isScriptMode()) {
                    console.println(repeat);
                }
            }

            if (password.isEmpty() || repeat.isEmpty()) {
                console.printError("Пароль не может быть пустым.");
                continue;
            }
            if (!password.equals(repeat)) {
                console.printError("Пароли не совпадают.");
                continue;
            }

            break;
        }
        return password;
    }

};
