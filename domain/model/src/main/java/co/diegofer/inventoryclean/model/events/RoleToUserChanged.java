package co.diegofer.inventoryclean.model.events;

import co.diegofer.inventoryclean.model.generic.DomainEvent;

public class RoleToUserChanged extends DomainEvent {

    private String userId;
    private String role;
    private String roleToChange;

    public RoleToUserChanged() {
        super("co.diegofer.inventoryclean.model.events.RoleToUserChanged");
    }

    public RoleToUserChanged(String userId, String role) {
        super("co.diegofer.inventoryclean.model.events.RoleToUserChanged");
        this.userId = userId;
        this.roleToChange = role;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoleToChange() {
        return roleToChange;
    }

    public String getRole() {
        return role;
    }
}
