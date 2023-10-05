package co.diegofer.inventoryclean.model;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.generic.Entity;
import co.diegofer.inventoryclean.model.values.invoice.InvoiceId;
import co.diegofer.inventoryclean.model.values.product.ProductId;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceEntity extends Entity<InvoiceId> {

    private List<ProductSale> products;
    private double total;
    private LocalDateTime date;
    private String invoiceType;
    private String branchId;

    public InvoiceEntity(InvoiceId id, List<ProductSale> products, double total, String invoiceType, String branchId) {
        super(id);
        this.products = products;
        this.total = total;
        this.date = LocalDateTime.now();
        this.invoiceType = invoiceType;
        this.branchId = branchId;
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

    public String getBranchId() {
        return branchId;
    }
}
