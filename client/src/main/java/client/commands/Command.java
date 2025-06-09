package client.commands;

import java.util.Objects;

/**
 * Абстрактная команда
 */
public abstract class Command {
    private final String name;
    private final String description;

    public Command(String name, String description) {

        this.name = name;
        this.description = description;
    }

    public boolean resolve(String name) {
        return name.equals(this.name);
    }

    /**
     * @return Название и использование команды.
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

    /**
     * Выполнить что-либо.
     * @param arguments запрос с данными для выполнения команды
     * @return результат выполнения
     */
    public abstract boolean apply(String[] arguments);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
