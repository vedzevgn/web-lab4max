package oauth;

import database.HibernateUtils;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AuthenticatedUserService {

    private final SessionFactory sessionFactory = HibernateUtils.getFactory();

    public void addUser(Long userId, String hash) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setUserId(userId);
            authenticatedUser.setHash(hash);

            session.persist(authenticatedUser);

            transaction.commit();
        }
    }

    public void deleteUserByHash(String hash) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            TypedQuery<AuthenticatedUser> query = session.createQuery(
                            "FROM oauth.AuthenticatedUser WHERE hash = :hash", AuthenticatedUser.class)
                    .setParameter("hash", hash);

            List<AuthenticatedUser> usersToDelete = query.getResultList();

            for (AuthenticatedUser user : usersToDelete) {
                session.delete(user);
            }

            transaction.commit();
        }
    }


    public List<AuthenticatedUser> getAuthenticatedUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM oauth.AuthenticatedUser", oauth.AuthenticatedUser.class)
                    .getResultList();
        }
    }
}
