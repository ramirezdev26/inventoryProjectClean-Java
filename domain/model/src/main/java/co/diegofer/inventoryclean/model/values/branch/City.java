package co.diegofer.inventoryclean.model.values.branch;

import co.diegofer.inventoryclean.model.generic.ValueObject;

public class City implements ValueObject<String> {
    private final String city;

    public City(String value) {
        if(value == null || value.length() > 90) throw new IllegalArgumentException("City cannot be null or empty");
        this.city = value;
    }

    @Override
    public String value() {
        return city;
    }

}
