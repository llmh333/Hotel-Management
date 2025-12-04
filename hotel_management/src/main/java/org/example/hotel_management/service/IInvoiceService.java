package org.example.hotel_management.service;

import org.example.hotel_management.dto.request.InvoiceRequestDto;

public interface IInvoiceService {

    public boolean addInvoice(InvoiceRequestDto invoiceRequestDto, Long bookingId);
}
