package server.managers;

import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import server.Main;
import server.orm.DragonORM;
import server.orm.UserORM;
import share.model.Dragon;
import share.model.User;

import java.util.List;

public class PersistenceManager {
  private final SessionFactory sessionFactory;
  private final Logger logger = Main.logger;

  public PersistenceManager(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public List<DragonORM> loadDragons() {
    try (var session = sessionFactory.getCurrentSession()) {
      var transaction = session.beginTransaction();
      try {
        var cq = session.getCriteriaBuilder().createQuery(DragonORM.class);
        var rootEntry = cq.from(DragonORM.class);
        var all = cq.select(rootEntry);

        var result = session.createQuery(all).getResultList();
        transaction.commit();
        return result;
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    }
  }

  public int add(User user, Dragon dragon) {
    logger.info("Добавление нового дракона в БД..." + dragon.getName());

    var dragonORM = new DragonORM(dragon);
    dragonORM.setCreator(new UserORM(user));
    try (var session = sessionFactory.getCurrentSession()) {
      var transaction = session.beginTransaction();
      try {
        session.persist(dragonORM);
        transaction.commit();

        logger.info("Дракон успешно добавлен.");

        var newId = dragonORM.getId();
        logger.info("Новый id дракона: " + newId);
        return newId;
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    }
  }

  /**
   * Удаляет из БД только драконов пользователя.
   */
  public void clear(User user) {
    logger.info("Удаление драконов пользователя id#" + user.getId() + " из БД...");
    try (var session = sessionFactory.getCurrentSession()) {
      var transaction = session.beginTransaction();
      try {
        var query = session.createQuery("DELETE FROM dragons WHERE creator.id = :creator");
        query.setParameter("creator", user.getId());
        var deletedSize = query.executeUpdate();
        transaction.commit();
        logger.info("Удалено " + deletedSize + " драконов.");
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    }
  }

  public void remove(User user, int id) {
    logger.info("Удаление дракона из БД...");
    try (var session = sessionFactory.getCurrentSession()) {
      var transaction = session.beginTransaction();
      try {
        var query = session.createQuery("DELETE FROM dragons WHERE id = :id");
        query.setParameter("id", id);
        int result = query.executeUpdate(); // Выполняем запрос
        transaction.commit();
        if (result > 0) {
          logger.info("Удален дракон id: " + id + " .");
        } else {
          logger.info("Дракон с id: " + id + " не найден.");
        }      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    }
  }

  public void removeGreater(User user, Dragon dragon) {
    logger.info("Удаление драконов пользователя " + user.getId() + ", превышающих заданный по возрасту из БД...");
    try (var session = sessionFactory.getCurrentSession()) {
      var transaction = session.beginTransaction();
      try {
        var query = session.createQuery("DELETE FROM dragons WHERE age > :age AND creator_id = :creatorId ");
        query.setParameter("age", dragon.getAge());
        query.setParameter("creatorId", user.getId());
        var deletedSize = query.executeUpdate();
        transaction.commit();
        logger.info("Удалено " + deletedSize + " драконов.");
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    }
  }



  public void update(User user, Dragon dragon) {
    logger.info("Обновление дракона id: " + dragon.getId() + " в БД...");
    try (var session = sessionFactory.getCurrentSession()) {
      var transaction = session.beginTransaction();
      try {
        var dragonORM = session.get(DragonORM.class, dragon.getId());
        dragonORM.update(dragon);
        session.update(dragonORM);
        transaction.commit();
        logger.info("Обновление дракона выполнено.");
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    }
  }
}
