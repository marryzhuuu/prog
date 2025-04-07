package model.builders;

import model.Coordinates;
import model.DragonCave;
import view.ConsoleView;

public class CaveBuilder extends Builder<DragonCave> {
    private final ConsoleView console;

    public CaveBuilder(ConsoleView console) {
        this.console = console;
    }


    @Override
    public DragonCave build() {
        int depth=0;
        while (true) {
            console.println("Глубина пещеры: ");
            console.attributePrompt();
            try {
                String depthString = console.getScanner().nextLine().trim();
                depth = Integer.parseInt(depthString);
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите целое число.");
            }
        }

        long treasures=0;
        while (true) {
            console.println("Количество сокровищ: ");
            console.attributePrompt();
            try {
                String treasuresString = console.getScanner().nextLine().trim();
                treasures = Long.parseLong(treasuresString);
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите целое число.");
            }
        }

        return new DragonCave(depth, treasures);
    }


    public DragonCave update(DragonCave current) {
        int depth=0;
        while (true) {
            console.println("Глубина пещеры: ");
            console.attributePrompt();
            try {
                String depthString = console.getScanner().nextLine().trim();
                if (depthString.isEmpty()) {
                    if(current != null) {
                        depth = current.getDepth();
                    }
                    break;
                }
                depth = Integer.parseInt(depthString);
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите целое число.");
            }
        }

        long treasures=0;
        while (true) {
            console.println("Количество сокровищ: ");
            console.attributePrompt();
            try {
                String treasuresString = console.getScanner().nextLine().trim();
                if (treasuresString.isEmpty()) {
                    if(current != null) {
                        treasures = current.getTreasures();
                    }
                    break;
                }
                treasures = Long.parseLong(treasuresString);
                break;
            } catch (NumberFormatException e) {
                console.printError("Введите целое число.");
            }
        }

        return new DragonCave(depth, treasures);
    }
};
