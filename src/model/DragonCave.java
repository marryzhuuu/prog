package model;

public class DragonCave {
    private int depth;
    private Long numberOfTreasures; //Поле не может быть null, Значение поля должно быть больше 0

    public DragonCave(int depth, Long treasures) {
        this.depth = depth;
        numberOfTreasures = treasures;
    }

    public int getDepth() {
        return depth;
    }

    public Long getTreasures() {
        return numberOfTreasures;
    }

    public String toString() {
        return "depth: " + depth + "; treasures: " + numberOfTreasures;
    }
}