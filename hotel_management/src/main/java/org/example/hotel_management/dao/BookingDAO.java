package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.entity.Booking;
import org.example.hotel_management.util.HibernateUtil;

import java.util.Optional;

public class BookingDAO extends GenericsDAO<Booking, Integer> {
    private static final BookingDAO INSTANCE = new BookingDAO(Booking.class);

    private BookingDAO(Class<Booking> entityClass) {
        super(entityClass);
    }

    public static BookingDAO getInstance() {
        return INSTANCE;
    }

    public Optional<Booking> findByRoomNumber(String roomNumber) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM Booking u WHERE u.room.roomNumber = :roomNumber";
            TypedQuery<Booking> query = entityManager.createQuery(jsql, Booking.class);
            query.setParameter("roomNumber", roomNumber);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public Optional<Booking> findActiveBookingWithDetails(String roomNumber) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            String jpql = "SELECT b FROM Booking b " +
                    "LEFT JOIN FETCH b.customer " +
                    "LEFT JOIN FETCH b.room " +
                    "LEFT JOIN FETCH b.user " +
                    "WHERE b.room.roomNumber = :roomNumber ";

            TypedQuery<Booking> query = em.createQuery(jpql, Booking.class);
            query.setParameter("roomNumber", roomNumber.trim());

            return query.getResultStream().findFirst();

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
