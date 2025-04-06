package model.builders;

import model.*;
import view.ConsoleView;

public class CoordinatesBuilder extends Builder<Coordinates> {
    private final ConsoleView console;
    private final Coordinates current;

    public CoordinatesBuilder(ConsoleView console) {
        this.console = console;
        this.current = null;
    }

    public CoordinatesBuilder(ConsoleView console, Coordinates current) {
        this.console = console;
        this.current = current;
    }

    @Override
    public Coordinates build() {
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
                    break;
                }
                x = Double.parseDouble(coordString);
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите корректное число.");
            }
        }
        while (true) {
            System.out.print("Координата Y: ");
            console.attributePrompt();
            try {
                String coordString = console.getScanner().nextLine().trim();
                if (coordString.isEmpty()) {
                    if(current != null) {
                        y = current.getY();
                    }
                    break;
                }
                y = Double.parseDouble(coordString);
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите корректное число.");
            }
        }
        return new Coordinates(x, y);
    }
};
