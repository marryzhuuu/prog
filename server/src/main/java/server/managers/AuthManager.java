package server.managers;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import server.Main;
import server.orm.UserORM;
import share.exceptions.WrongPasswordException;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class AuthManager {
  private final SessionFactory sessionFactory;
  private final int SALT_LENGTH = 10;
  private final String pepper;

  private final Logger logger = Main.logger;

  public AuthManager(SessionFactory sessionFactory, String pepper) {
    this.sessionFactory = sessionFactory;
    this.pepper = pepper;
  }

  public int registerUser(String login, String password) throws SQLException {
    logger.info("Создание нового пользователя " + login);

    var salt = generateSalt();
    var passwordHash = generatePasswordHash(password, salt);

    var userORM = new UserORM();
    userORM.setName(login);
    userORM.setPasswordDigest(passwordHash);
    userORM.setSalt(salt);

    try (var session = sessionFactory.getCurrentSession()) {
      var transaction = session.beginTransaction();
      try {
        session.persist(userORM);
        transaction.commit();
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    }

    var newId = userORM.getId();
    logger.info("Пользователь успешно создан, id#" + newId);
    return newId;
  }

  public int loginUser(String login, String password) throws SQLException, WrongPasswordException {
    logger.info("Аутентификация пользователя " + login);
    try (var session = sessionFactory.getCurrentSession()) {
      var transaction = session.beginTransaction();
      try {
        var query = session.createQuery("SELECT u FROM users u WHERE u.name = :name");
        query.setParameter("name", login);

        List<UserORM> result = (List<UserORM>) query.list();

        if (result.isEmpty()) {
          logger.warn("Пользователь " + login + " не существует.");
          return 0;
        }

        var user = result.get(0);
        session.getTransaction().commit();
        session.close();

        var id = user.getId();
        var salt = user.getSalt();
        var expectedHashedPassword = user.getPasswordDigest();

        var actualHashedPassword = generatePasswordHash(password, salt);
        if (expectedHashedPassword.equals(actualHashedPassword)) {;
          logger.info("Пользователь " + login + " аутентифицирован c id#" + id);
          return id;
        }

        logger.warn(
                "Неправильный пароль для пользователя " + login +
                        ". Ожидалось '" + expectedHashedPassword + "', получено '" + actualHashedPassword + "'"
        );
        throw new WrongPasswordException("Неправильный пароль пользователя");
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
        }
        throw e;
      }
    }
  }

  private String generateSalt() {
    return RandomStringUtils.randomAlphanumeric(SALT_LENGTH);
  }

  private String generatePasswordHash(String password, String salt) {
    return Hashing.sha512()
      .hashString(pepper + password + salt, StandardCharsets.UTF_8)
      .toString();
  }
}
