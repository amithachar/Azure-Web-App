package com.example.invoice.dto;

public class InvoiceRequest {

    private String customerName;
    private Double amount;

    public InvoiceRequest() {}

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
