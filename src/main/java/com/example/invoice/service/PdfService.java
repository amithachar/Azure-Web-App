package com.example.invoice.service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(String invoiceNumber,
                                     String customer,
                                     Double amount) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("Invoice Number: " + invoiceNumber));
        document.add(new Paragraph("Customer: " + customer));
        document.add(new Paragraph("Amount: ₹" + amount));
        document.add(new Paragraph("Invoice Date: " + invoice.getInvoiceDate()));
        document.add(new Paragraph("PO Number: " + invoice.getPoNumber()));

        document.close();

        return out.toByteArray();
    }
}
