package co.diegofer.inventoryclean.model.values.invoice;

import co.diegofer.inventoryclean.model.generic.Identity;
import co.diegofer.inventoryclean.model.values.product.ProductId;

public class InvoiceId extends Identity {

    public InvoiceId(){
    }

    private InvoiceId(String uuid){
        super(uuid);
    }

    public static InvoiceId of(String uuid){
        return new InvoiceId(uuid);
    }
}