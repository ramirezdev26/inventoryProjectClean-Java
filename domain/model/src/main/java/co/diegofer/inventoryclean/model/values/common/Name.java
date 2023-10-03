package co.diegofer.inventoryclean.model.values.common;

import co.diegofer.inventoryclean.model.generic.ValueObject;

public class Name implements ValueObject<String> {

    private final String name;

    public Name(String value) {
        if(value == null || value.length() > 100) throw new IllegalArgumentException("Name cannot be null or above 100");
        this.name = value;
    }

    @Override
    public String value() {
        return name;
    }

}
