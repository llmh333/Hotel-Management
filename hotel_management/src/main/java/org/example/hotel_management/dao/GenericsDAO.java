package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class GenericsDAO<T, ID> {

    private static EntityManager entityManager = HibernateUtil.getEntityManager();
    private final Class<T> entityClass;

    public GenericsDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public <T> Optional<T> add(T entity) {

        EntityTransaction transactional = entityManager.getTransaction();
        try {
            transactional.begin();
            entityManager.persist(entity);
            transactional.commit();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            if  (transactional != null && transactional.isActive()) {
                transactional.rollback();
            }
            return Optional.empty();
        }
    }

    public <T> Optional<T> update(T entity) {
        EntityTransaction transactional = entityManager.getTransaction();
        try {
            transactional.begin();
            entityManager.merge(entity);
            transactional.commit();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            if  (transactional != null && transactional.isActive()) {
                transactional.rollback();
            }
            return Optional.empty();
        }
    }

    public boolean delete(T entity) {
        EntityTransaction transactional = entityManager.getTransaction();
        try {
            transactional.begin();
            entityManager.persist(entity);
            transactional.commit();

            return true;
        } catch (Exception e) {
            if  (transactional != null && transactional.isActive()) {
                transactional.rollback();
            }
            return false;
        }
    }

    public Optional<T> findById(Object id) {
        try {
            T entity = entityManager.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<T> findAll() {
        String sql = "SELECT * FROM " +  entityClass.getSimpleName();
        try {
            TypedQuery<T> query = entityManager.createQuery(sql, entityClass);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
}
