package co.diegofer.inventoryclean.model.commands;

import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
import co.diegofer.inventoryclean.model.generic.Command;

import java.util.List;

public class AddStockToProductCommand extends Command {

    List<ProductToAdd> products;
    private String branchId;

    public AddStockToProductCommand() {
    }


    public List<ProductToAdd> getProducts() {
        return products;
    }

    public String getBranchId() {
        return branchId;
    }

}
