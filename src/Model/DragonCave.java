package Model;

public class DragonCave {
    private int depth;
//    ToDo: проверку
    private Long numberOfTreasures; //Поле не может быть null, Значение поля должно быть больше 0

    public DragonCave(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}