package jm.task.core.jdbc.util;

import com.mysql.cj.jdbc.ConnectionImpl;
import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Util {
    private static final String DB_HOST  = "localhost";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "00000000";
    private static final String DB_NAME = "users";
    private static final String DB_PORT = ":3306/";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://"
            + DB_HOST
            + DB_PORT
            + DB_NAME
            + "?serverTimezone=Europe/Moscow&useSSL=false";
    private final static String DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static Statement statement;
    private static SessionFactory sessionFactory;

    public static Statement connect() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection connection = DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD);
        statement = connection.createStatement();
        return statement;
    }

    public void close() throws SQLException {
        if (statement != null) {
            statement.close();
            System.out.println("Connection closed");
        }
    }

    public static SessionFactory getSessionFactory() throws HibernateException {
        if(sessionFactory==null) {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();

            settings.put(Environment.DRIVER, DRIVER);
            settings.put(Environment.URL, URL);
            settings.put(Environment.USER, DB_USERNAME);
            settings.put(Environment.PASS, DB_PASSWORD);
            settings.put(Environment.DIALECT, DIALECT);
            settings.put(Environment.SHOW_SQL, "true");
            //settings.put("hibernate.hbm2ddl.auto", "update");

            configuration.setProperties(settings);
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
}
