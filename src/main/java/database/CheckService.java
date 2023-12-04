package database;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import results.CheckArea;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class CheckService implements CheckDAO {
    @Override
    public void addNewResult(CheckArea result) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtils.getFactory().openSession();
            session.beginTransaction();
            session.persist(result);
            session.getTransaction().commit();
        } catch (Throwable e) {
            System.err.println("Something went wrong in DAO: " + e);
            throw new SQLException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void updateResult(Long bus_id, CheckArea result) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtils.getFactory().openSession();
            session.beginTransaction();
            session.merge(result);
            session.getTransaction().commit();
        } catch (Throwable e) {
            System.err.println("Something went wrong in DAO: " + e);
            throw new SQLException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public CheckArea getResultById(Long result_id) throws SQLException {
        Session session = null;
        CheckArea result;
        try {
            session = HibernateUtils.getFactory().openSession();
            result = session.getReference(CheckArea.class, result_id);
        } catch (Throwable e) {
            System.err.println("Something went wrong in DAO: " + e);
            throw new SQLException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    @Override
    public Collection<CheckArea> getResults(String userHash) throws SQLException {
        Session session = null;
        List<CheckArea> results;
        try {
            session = HibernateUtils.getFactory().openSession();
            var criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<CheckArea> criteriaQuery = criteriaBuilder.createQuery(CheckArea.class);
            Root<CheckArea> root = criteriaQuery.from(CheckArea.class);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("user_hash"), userHash));

            results = session.createQuery(criteriaQuery).getResultList();
        } catch (Throwable e) {
            System.err.println("Error: " + e);
            throw new SQLException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }


    @Override
    public void deleteResult(CheckArea result) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtils.getFactory().openSession();
            session.beginTransaction();
            session.remove(result);
            session.getTransaction().commit();
        } catch (Throwable e) {
            System.err.println("Error: " + e);
            throw new SQLException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void clearResults() throws SQLException {
        Session session = null;
        try {
            session = HibernateUtils.getFactory().openSession();
            session.beginTransaction();
            String sql = "delete from results";
            session.createNativeQuery(sql, this.getClass()).executeUpdate();
            session.getTransaction().commit();
        } catch (Throwable e) {
            System.err.println("Error: " + e);
            throw new SQLException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}