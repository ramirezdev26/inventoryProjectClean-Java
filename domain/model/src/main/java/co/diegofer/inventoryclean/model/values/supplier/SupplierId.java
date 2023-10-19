package co.diegofer.inventoryclean.model.values.supplier;

import co.diegofer.inventoryclean.model.generic.Identity;
import co.diegofer.inventoryclean.model.values.user.UserId;

public class SupplierId extends Identity {

    public SupplierId(){
    }

    private SupplierId(String uuid){
        super(uuid);
    }

    public static SupplierId of(String uuid){
        return new SupplierId(uuid);
    }
}
