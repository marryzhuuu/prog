package share.model;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private double x;

    private float y; //Значение поля должно быть больше -948

    public Coordinates(double x, double y) {
        this.x = x;
        if(y <= -948) {
            throw new IllegalArgumentException("Координата Y должна быть больше -948");
        }
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