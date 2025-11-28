package org.example.hotel_management.dao;

import org.example.hotel_management.entity.Booking;

public class BookingDAO extends GenericsDAO<Booking, Integer> {
    private static final BookingDAO INSTANCE = new BookingDAO(Booking.class);

    private BookingDAO(Class<Booking> entityClass) {
        super(entityClass);
    }

    public static BookingDAO getInstance() {
        return INSTANCE;
    }
}
