package com.example.invoice.service;

import com.example.invoice.dto.InvoiceRequest;
import com.example.invoice.entity.Invoice;
import com.example.invoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository repository;
    private final PdfService pdfService;
    private final AzureBlobService blobService;

    public InvoiceService(InvoiceRepository repository,
                          PdfService pdfService,
                          AzureBlobService blobService) {
        this.repository = repository;
        this.pdfService = pdfService;
        this.blobService = blobService;
    }

public Invoice createInvoice(InvoiceRequest request) throws Exception {

    String invoiceNumber = "INV-" + System.currentTimeMillis();

    byte[] pdf = pdfService.generateInvoicePdf(
            invoiceNumber,
            request.getCustomerName(),
            request.getAmount(),
            request.getInvoiceDate(),
            request.getPoNumber()
    );

    String fileName = invoiceNumber + ".pdf";
    String blobUrl = blobService.upload(pdf, fileName);

    Invoice invoice = new Invoice();
    invoice.setInvoiceNumber(invoiceNumber);
    invoice.setCustomerName(request.getCustomerName());
    invoice.setAmount(request.getAmount());
    invoice.setInvoiceDate(request.getInvoiceDate());
    invoice.setPoNumber(request.getPoNumber());
    invoice.setBlobUrl(blobUrl);

    return repository.save(invoice);
}

}
