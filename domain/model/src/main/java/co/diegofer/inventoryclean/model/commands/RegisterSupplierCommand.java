package co.diegofer.inventoryclean.model.commands;

import co.diegofer.inventoryclean.model.generic.Command;

public class RegisterSupplierCommand extends Command {

    private String branchId;
    private String supplierId;
    private String name;
    private Integer number;
    private String email;
    private String payment_term;

    public RegisterSupplierCommand() {
    }

    public RegisterSupplierCommand(String branchId, String supplierId, String name, Integer number, String email, String payment_term) {
        this.branchId = branchId;
        this.supplierId = supplierId;
        this.name = name;
        this.number = number;
        this.email = email;
        this.payment_term = payment_term;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getSupplierId() {
        return supplierId;
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
}
