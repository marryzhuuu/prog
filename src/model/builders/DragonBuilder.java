package model.builders;

import model.*;
import view.ConsoleView;

public class DragonBuilder extends Builder<Dragon> {
    private final ConsoleView console;

    public DragonBuilder(ConsoleView console) {
        this.console = console;
    }
    @Override
    public Dragon build() {
        var dragon = new Dragon(
            getName(),
            getCoordinates(),
            getAge(),
            getDesctiption(),
            getColor(),
            getCharacter(),
            getCave()
        );

        return dragon;
    }

    /**
     * Ввод имени (не может быть null или пустым)
     */
    String getName() {
        // Ввод имени (не может быть null или пустым)
        String name;
        while (true) {
            console.println("Имя (не может быть пустым): ");
            console.attributePrompt();
            name = console.getScanner().nextLine().trim();
            if (!name.isEmpty()) {
                break;
            }
            console.printError("Имя не может быть пустым.");
        }
        return name;
    }

    /**
     * Ввод имени (не может быть null)
     */
    Coordinates getCoordinates() {
        return new CoordinatesBuilder(console).build();
    }

    /**
     * Ввод возраста (должен быть больше 0 и не null)
     */
    int getAge() {
        int age;
        while (true) {
            console.println("Возраст (должен быть больше 0): ");
            console.attributePrompt();
            try {
                age = Integer.parseInt(console.getScanner().nextLine());
                if (age > 0) {
                    break;
                }
                console.printError("Возраст должен быть больше 0.");
            } catch (NumberFormatException e) {
                console.printError("Введите корректное число.");
            }
        }
        return age;
    }

    /**
     * Ввод описания (не может быть null)
     */
    String getDesctiption() {
        String description;
        while (true) {
            console.println("Описание (не может быть пустым): ");
            console.attributePrompt();
            description = console.getScanner().nextLine().trim();
            if (!description.isEmpty()) {
                break;
            }
            console.printError("Описание не может быть пустым.");
        }
        return description;
    }

    /**
     * Ввод цвета (не может быть null, должен быть одним из значений enum Color)
     */
    Color getColor() {
        Color color;
        while (true) {
            console.println("Цвет (RED, ORANGE, WHITE, BROWN): ");
            console.attributePrompt();
            try {
                color = Color.valueOf(console.getScanner().nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                console.printError("Введите корректный цвет.");
            }
        }
        return color;
    }

    /**
     * Ввод характера (не может быть null, должен быть одним из значений enum DragonCharacter)
     */
    DragonCharacter getCharacter() {
        DragonCharacter character;
        while (true) {
            console.println("Характер (CUNNING, EVIL, CHAOTIC_EVIL, FICKLE, GOOD): ");
            console.attributePrompt();
            try {
                character = DragonCharacter.valueOf(console.getScanner().nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                console.printError("Ошибка: Введите корректный характер.");
            }
        }
        return character;
    }

    /**
     * Ввод пещеры (не может быть null)
     */
    DragonCave getCave() {
        return new CaveBuilder(console).build();
    }
};
