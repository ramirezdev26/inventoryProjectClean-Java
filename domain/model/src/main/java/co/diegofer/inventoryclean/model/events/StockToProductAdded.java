package co.diegofer.inventoryclean.model.events;

import co.diegofer.inventoryclean.model.generic.DomainEvent;

public class StockToProductAdded extends DomainEvent {

    private String productId;
    private Integer quantityToAdd;

    public StockToProductAdded() {
        super("co.diegofer.inventoryclean.model.events.StockToProductAdded");
    }

    public StockToProductAdded(String productId, Integer quantityToAdd) {
        super("co.diegofer.inventoryclean.model.events.StockToProductAdded");
        this.productId = productId;
        this.quantityToAdd = quantityToAdd;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantityToAdd() {
        return quantityToAdd;
    }


}
