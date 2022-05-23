package com.service.shopping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.shopping.entity.Invoice;
import com.service.shopping.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> getInvoices(){
        List<Invoice> invoices = this.invoiceService.getInvoices();
        if(invoices.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable("id") Long id){
        log.info("Fetching invoice with id {}", id);
        Invoice invoice = this.invoiceService.getInvoice(id);
        if(invoice == null){
            log.error("Invoice with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PostMapping()
    public ResponseEntity<Invoice> createInvioce(@RequestBody Invoice invoice, BindingResult result){
        log.info("Creating invoice: {}", invoice);
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Invoice invoiceDB = this.invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice, @PathVariable("id") Long id){
        log.info("Updating invoice with id {}", id);
        invoice.setId(id);
        Invoice invoiceDB = this.invoiceService.updateInvoice(invoice);
        if (invoiceDB == null){
            log.error("Invoice with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoiceDB);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") Long id){
        log.info("Fetching & Deleting Invoice with id {}", id);
        Invoice invoiceDB = this.invoiceService.getInvoice(id);
        if(invoiceDB == null){
            log.error("Invoice with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        invoiceDB = this.invoiceService.deleteInvoice(invoiceDB);
        return ResponseEntity.ok(invoiceDB);
    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
