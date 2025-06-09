package server.collection;

import org.apache.logging.log4j.Logger;
import server.Main;
import server.managers.PersistenceManager;
import share.model.Color;
import share.model.Dragon;
import share.model.DragonCharacter;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Класс DragonCollection управляет коллекцией объектов Dragon.
 */
public class DragonCollection {
    private Vector<Dragon> dragons;
    private final Date date;
    private final PersistenceManager persistenceManager;
    private final Logger logger = Main.logger;
    private final ReentrantLock lock = new ReentrantLock();

    public DragonCollection() {
        dragons = new Vector<>();
        date = new Date();
//        ToDo: проверять
        persistenceManager = null;
    }

    public DragonCollection(PersistenceManager persistenceManager) throws IOException, ParseException {
        dragons = new Vector<>();
        date = new Date();
        this.persistenceManager = persistenceManager;

//        this.dragons = fileManager.loadFromFile().getDragons();
        try {
            load();
        } catch (Exception e) {
            logger.fatal("Ошибка загрузки продуктов из базы данных!", e);
            System.exit(3);
        }

    }

    /**
     * Загружает коллекцию из базы данных.
     */
    private void load() {
        logger.info("Загрузка коллекции...");

        lock.lock();
        dragons = new Vector<Dragon>();
//        var dragonsORM = persistenceManager.loadDragons();
//        var savedDragons = dragonsORM.stream().map((dao) -> {
//            ToDo: загрузка из БД
//            return new DragonCollection().getDragons();
//        }).toList();
//        ToDo: добавить в коллекцию
//        dragons.addAll(savedDragons);
        lock.unlock();
        logger.info("Загрузка завершена.");
    }

    public Date getDate() {
        return this.date;
    }

    public void addDragon(Dragon dragon) {
        dragons.add(dragon);
    }

    public void addDragon(Dragon dragon, boolean nextId) {
        dragon.setId(Dragon.nextId());
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

    public Vector<Dragon> sorted() {
        return dragons.stream()
            .sorted()
            .collect(Collectors.toCollection(Vector::new));
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
            dragon.setId(Dragon.nextId());
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

    public void save() {
//        ToDo: в БД
    }
}
