package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class GenericsDAO<T, ID> {

    private final Class<T> entityClass;

    public GenericsDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public <T> Optional<T> add(T entity) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
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
        } finally {
            entityManager.close();
        }
    }

    public <T> Optional<T> update(T entity) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
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
        } finally {
            entityManager.close();
        }
    }

    public boolean delete(T entity) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction transactional = entityManager.getTransaction();
        try {
            transactional.begin();
            T managedEntity = entityManager.contains(entity) ? entity : entityManager.merge(entity);
            entityManager.remove(managedEntity);
            transactional.commit();

            return true;
        } catch (Exception e) {
            if  (transactional != null && transactional.isActive()) {
                transactional.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            entityManager.close();
        }
    }

    public Optional<T> findById(Object id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            T entity = entityManager.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public List<T> findAll() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        String sql = "SELECT * FROM " +  entityClass.getSimpleName();
        try {
            TypedQuery<T> query = entityManager.createQuery(sql, entityClass);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    public long count() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        String sql = "SELECT COUNT(*) FROM " + entityClass.getSimpleName();
        try {
            TypedQuery<Long> query = entityManager.createQuery(sql, Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
