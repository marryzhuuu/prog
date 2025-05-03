package client.builders;

import client.model.*;
import client.view.ConsoleView;

public class DragonBuilder extends Builder<Dragon> {
    private final ConsoleView console;

    public DragonBuilder(ConsoleView console) {
        this.console = console;
    }

    @Override
    public Dragon build() {
        return new Dragon(
            getName(),
            getCoordinates(),
            getAge(),
            getDescription(),
            getColor(),
            getCharacter(),
            getCave()
        );
    }

    public Dragon update(Dragon dragon) {
        return new Dragon(
            getName(dragon),
            getCoordinates(dragon.getCoordinates()),
            getAge(dragon),
            getDescription(dragon),
            getColor(dragon),
            getCharacter(dragon),
            getCave(dragon.getCave())
        );
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
                if (console.isScriptMode()) {
                    console.println(name);
                }
                break;
            }
            console.printError("Имя не может быть пустым.");
        }
        return name;
    }

    String getName(Dragon dragon) {
        String name;
        String currentName = dragon.getName();
        console.println("Текущее имя: " + currentName);
        console.attributePrompt();
        name = console.getScanner().nextLine().trim();
        if (name.isEmpty()) {
            if (console.isScriptMode()) {
                console.println(name);
            }
            name = currentName;
        }
        console.println("Новое имя: " + name);
        return name;
    }


    /**
     * Ввод имени (не может быть null)
     */
    Coordinates getCoordinates() {
        return new CoordinatesBuilder(console).build();
    }

    Coordinates getCoordinates(Coordinates coordinates) {
        console.println("Текущие координаты: " + coordinates);
        Coordinates newCoordinates =  new CoordinatesBuilder(console).update(coordinates);
        console.println("Новые координаты: " + newCoordinates);
        return newCoordinates;
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
                    if (console.isScriptMode()) {
                        console.println(age);
                    }
                    break;
                }
                console.printError("Возраст должен быть больше 0.");
            } catch (NumberFormatException e) {
                console.printError("Введите корректное число.");
            }
        }
        return age;
    }

    int getAge(Dragon dragon) {
        int currentAge = dragon.getAge();
        int age=currentAge;
        console.println("Текущий возраст: " + currentAge);
        console.attributePrompt();
        while(true) {
            try {
                String ageString = console.getScanner().nextLine().trim();
                if (ageString.isEmpty()) {
                    break;
                }
                age = Integer.parseInt(ageString);
                if (age > 0) {
                    if (console.isScriptMode()) {
                        console.println(age);
                    }
                    break;
                }
                console.printError("Возраст должен быть больше 0.");
                break;
            } catch (NumberFormatException e) {
                console.printError("Возраст (должен быть больше 0): ");
            }

        }
        console.println("Новый возраст: " + age);
        return age;
    }

    /**
     * Ввод описания (не может быть null)
     */
    String getDescription() {
        String description;
        while (true) {
            console.println("Описание (не может быть пустым): ");
            console.attributePrompt();
            description = console.getScanner().nextLine().trim();
            if (!description.isEmpty()) {
                if (console.isScriptMode()) {
                    console.println(description);
                }
                break;
            }
            console.printError("Описание не может быть пустым.");
        }
        return description;
    }

    String getDescription(Dragon dragon) {
        String currentDescription = dragon.getDescription();
        String description;
        console.println("Текущее описание: " + currentDescription);
        console.attributePrompt();
        description = console.getScanner().nextLine().trim();
        if (description.isEmpty()) {
            description=currentDescription;
        }
        if (console.isScriptMode()) {
            console.println(description);
        }
        console.println("Новое описание: " + description);
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
                if (console.isScriptMode()) {
                    console.println(color);
                }
                break;
            } catch (IllegalArgumentException e) {
                console.printError("Введите корректный цвет.");
            }
        }
        return color;
    }

    Color getColor(Dragon dragon) {
        Color color;
        Color currentColor=dragon.getColor();
        console.println("Текущий цвет: " + dragon.getColor());
        while (true) {
            console.println("Цвет (RED, ORANGE, WHITE, BROWN): ");
            console.attributePrompt();
            try {
                String colorString = console.getScanner().nextLine().trim();
                if (colorString.isEmpty()) {
                    color = currentColor;
                    if (console.isScriptMode()) {
                        console.println(color);
                    }
                    break;
                }
                color = Color.valueOf(colorString.toUpperCase());
                if (console.isScriptMode()) {
                    console.println(color);
                }
                break;
            } catch (IllegalArgumentException e) {
                console.printError("Введите корректный цвет.");
            }
        }
        console.println("Новый цвет: " + color);
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
                if (console.isScriptMode()) {
                    console.println(character);
                }
                break;
            } catch (IllegalArgumentException e) {
                console.printError("Ошибка: Введите корректный характер.");
            }
        }
        return character;
    }

    DragonCharacter getCharacter(Dragon dragon) {
        DragonCharacter character;
        DragonCharacter currentCharacter=dragon.getCharacter();
        console.println("Текущий характер: " + currentCharacter);
        while (true) {
            console.println("Характер (CUNNING, EVIL, GOOD, CHAOTIC_EVIL, FICKLE): ");
            console.attributePrompt();
            try {
                String characterString = console.getScanner().nextLine().trim();
                if (characterString.isEmpty()) {
                    character = currentCharacter;
                    if (console.isScriptMode()) {
                        console.println(character);
                    }
                    break;
                }
                character = DragonCharacter.valueOf(characterString.toUpperCase());
                if (console.isScriptMode()) {
                    console.println(character);
                }
                break;
            } catch (IllegalArgumentException e) {
                console.printError("Введите корректный характер.");
            }
        }
        console.println("Новый характер: " + character);
        return character;
    }

    /**
     * Ввод пещеры (не может быть null)
     */
    DragonCave getCave() {
        return new CaveBuilder(console).build();
    }
    DragonCave getCave(DragonCave cave) {
        console.println("Текущая пещера: " + cave);
        DragonCave newCave = new CaveBuilder(console).update(cave);
        console.println("Новая пещера: " + newCave);
        return newCave;
    }
};
