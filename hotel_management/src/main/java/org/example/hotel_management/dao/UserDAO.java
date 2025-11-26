package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.util.HibernateUtil;

import java.util.Optional;


public class UserDAO extends GenericsDAO<User, String> {

    private static final UserDAO INSTANCE = new UserDAO();
    private static final EntityManager entityManager = HibernateUtil.getEntityManager();

    private UserDAO() {
        super(User.class);
    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }

    public Optional<User> findByUsername(String username) {
        try {
            String jsql = "FROM User u WHERE u.username = :username";
            TypedQuery<User> query = entityManager.createQuery(jsql, User.class);
            query.setParameter("username", username);

            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
