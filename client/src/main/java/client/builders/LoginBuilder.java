package client.builders;

import client.view.ConsoleView;
import share.model.User;

public class LoginBuilder extends Builder<User> {
    private final ConsoleView console;

    public LoginBuilder(ConsoleView console) {
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

            if (password.isEmpty()) {
                console.printError("Пароль не может быть пустым.");
                continue;
            }
            break;
        }
        return password;
    }

};
