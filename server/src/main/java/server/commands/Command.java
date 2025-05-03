package server.commands;

import java.util.Objects;

/**
 * Абстрактный класс команд.
 */
public abstract class Command implements Executable{
    private final String name;


    public Command(String name) {
        this.name = name;
    }

    /**
     * @return Название команды.
     */
    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name);
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                '}';
    }
}
