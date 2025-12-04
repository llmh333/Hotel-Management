package org.example.hotel_management.dao;

import org.example.hotel_management.entity.Invoice;

public class InvoiceDAO extends GenericsDAO<Invoice, Long>{

    private static final InvoiceDAO INSTANCE = new InvoiceDAO(Invoice.class);

    private InvoiceDAO(Class<Invoice> entityClass) {
        super(entityClass);
    }

    public static InvoiceDAO getInstance() {
        return INSTANCE;
    }

}
