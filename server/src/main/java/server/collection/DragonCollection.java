package server.collection;

import org.apache.logging.log4j.Logger;
import server.Main;
import server.managers.PersistenceManager;
import share.exceptions.EmptyCollectionException;
import share.exceptions.ForbiddenException;
import share.exceptions.NotFoundException;
import share.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Класс DragonCollection управляет коллекцией объектов Dragon.
 */
public class DragonCollection {
    private Vector<Dragon> dragons;
    private final LocalDate date;
    private final PersistenceManager persistenceManager;
    private final Logger logger = Main.logger;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

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
        logger.info("Загрузка коллекции из БД...");

        lock.writeLock().lock();
        dragons = new Vector<Dragon>();
        var dragonsORM = persistenceManager.loadDragons();

        var savedDragons = dragonsORM.stream().map(d -> new Dragon(
            d.getId(),
            d.getName(),
            new Coordinates(d.getX(), d.getY()),
            d.getAge(),
            d.getDescription(),
            d.getColor(),
            d.getTemper(),
            new DragonCave(d.getCaveDepth(), d.getCaveTreasures()),
            d.getCreationDate(),
            d.getCreator().getId()
        )).toList();
        dragons.addAll(savedDragons);
        lock.writeLock().unlock();
        logger.info("Загрузка завершена.");
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void addDragon(User user, Dragon dragon) {
        var newId = persistenceManager.add(user, dragon);
        logger.info("Новый дракон добавлен в БД.");

        lock.writeLock().lock();
        dragons.add(dragon.copy(newId, user.getId()));
        lock.writeLock().unlock();
    }

    public Dragon updateDragon(User user, int id, Dragon updated) throws NotFoundException, ForbiddenException {
        Dragon dragon = findDragonById(user, id);
        if (dragon == null) {
            throw new NotFoundException();
        }

        if (user.getId() != dragon.getCreatorId()) {
            throw new ForbiddenException();
        }

        updated.setId(id);
        persistenceManager.update(user, updated);

        lock.writeLock().lock();
        dragon.updateFields(updated);
        lock.writeLock().unlock();

        return dragon;
    }

    public void removeDragon(User user, int id) throws NotFoundException, ForbiddenException {
        Dragon dragon = findDragonById(user, id);
        if (dragon == null) {
            throw new NotFoundException();
        }
        if (user.getId() != dragon.getCreatorId()) {
            throw new ForbiddenException();
        }
        persistenceManager.remove(user, id);
        lock.writeLock().lock();
        dragons.removeIf(d -> (d.getId() == id && d.getCreatorId() == user.getId()));
        lock.writeLock().unlock();
    }

    public Dragon findDragonById(User user, int id) {
        return dragons.stream()
                .filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    public Vector<Dragon> getDragons(User user) {
        return dragons;
    }

    public Vector<Dragon> sorted(User user) {
        return dragons.stream()
            .sorted()
            .collect(Collectors.toCollection(Vector::new));
    }

    public int size() {
        return this.dragons.size();
    }

    /**
     * Удаляет только драконов пользователя.
     */
    public void clear(User user) {
        persistenceManager.clear(user);
        lock.writeLock().lock();
        dragons.removeIf(dragon -> dragon.getCreatorId() == user.getId());
        lock.writeLock().unlock();
    }

    public Dragon addIfMax(User user, Dragon dragon) throws EmptyCollectionException {
        Dragon oldestDragon = null;
        if(!dragons.isEmpty()) {
            // находим максимальный возраст
            oldestDragon = dragons.stream()
                    .max(Comparator.comparingInt(Dragon::getAge))
                    .orElse(null);        }
        if(oldestDragon==null || dragon.getAge() > oldestDragon.getAge()) {
            var newId = persistenceManager.add(user, dragon);
            logger.info("Новый дракон добавлен в БД.");

            lock.writeLock().lock();
            dragons.add(dragon.copy(newId, user.getId()));
            lock.writeLock().unlock();
            return dragon;
        }
        return null;
    }

    /**
     * Удаляет только драконов пользователя, старше чем у дракона.
     */
    public int removeGreater(User user, Dragon dragon) {
        persistenceManager.removeGreater(user, dragon);
        int age = dragon.getAge();
        dragons.removeIf(d -> (d.getAge() > age && dragon.getCreatorId() == user.getId()));
        return this.size();
    }

    public Map<Color, Long> groupCountingByColor(User user) {
        return dragons.stream()
                .collect(Collectors.groupingBy(Dragon::getColor, Collectors.counting()));
    }

    public long countGreaterThanAge(User user, long minAge) {
        return dragons.stream()
                .filter(dragon -> dragon.getAge() > minAge)
                .count();
    }

    public List<Dragon> filterLessThanCharacter(User user, DragonCharacter character) {
        return dragons.stream()
                .filter(dragon -> dragon.getTemper().compareTo(character) < 0)
                .collect(Collectors.toList());
    }
}
