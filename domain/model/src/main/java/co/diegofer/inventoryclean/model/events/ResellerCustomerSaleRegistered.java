package co.diegofer.inventoryclean.model.events;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.generic.DomainEvent;

import java.util.List;

public class ResellerCustomerSaleRegistered extends DomainEvent {

    List<ProductSale> products;
    int total;

    public ResellerCustomerSaleRegistered() {
        super("co.diegofer.inventoryclean.model.events.ResellerCustomerSaleRegistered");
    }

    public ResellerCustomerSaleRegistered(List<ProductSale> products, int total) {
        super("co.diegofer.inventoryclean.model.events.ResellerCustomerSaleRegistered");
        this.products = products;
        this.total = total;
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
}
