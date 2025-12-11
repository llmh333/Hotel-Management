package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.hotel_management.dto.response.ServiceBookingResponseDTO;
import org.example.hotel_management.entity.Service;
import org.example.hotel_management.enums.ServiceCategory;
import org.example.hotel_management.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceDAO extends GenericsDAO<Service, Integer>{

    private static final ServiceDAO INSTANCE = new ServiceDAO(Service.class);

    public static ServiceDAO getInstance() {
        return INSTANCE;
    }

    private ServiceDAO(Class<Service> entityClass) {
        super(entityClass);
    }

    public Optional<Service> findByName(String name) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM Service u WHERE u.name = :name";
            TypedQuery<Service> query = entityManager.createQuery(jsql, Service.class);
            query.setParameter("name", name);

            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    public long countByCategory(ServiceCategory category) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql;
            TypedQuery<Long> query;

            if (category == null) {
                jsql = "SELECT COUNT(u) FROM Service u";
                query = entityManager.createQuery(jsql, Long.class);
            } else {
                jsql = "SELECT COUNT(u) FROM Service u WHERE u.category = :category";
                query = entityManager.createQuery(jsql, Long.class);
                query.setParameter("category", category);
            }

            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }



        public List<Service> findAll(String keyword, ServiceCategory category,int pageNumber, int pageSize) {

            EntityManager em = HibernateUtil.getEntityManager();

            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Service> cq = cb.createQuery(Service.class);
                Root<Service> root = cq.from(Service.class);

                List<Predicate> predicates = new ArrayList<>();

                // Filter by keyword
                if (keyword != null && !keyword.isEmpty()) {
                    predicates.add(cb.like(root.get("name"), "%" + keyword + "%"));
                }

                // Filter by category if not null
                if (category != null) {
                    predicates.add(cb.equal(root.get("category"), category));
                }

                if (!predicates.isEmpty()) {
                    cq.where(predicates.toArray(new Predicate[0]));
                }

                cq.orderBy(cb.asc(root.get("id")));

                TypedQuery<Service> query = em.createQuery(cq);

                int offset = (pageNumber - 1) * pageSize;
                query.setFirstResult(offset);
                query.setMaxResults(pageSize);

                return query.getResultList();

            } finally {
                em.close();
            }
        }

    public List<ServiceBookingResponseDTO> findAllByBookingId(Long bookingId) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jpql = "SELECT new org.example.hotel_management.dto.response.ServiceBookingResponseDTO(" +
                    "   bs.service.name," +
                    "   bs.service.description, " +
                    "   bs.quantity," +
                    "   bs.service.price, " +
                    "   bs.totalPrice" +
                    ") " +
                    "FROM BookingService bs " +
                    "WHERE bs.booking.id = :bookingId";
            TypedQuery<ServiceBookingResponseDTO> query = entityManager.createQuery(jpql, ServiceBookingResponseDTO.class);
            query.setParameter("bookingId", bookingId);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
    }
}
