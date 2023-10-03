package co.diegofer.inventoryclean.model.values.branch;

import co.diegofer.inventoryclean.model.generic.ValueObject;

public class Country implements ValueObject<String>{
    private final String country;

    public Country(String value) {
        if(value == null || value.length() > 50) throw new IllegalArgumentException("Country cannot be null or empty");
        this.country = value;
    }

    @Override
    public String value() {
        return country;
    }

}
