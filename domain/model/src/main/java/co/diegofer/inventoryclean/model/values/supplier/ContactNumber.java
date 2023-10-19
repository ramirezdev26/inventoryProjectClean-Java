package co.diegofer.inventoryclean.model.values.supplier;

import co.diegofer.inventoryclean.model.generic.ValueObject;

public class ContactNumber  implements ValueObject<Integer> {

    private final Integer number;

    public ContactNumber(Integer value){
        this.number = value;
    }

    @Override
    public Integer value() {
        return number;
    }
}