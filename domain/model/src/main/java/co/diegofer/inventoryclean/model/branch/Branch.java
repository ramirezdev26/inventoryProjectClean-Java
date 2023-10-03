package co.diegofer.inventoryclean.model.branch;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Branch {

    private String id;
    private String name;
    private String country;
    private String city;

    public Branch(String name, String country, String city) {
        this.name = name;
        this.country = country;
        this.city = city;
    }
}
