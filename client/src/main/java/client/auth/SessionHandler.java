package client.auth;


import share.exceptions.AuthentificationException;
import share.model.User;

public class SessionHandler {
  public static User currentUser = null;

  public static User getCurrentUser() throws AuthentificationException {
    if (currentUser == null) {
      throw new AuthentificationException("Пользователь не аутентифицирован");
    }
    return currentUser;
  }

  public static void setCurrentUser(User currentUser) {
    SessionHandler.currentUser = currentUser;
  }
}
