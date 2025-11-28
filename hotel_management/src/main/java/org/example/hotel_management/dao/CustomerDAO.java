package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.entity.Customer;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.util.HibernateUtil;

import javax.swing.text.html.Option;
import java.util.Optional;

public class CustomerDAO extends GenericsDAO<Customer, Integer>{

    private static final CustomerDAO INSTANCE = new CustomerDAO(Customer.class);

    private CustomerDAO(Class<Customer> entityClass) {
        super(entityClass);
    }

    public static CustomerDAO getInstance() {
        return INSTANCE;
    }

    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM Customer u WHERE u.phoneNumber = :phoneNumber";
            TypedQuery<Customer> query = entityManager.createQuery(jsql, Customer.class);
            query.setParameter("phoneNumber", phoneNumber);

            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return findByPhoneNumber(phoneNumber).isPresent();
    }
}
