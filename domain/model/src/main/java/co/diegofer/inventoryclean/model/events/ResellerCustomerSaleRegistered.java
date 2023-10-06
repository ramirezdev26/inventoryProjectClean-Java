package co.diegofer.inventoryclean.model.events;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.generic.DomainEvent;

import java.time.LocalDateTime;
import java.util.List;

public class ResellerCustomerSaleRegistered extends DomainEvent {

    String invoiceId;
    List<ProductSale> products;
    int total;
    LocalDateTime date;
    String type;
    String branchId;

    public ResellerCustomerSaleRegistered() {
        super("co.diegofer.inventoryclean.model.events.ResellerCustomerSaleRegistered");
    }

    public ResellerCustomerSaleRegistered(String invoiceId, List<ProductSale> products, int total, String type, String branchId) {
        super("co.diegofer.inventoryclean.model.events.ResellerCustomerSaleRegistered");
        this.invoiceId = invoiceId;
        this.products = products;
        this.total = total;
        this.type = type;
        this.branchId = branchId;
        this.date = LocalDateTime.now();

    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public List<ProductSale> getProducts() {
        return products;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getBranchId() {
        return branchId;
    }
}
