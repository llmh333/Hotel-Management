package org.example.hotel_management.dao;

import org.example.hotel_management.entity.BookingService;

public class BookingServiceDAO extends GenericsDAO<BookingService, Long> {

    public static final BookingServiceDAO INSTANCE = new BookingServiceDAO(BookingService.class);

    private BookingServiceDAO(Class<BookingService> entityClass) {
        super(entityClass);
    }

    public static BookingServiceDAO getInstance() {
        return INSTANCE;
    }
}
