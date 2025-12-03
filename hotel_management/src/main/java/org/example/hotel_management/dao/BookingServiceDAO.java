package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import org.example.hotel_management.entity.BookingService;
import org.example.hotel_management.util.HibernateUtil;

import java.util.Optional;

public class BookingServiceDAO extends GenericsDAO<BookingService, Long> {

    public static final BookingServiceDAO INSTANCE = new BookingServiceDAO(BookingService.class);

    private BookingServiceDAO(Class<BookingService> entityClass) {
        super(entityClass);
    }

    public static BookingServiceDAO getInstance() {
        return INSTANCE;
    }


    public Optional<BookingService> findByBookingIdAndServiceId(Long bookingId, Long serviceId) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM BookingService u WHERE u.booking.id = :bookingId AND u.service.id = :serviceId";
            return Optional.ofNullable(entityManager.createQuery(jsql, BookingService.class)
                    .setParameter("bookingId", bookingId)
                    .setParameter("serviceId", serviceId)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }
}
