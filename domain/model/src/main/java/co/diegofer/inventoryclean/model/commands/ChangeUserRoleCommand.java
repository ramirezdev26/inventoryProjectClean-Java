package co.diegofer.inventoryclean.model.commands;

import co.diegofer.inventoryclean.model.generic.Command;

public class ChangeUserRoleCommand extends Command {

    private String userId;
    private String branchId;
    private String role;

    public ChangeUserRoleCommand() {
    }


    public String getUserId() {
        return userId;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getRole() {
        return role;
    }
}
