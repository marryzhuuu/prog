package Model;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
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

    public Dragon findDragonById(long id) {
//        Iterator<Dragon> iterator = dragons.iterator();
//        while (iterator.hasNext()) {
//            Dragon dragon = iterator.next();
//            if (dragon.getId() == id) {
//                return dragon; // Возвращаем найденный объект
//            }
//        }
//        return null; // Если объект не найден
        Dragon dragon = dragons.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
        return dragon;
    }

    public Vector<Dragon> getDragons() {
        return dragons;
    }

    public int size() {
        return this.dragons.size();
    }
}
