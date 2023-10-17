package co.diegofer.inventoryclean.model.commands;

import co.diegofer.inventoryclean.model.generic.Command;

public class RegisterUserCommand extends Command {

    public String branchId;
    public String userId;
    public String name;
    public String last_name;
    public String email;
    public String password;
    public String role;

    public RegisterUserCommand() {
    }

    public RegisterUserCommand(String branchId, String userId, String name, String last_name, String email, String password, String role) {
        this.branchId = branchId;
        this.userId = userId;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
