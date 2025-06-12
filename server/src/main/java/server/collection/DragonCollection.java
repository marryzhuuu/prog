package server.collection;

import org.apache.logging.log4j.Logger;
import server.Main;
import server.managers.PersistenceManager;
import share.exceptions.EmptyCollectionException;
import share.model.Color;
import share.model.Dragon;
import share.model.DragonCharacter;
import share.model.User;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Класс DragonCollection управляет коллекцией объектов Dragon.
 */
public class DragonCollection {
    private Vector<Dragon> dragons;
    private final LocalDate date;
    private final PersistenceManager persistenceManager;
    private final Logger logger = Main.logger;
    private final ReentrantLock lock = new ReentrantLock();

    public DragonCollection() {
        dragons = new Vector<>();
        date = LocalDate.now();
//        ToDo: проверять
        persistenceManager = null;
    }

    public DragonCollection(PersistenceManager persistenceManager) throws IOException, ParseException {
        dragons = new Vector<>();
        date = LocalDate.now();
        this.persistenceManager = persistenceManager;

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
//            ToDo: загрузка из БД ТОЛЬКО СВОИХ ДРАКОНОВ!!!!!!!!!!!
//            return new DragonCollection().getDragons();
//        }).toList();
//        ToDo: добавить в коллекцию
//        dragons.addAll(savedDragons);
        lock.unlock();
        logger.info("Загрузка завершена.");
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void addDragon(Dragon dragon) {
        dragons.add(dragon);
    }

    public void addDragon(User user, Dragon dragon) {
        var newId = persistenceManager.add(user, dragon);
        logger.info("Новый дракон добавлен в БД.");

        lock.lock();
        dragons.add(dragon.copy(newId, user.getId()));
        lock.unlock();
    }

    public Dragon updateDragon(User user, int id, Dragon updated) {
        Dragon dragon = findDragonById(user, id);
        return dragon.updateFields(updated);
    }

    public void removeDragon(User user, int id) {
        persistenceManager.remove(user, id);
        lock.lock();
        dragons.removeIf(dragon -> (dragon.getId() == id && dragon.getCreatorId() == user.getId()));
        lock.unlock();
    }

    public Dragon findDragonById(User user, int id) {
        return dragons.stream()
                .filter(d -> d.getCreatorId() == user.getId())
                .filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    public Vector<Dragon> getDragons(User user) {
        return dragons.stream()
                .filter(d -> d.getCreatorId() == user.getId())
                .collect(Collectors.toCollection(Vector::new));
    }

    public Vector<Dragon> sorted(User user) {
        return dragons.stream()
            .filter(d -> d.getCreatorId() == user.getId())
            .sorted()
            .collect(Collectors.toCollection(Vector::new));
    }

    public int size() {
        return this.dragons.size();
    }

    public void clear(User user) {
        persistenceManager.clear(user);
        lock.lock();
        dragons.removeIf(dragon -> dragon.getCreatorId() == user.getId());
        lock.unlock();
    }

    public Dragon addIfMax(User user, Dragon dragon) throws EmptyCollectionException {
        Dragon oldestDragon = null;
        if(!dragons.isEmpty()) {
            // Фильтруем драконов по creationId пользователя и находим максимальный возраст
            oldestDragon = dragons.stream()
                    .filter(d -> d.getCreatorId() == user.getId())
                    .max(Comparator.comparingInt(Dragon::getAge))
                    .orElse(null);        }
        if(oldestDragon==null || dragon.getAge() > oldestDragon.getAge()) {
            var newId = persistenceManager.add(user, dragon);
            logger.info("Новый дракон добавлен в БД.");

            lock.lock();
            dragons.add(dragon.copy(newId, user.getId()));
            lock.unlock();
            return dragon;
        }
        return null;
    }

    public int removeGreater(User user, Dragon dragon) {
        persistenceManager.removeGreater(user, dragon);
        int age = dragon.getAge();
        dragons.removeIf(d -> (d.getAge() > age && dragon.getCreatorId() == user.getId()));
        return this.size();
    }

    public Map<Color, Long> groupCountingByColor(User user) {
        return dragons.stream()
                .filter(dragon -> dragon.getCreatorId() == user.getId())
                .collect(Collectors.groupingBy(Dragon::getColor, Collectors.counting()));
    }

    public long countGreaterThanAge(User user, long minAge) {
        return dragons.stream()
                .filter(dragon -> dragon.getCreatorId() == user.getId())
                .filter(dragon -> dragon.getAge() > minAge)
                .count();
    }

    public List<Dragon> filterLessThanCharacter(User user, DragonCharacter character) {
        return dragons.stream()
                .filter(dragon -> dragon.getCreatorId() == user.getId())
                .filter(dragon -> dragon.getTemper().compareTo(character) < 0)
                .collect(Collectors.toList());
    }

    public void save() {
//        ToDo: в БД
    }
}
