package co.diegofer.inventoryclean.model.commands;

import co.diegofer.inventoryclean.model.generic.Command;

public class AddProductCommand extends Command {

    private String branchId;
    private String productId;
    private String name;
    private String category;
    private String description;
    private Integer price;

    public AddProductCommand() {
    }

    public AddProductCommand(String branchId, String productId, String name, String category, String description, Integer price) {
        this.branchId = branchId;
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
