package co.diegofer.inventoryclean.model.events;

import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
import co.diegofer.inventoryclean.model.generic.DomainEvent;

import java.util.List;


public class StockToProductAdded extends DomainEvent {

    private List<ProductToAdd> products;


    public StockToProductAdded() {
        super("co.diegofer.inventoryclean.model.events.StockToProductAdded");
    }

    public StockToProductAdded(List<ProductToAdd> products) {
        super("co.diegofer.inventoryclean.model.events.StockToProductAdded");
        this.products = products;
    }

    public List<ProductToAdd> getProducts() {
        return products;
    }
}
