package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.util.HibernateUtil;

import java.util.Optional;


public class UserDAO extends GenericsDAO<User, String> {

    private static final UserDAO INSTANCE = new UserDAO();

    private UserDAO() {
        super(User.class);
    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }

    public Optional<User> findByUsername(String username) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM User u WHERE u.username = :username";
            TypedQuery<User> query = entityManager.createQuery(jsql, User.class);
            query.setParameter("username", username);

            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public Optional<User> findByEmail(String email) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM User u WHERE u.email = :email";
            TypedQuery<User> query = entityManager.createQuery(jsql, User.class);
            query.setParameter("email", email);

            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM User u WHERE u.phoneNumber = :phoneNumber";
            TypedQuery<User> query = entityManager.createQuery(jsql, User.class);
            query.setParameter("phoneNumber", phoneNumber);

            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return findByPhoneNumber(phoneNumber).isPresent();
    }

}
