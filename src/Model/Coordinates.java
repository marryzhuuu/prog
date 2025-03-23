package Model;

public class Coordinates {
    private double x;

//    ToDo: реализовать проверку
    private float y; //Значение поля должно быть больше -948

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = (float) y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String toString() {
        return "X: " + x + "; Y: " + y;
    }
}