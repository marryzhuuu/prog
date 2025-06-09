package server.managers;

import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import server.Main;
import server.orm.DragonORM;

import java.util.List;

public class PersistenceManager {
  private final SessionFactory sessionFactory;
  private final Logger logger = Main.logger;

  public PersistenceManager(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public List<DragonORM> loadDragons() {
    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();

    var cq = session.getCriteriaBuilder().createQuery(DragonORM.class);
    var rootEntry = cq.from(DragonORM.class);
    var all = cq.select(rootEntry);

    var result = session.createQuery(all).getResultList();
    session.getTransaction().commit();
    session.close();
    return result;
  }

  //  ToDo: методы работы с БД
}
