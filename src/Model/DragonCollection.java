package Model;

import java.util.Date;
import java.util.Vector;

/**
 * Класс DragonCollection управляет коллекцией объектов Dragon.
 */
public class DragonCollection {
    private Vector<Dragon> dragons;
    private Date date;

    public DragonCollection() {
        dragons = new Vector<>();
        date = new Date();
    }

    public Date getDate() {
        return this.date;
    }

    public void addDragon(Dragon dragon) {
        dragons.add(dragon);
    }

    public void removeDragon(int id) {
//        dragons.removeIf(dragon -> dragon.getId() == id);
    }

    public Dragon findDragonById(int id) {
//        return dragons.stream().filter(dragon -> dragon.getId() == id).findFirst().orElse(null);
        return null;
    }

    public Vector<Dragon> getDragons() {
        return dragons;
    }

    public int getSize() {
        return this.dragons.size();
    }
}
