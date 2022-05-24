package com.service.shopping.service;

import com.service.shopping.Repository.InvoiceItemsRepository;
import com.service.shopping.Repository.InvoiceRepository;
import com.service.shopping.client.CustomerClient;
import com.service.shopping.client.ProductClient;
import com.service.shopping.entity.Invoice;
import com.service.shopping.entity.InvoiceItem;
import com.service.shopping.model.Customer;
import com.service.shopping.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvoiceServiceImpl implements  InvoiceService{

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceItemsRepository invoiceItemsRepository;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    ProductClient productClient;

    @Override
    public List<Invoice> getInvoices() {
        return this.invoiceRepository.findAll();
    }

    @Override
    public Invoice getInvoice(Long id) {
        Invoice invoiceDB = this.invoiceRepository.findById(id).orElse(null);
        if(invoiceDB != null){
            Customer customer = customerClient.getCustomer(invoiceDB.getCustomerId()).getBody();
            invoiceDB.setCustomer(customer);
            List<InvoiceItem> listItems = invoiceDB.getItems().stream().map(invoiceItem -> {
                Product product = productClient.getProduct(invoiceItem.getProductId()).getBody();
                invoiceItem.setProduct(product);
                return invoiceItem;
            }).collect(Collectors.toList());
            invoiceDB.setItems(listItems);
        }
        return invoiceDB;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDB = this.invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if(invoiceDB != null){
            return invoiceDB;
        }
        invoice.setState("CREATED");
        invoiceDB = this.invoiceRepository.save(invoice);
        invoiceDB.getItems().forEach(invoiceItem -> {
            productClient.updateStock(invoiceItem.getProductId(), invoiceItem.getQuantity() *  -1);
        });
        return invoiceDB;
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if(invoiceDB == null){
            return null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return this.invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if(invoiceDB == null){
            return null;
        }
        invoiceDB.setState("DELETED");
        return this.invoiceRepository.save(invoiceDB);
    }
}
