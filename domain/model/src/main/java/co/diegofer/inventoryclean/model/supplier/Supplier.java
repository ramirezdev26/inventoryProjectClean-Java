package co.diegofer.inventoryclean.model.supplier;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Supplier {

    private String id;
    private String name;
    private Integer number;
    private String email;
    private String payment_term;
    private String branchId;

    public Supplier(String name, Integer number, String email, String payment_term, String branchId) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.payment_term = payment_term;
        this.branchId = branchId;
    }


}
