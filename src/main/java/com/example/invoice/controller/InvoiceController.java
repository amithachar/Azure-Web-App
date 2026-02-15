package com.example.invoice.controller;

import com.example.invoice.dto.InvoiceRequest;
import com.example.invoice.entity.Invoice;
import com.example.invoice.repository.InvoiceRepository;
import com.example.invoice.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService service;
    private final InvoiceRepository repository;

    public InvoiceController(InvoiceService service,
                             InvoiceRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    public Invoice create(@RequestBody InvoiceRequest request) throws Exception {
        return service.createInvoice(request);
    }

    @GetMapping("/{id}")
    public Invoice get(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow();
    }

    @GetMapping
    public List<Invoice> getAll() {
        return repository.findAll();
    }
}
