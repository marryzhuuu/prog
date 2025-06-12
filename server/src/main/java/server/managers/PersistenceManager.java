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
        session.getTransaction().commit();
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
    logger.info("Добавление нового дракона..." + dragon.getName());

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

  //  ToDo: методы работы с БД
}
