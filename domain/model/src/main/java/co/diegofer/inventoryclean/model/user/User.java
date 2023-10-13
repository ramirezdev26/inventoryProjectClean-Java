package co.diegofer.inventoryclean.model.user;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String id;
    private String name;
    private String last_name;
    private String email;
    private String password;
    private String role;
    private String branchId;

    public User(String name, String lastName, String email, String password, String role, String branchId) {
        this.name = name;
        this.last_name = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.branchId = branchId;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
