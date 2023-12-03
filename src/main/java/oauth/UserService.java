package oauth;

import database.HibernateUtils;
import database.OauthDAO;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernateEntityManagerFactory;

import javax.ejb.*;
import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Stateless
public class UserService implements OauthDAO {

    private final SessionFactory sessionFactory = HibernateUtils.getFactory();

    @Override
    public User getUserById(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, userId);
        }
    }

    @Override
    public User registerUser(String username, String password, String name) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashPassword(password));
        user.setName(name);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }

        return user;
    }

    @Override
    public String authenticateUser(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
            return hashPassword(username);
        } else {
            return "0";
        }
    }

//    public CheckAreaBean getUserCheckAreaData(Long userId) {
//        User user = getUserById(userId);
//        if (user != null) {
//            return user.getTable();
//        }
//        return new CheckAreaBean();
//    }

    @Override
    public User findUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            org.hibernate.query.Query<User> query = session.createQuery("FROM oauth.User WHERE username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
