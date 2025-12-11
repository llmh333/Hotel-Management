package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.InvoiceRequestDTO;

public interface IInvoiceService {

    public boolean addInvoice(InvoiceRequestDTO invoiceRequestDto, Long bookingId);
}
