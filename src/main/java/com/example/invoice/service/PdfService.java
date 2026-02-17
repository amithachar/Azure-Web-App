package com.example.invoice.service;

import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(String invoiceNumber,
                                     String customerName,
                                     Double amount,
                                     LocalDate invoiceDate,
                                     String poNumber) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("INVOICE"));
        document.add(new Paragraph("Invoice Number: " + invoiceNumber));
        document.add(new Paragraph("Customer: " + customerName));
        document.add(new Paragraph("Amount: ₹" + amount));
        document.add(new Paragraph("Invoice Date: " + invoiceDate));
        document.add(new Paragraph("PO Number: " + poNumber));

        document.close();

        return out.toByteArray();
    }
}
