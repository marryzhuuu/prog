package model;

import exceptions.SaveToFileException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс DragonCollection управляет коллекцией объектов Dragon.
 */
public class DragonCollection {
    private Vector<Dragon> dragons;
    private final Date date;
    private FileManager fileManager;

    public DragonCollection() {
        dragons = new Vector<>();
        date = new Date();
    }

    public DragonCollection(FileManager fileManager) throws IOException, ParseException {
        dragons = new Vector<>();
        date = new Date();
        this.fileManager = fileManager;

        this.dragons = fileManager.loadFromFile().getDragons();
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
        return dragons.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
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
        return dragons.stream()
                .collect(Collectors.groupingBy(Dragon::getColor, Collectors.counting()));
    }

    public long countGreaterThanAge(long minAge) {
        return dragons.stream()
                .filter(dragon -> dragon.getAge() > minAge)
                .count();
    }

    public List<Dragon> filterLessThanCharacter(DragonCharacter character) {
        return dragons.stream()
                .filter(dragon -> dragon.getCharacter().compareTo(character) < 0)
                .collect(Collectors.toList());
    }

    public void save() throws SaveToFileException {
        fileManager.saveToFile(this);
    }
}
