package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    Util util = new Util();
    private static Statement statement;
    private static Transaction transaction;
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try(Statement statement = Util.connect().getConnection().createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS `user` (" +
                    "  `ID` bigint NOT NULL AUTO_INCREMENT," +
                    "  `Name` varchar(20) DEFAULT NULL," +
                    "  `Surname` varchar(20) DEFAULT NULL," +
                    "  `Age` int DEFAULT NULL," +
                    "  PRIMARY KEY (`ID`)," +
                    "  UNIQUE KEY `ID` (`ID`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try(Statement statement = Util.connect().getConnection().createStatement()) {
            String query = "DROP TABLE IF EXISTS User;";
            //statement.executeUpdate(resetAutoIncrement);
            statement.executeUpdate(query);
        } catch (SQLException | ClassNotFoundException eSQL) {
            eSQL.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(Session session = Util.getSessionFactory().openSession()) {
            User user = new User(name, lastName, age);
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (HibernateException throwables) {
            if (transaction != null) {
                transaction.rollback();
            }
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.find(User.class,id));
            transaction.commit();
        } catch (HibernateException throwables) {
            if (transaction != null) {
                transaction.rollback();
            }
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try(Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            users = session.createQuery("from " + User.class.getSimpleName()).list();
            transaction.commit();
        } catch (HibernateException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE from " + User.class.getSimpleName()).executeUpdate();
            transaction.commit();
        } catch (HibernateException exception) {
            if(transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
        }
    }
}
