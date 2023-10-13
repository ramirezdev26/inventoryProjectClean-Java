package co.diegofer.inventoryclean.model.user;

public enum RoleEnum {
    SUPER,ADMIN,SELLER;

    public static boolean contains(String role) {
        for (RoleEnum r : values()) {
            if (r.name().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
