package co.diegofer.inventoryclean.model;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.generic.Entity;
import co.diegofer.inventoryclean.model.values.invoice.InvoiceId;
import co.diegofer.inventoryclean.model.values.product.ProductId;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceEntity extends Entity<InvoiceId> {

    private InvoiceId id;
    private List<ProductSale> products;
    private double total;
    private LocalDateTime date;
    private String invoiceType;

    public InvoiceEntity(InvoiceId id, List<ProductSale> products, double total, LocalDateTime date, String invoiceType) {
        super(id);
        this.products = products;
        this.total = total;
        this.date = date;
        this.invoiceType = invoiceType;
    }

    public InvoiceId getId() {
        return id;
    }

    public List<ProductSale> getProducts() {
        return products;
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getInvoiceType() {
        return invoiceType;
    }
}
