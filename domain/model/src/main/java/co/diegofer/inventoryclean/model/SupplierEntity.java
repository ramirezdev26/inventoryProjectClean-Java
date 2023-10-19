package co.diegofer.inventoryclean.model;

import co.diegofer.inventoryclean.model.generic.Entity;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.model.values.product.ProductId;
import co.diegofer.inventoryclean.model.values.supplier.ContactNumber;
import co.diegofer.inventoryclean.model.values.supplier.PaymentTerm;
import co.diegofer.inventoryclean.model.values.supplier.SupplierId;
import co.diegofer.inventoryclean.model.values.user.Email;

public class SupplierEntity extends Entity<SupplierId>  {

    private Name name;
    private ContactNumber number;
    private Email email;
    private PaymentTerm payment_term;

    public SupplierEntity(SupplierId id, Name name, ContactNumber number, Email email, PaymentTerm payment_term) {
        super(id);
        this.name = name;
        this.number = number;
        this.email = email;
        this.payment_term = payment_term;
    }

    public Name name() {
        return name;
    }

    public ContactNumber number() {
        return number;
    }

    public Email email() {
        return email;
    }

    public PaymentTerm payment_term() {
        return payment_term;
    }
}
