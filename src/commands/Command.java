package commands;

import java.io.IOException;
import java.util.Objects;

/**
 * Абстрактный класс команд.
 */
public abstract class Command {
    private String name;
    private String description;


    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return Название команды.
     */
    public String getName() {
        return name;
    }


    /**
     * @return Описание команды.
     */
    public String getDescription() {
        return description;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description);
    }

    public int hashCode() {
        return Objects.hash(name, description);
    }

    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * Выполнить команду.
     *
     * @param arguments Аргумент для выполнения
     * @return результат выполнения
     */
    public boolean apply(String[] arguments) { return true;}
}
