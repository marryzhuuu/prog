package server;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import server.collection.DragonCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.AuthManager;
import server.managers.CommandManager;
import server.commands.*;
import server.managers.PersistenceManager;
import server.network.UDPDatagramServer;
import server.orm.DragonORM;
import server.orm.UserORM;
import share.commands.CommandType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Properties;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Серверная часть приложения.
 */
public class Main {
    private static final int DEFAULT_PORT = 25001;
    public static Dotenv config;

    private static DragonCollection dragonCollection; // статический для использования в shutdown hook

    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static void main(String[] args) throws IOException, ParseException {

        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) buildSessionFactory();
        var session = sessionFactory.getCurrentSession();
        Runtime.getRuntime().addShutdownHook(new Thread(sessionFactory::close));
        var persistenceManager = new PersistenceManager(sessionFactory);
        var authManager = new AuthManager(sessionFactory, config.get("PEPPER"));

        dragonCollection = new DragonCollection(persistenceManager);

        // Добавляем shutdown hook для сохранения коллекции при завершении работы сервера
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
//                logger.info("Сервер завершает работу, сохраняем коллекцию...");
//                dragonCollection.save();
//                logger.info("Коллекция успешно сохранена");
//            } catch (Exception e) {
//                logger.error("Ошибка при сохранении коллекции при завершении работы", e);
//            }
//        }));

        var commandManager = new CommandManager() {{
            addCommand(CommandType.REGISTER, new Register(authManager));
            addCommand(CommandType.LOGIN, new Login(authManager));
            addCommand(CommandType.INFO, new Info(dragonCollection));
            addCommand(CommandType.SHOW, new Show(dragonCollection));
            addCommand(CommandType.ADD, new Add(dragonCollection));
            addCommand(CommandType.GET, new GetById(dragonCollection));
            addCommand(CommandType.UPDATE, new Update(dragonCollection));
            addCommand(CommandType.REMOVE_BY_ID, new Remove(dragonCollection));
            addCommand(CommandType.CLEAR, new Clear(dragonCollection));
            addCommand(CommandType.SAVE, new Save(dragonCollection));
            addCommand(CommandType.ADD_IF_MAX, new AddIfMax(dragonCollection));
            addCommand(CommandType.REMOVE_GREATER, new RemoveGreater(dragonCollection));
            addCommand(CommandType.HISTORY, new History(this));
            addCommand(CommandType.GROUP_COUNTING_BY_COLOR, new GroupCountingByColor(dragonCollection));
            addCommand(CommandType.COUNT_GREATER_THAN_AGE, new CountGreaterThanAge(dragonCollection));
            addCommand(CommandType.FILTER_LESS_THAN_CHARACTER, new FilterLessThanCharacter(dragonCollection));
        }};

        try {
            int port = args.length > 0 ? parsePort(args[0]) : DEFAULT_PORT;
            var server = new UDPDatagramServer(InetAddress.getLocalHost(), port, new CommandHandler(commandManager));

            server.run();
        } catch (SocketException e) {
            logger.fatal("Ошибка сокета", e);
        } catch (UnknownHostException e) {
            logger.fatal("Неизвестный хост", e);
        }

    }

    private static int parsePort(String portStr) throws IllegalArgumentException {
        try {
            int port = Integer.parseInt(portStr);
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("Номер порта должен быть в диапазоне от 0 до 65535");
            }
            return port;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Номер порта должен быть целым числом");
        }
    }

    private static SessionFactory buildSessionFactory() {

        try {
            loadEnv();

            String url = config.get("DB_URL");
            String user = config.get("DB_USER");
            String password = config.get("DB_PASSWORD");

            if (url == null || url.isEmpty() || user == null || user.isEmpty() || password == null || password.isEmpty()) {
                System.out.println("В конфигурационном файле не обнаружены данные для подключения к БД");
                System.exit(1);
            }

            //Create Properties, can be read from property files too
            Properties props = new Properties();
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver");

            props.put("hibernate.connection.username", user);
            props.put("hibernate.connection.password", password);
            props.put("hibernate.connection.url", url);

            props.put("hibernate.connection.pool_size", "100");
            props.put("hibernate.current_session_context_class", "thread");
            props.put("hibernate.connection.autocommit", "true");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
            props.put("hibernate.hbm2ddl.auto", "validate");

            Configuration configuration = new Configuration();
            configuration.setProperties(props);

            configuration.addPackage("server.orm");
            configuration.addAnnotatedClass(UserORM.class);
//            configuration.addAnnotatedClass(DragonORM.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            logger.info("Hibernate Java Config serviceRegistry created");

            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            logger.error("Ошибка инициализации SessionFactory." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void loadEnv() {
        var environmentFile = ".env.dev";
        var isProduction = System.getenv("PROD");
        if (isProduction != null && isProduction.equals("true")) {
            environmentFile = ".env";
        }

        config = Dotenv.configure()
                .filename(environmentFile)
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
    }
}
