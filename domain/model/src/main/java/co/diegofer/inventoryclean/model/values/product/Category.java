package co.diegofer.inventoryclean.model.values.product;

import co.diegofer.inventoryclean.model.generic.ValueObject;

public class Category implements ValueObject<String> {

    private final String category;

    public Category(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }

        if (!isValidCategory(category)) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }

        this.category = category;
    }

    private boolean isValidCategory(String category) {
        for (CATEGORY_ENUM enumValue : CATEGORY_ENUM.values()) {
            if (enumValue.name().equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String value() {
        return category;
    }
}
