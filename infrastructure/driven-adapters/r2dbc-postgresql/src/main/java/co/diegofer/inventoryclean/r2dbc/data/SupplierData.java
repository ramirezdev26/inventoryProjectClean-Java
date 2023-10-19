package co.diegofer.inventoryclean.r2dbc.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Supplier")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SupplierData {

    @Id
    private String id;
    private String name;
    private Integer number;
    private String email;
    private String payment_term;
    private String branchId;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getPayment_term() {
        return payment_term;
    }

    public String getBranchId() {
        return branchId;
    }
}
