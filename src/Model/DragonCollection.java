package Model;

import java.util.*;
import java.util.stream.Collectors;

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

    public Dragon updateDragon(int id, Dragon updated) {
        Dragon dragon = findDragonById(id);
        return dragon.updateFields(updated);
    }

    public void removeDragon(int id) {
        dragons.removeIf(dragon -> dragon.getId() == id);
    }

    public Dragon findDragonById(int id) {
        Dragon dragon = dragons.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
        return dragon;
    }

    public Vector<Dragon> getDragons() {
        return dragons;
    }

    public int size() {
        return this.dragons.size();
    }

    public void clear() {
        this.dragons.clear();
    }

    public Dragon addIfMax(Dragon dragon) {
        Dragon oldestDragon = Collections.max(dragons, Comparator.comparingInt(Dragon::getAge));
        if(dragon.getAge() > oldestDragon.getAge()) {
            dragons.add(dragon);
            return dragon;
        }
        return null;
    }

    public int removeGreater(Dragon dragon) {
        int age = dragon.getAge();
        dragons.removeIf(d -> d.getAge() > age);
        return this.size();
    }

    public Map<Color, Long> groupCountingByColor() {
        Map<Color, Long> dragonsByColor = dragons.stream()
                .collect(Collectors.groupingBy(Dragon::getColor, Collectors.counting()));
        return dragonsByColor;
    }
}
