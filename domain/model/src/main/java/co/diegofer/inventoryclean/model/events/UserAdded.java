package co.diegofer.inventoryclean.model.events;

import co.diegofer.inventoryclean.model.generic.DomainEvent;

public class UserAdded extends DomainEvent {


    private String userId;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String branchId;

    public UserAdded() {
        super("co.diegofer.inventoryclean.model.events.UserAdded");
    }

    public UserAdded(String userId, String name, String lastName, String email, String password, String role, String branchId) {
        super("co.diegofer.inventoryclean.model.events.UserAdded");
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.branchId = branchId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getBranchId() {
        return branchId;
    }
}
