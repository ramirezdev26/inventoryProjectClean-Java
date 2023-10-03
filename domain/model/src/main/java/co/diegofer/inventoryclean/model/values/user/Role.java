package co.diegofer.inventoryclean.model.values.user;

import co.diegofer.inventoryclean.model.generic.ValueObject;
import co.diegofer.inventoryclean.model.values.product.CATEGORY_ENUM;

public class Role implements ValueObject<String> {

    private final String role;

    public Role(String value){

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Rol cannot be null or empty");
        }

        if (!isValidRol(value)) {
            throw new IllegalArgumentException("Invalid Rol: " + value);
        }

        this.role = value;
    }

    @Override
    public String value() {
        return role;
    }

    private boolean isValidRol(String rol) {
        for (ROLE_ENUM enumValue : ROLE_ENUM.values()) {
            if (enumValue.name().equalsIgnoreCase(rol)) {
                return true;
            }
        }
        return false;
    }


}
