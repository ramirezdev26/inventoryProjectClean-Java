package co.diegofer.inventoryclean.model.values.supplier;

import co.diegofer.inventoryclean.model.generic.ValueObject;

public class PaymentTerm  implements ValueObject<String> {

    private final String payment_term;

    public PaymentTerm(String value){
        this.payment_term = value;
    }

    @Override
    public String value() {
        return payment_term;
    }
}
