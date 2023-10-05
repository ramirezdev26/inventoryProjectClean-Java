package co.diegofer.inventoryclean.model.events;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.generic.DomainEvent;

import java.util.List;

public class FinalCustomerSaleRegistered extends DomainEvent {

    String invoiceId;
    List<ProductSale> products;
    int total;

    public FinalCustomerSaleRegistered() {
        super("co.diegofer.inventoryclean.model.events.FinalCustomerSaleRegistered");
    }

    public FinalCustomerSaleRegistered( String invoiceId, List<ProductSale> products, int total) {
        super("co.diegofer.inventoryclean.model.events.FinalCustomerSaleRegistered");
        this.invoiceId = invoiceId;
        this.products = products;
        this.total = total;
    }

    public List<ProductSale> getProducts() {
        return products;
    }

    public int getTotal() {
        return total;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
