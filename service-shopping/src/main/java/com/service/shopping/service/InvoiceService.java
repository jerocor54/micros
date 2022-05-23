package com.service.shopping.service;

import com.service.shopping.entity.Invoice;

import java.util.List;

public interface InvoiceService {
    public List<Invoice> getInvoices();
    public Invoice createInvoice(Invoice invoice);
    public Invoice updateInvoice(Invoice invoice);
    public Invoice deleteInvoice(Invoice invoice);
    public Invoice getInvoice(Long id);
}
