package client.model.builders;

import client.model.Coordinates;
import client.model.*;
import client.view.ConsoleView;

public class CoordinatesBuilder extends Builder<Coordinates> {
    private final ConsoleView console;

    public CoordinatesBuilder(ConsoleView console) {
        this.console = console;
    }

    @Override
    public Coordinates build() {
        double x = 0, y = 0;
        while (true) {
            console.println("Координата X: ");
            console.attributePrompt();
            try {
                String coordString = console.getScanner().nextLine().trim();
                x = Double.parseDouble(coordString);
                if (console.isScriptMode()) {
                    console.println(x);
                }
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите корректное число.");
            }
        }
        while (true) {
            console.println("Координата Y: ");
            console.attributePrompt();
            try {
                String coordString = console.getScanner().nextLine().trim();
                y = Double.parseDouble(coordString);
                if (console.isScriptMode()) {
                    console.println(y);
                }
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите корректное число.");
            }
        }
        return new Coordinates(x, y);
    }


    public Coordinates update(Coordinates current) {
        double x = 0, y = 0;
        while (true) {
            console.println("Координата X: ");
            console.attributePrompt();
            try {
                String coordString = console.getScanner().nextLine().trim();
                if (coordString.isEmpty()) {
                    if(current != null) {
                        x = current.getX();
                    }
                    if (console.isScriptMode()) {
                        console.println(x);
                    }
                    break;
                }
                x = Double.parseDouble(coordString);
                if (console.isScriptMode()) {
                    console.println(x);
                }
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите корректное число.");
            }
        }
        while (true) {
            console.println("Координата Y: ");
            console.attributePrompt();
            try {
                String coordString = console.getScanner().nextLine().trim();
                if (coordString.isEmpty()) {
                    if(current != null) {
                        y = current.getY();
                    }
                    if (console.isScriptMode()) {
                        console.println(y);
                    }
                    break;
                }
                y = Double.parseDouble(coordString);
                if (console.isScriptMode()) {
                    console.println(y);
                }
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите корректное число.");
            }
        }
        return new Coordinates(x, y);
    }
};
