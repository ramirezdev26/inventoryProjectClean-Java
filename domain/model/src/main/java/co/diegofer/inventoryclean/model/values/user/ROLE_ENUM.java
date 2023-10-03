package co.diegofer.inventoryclean.model.values.user;

public enum ROLE_ENUM {
    Super_Admin("super admin"),
    Admin("admin"),
    Employee("employee");

    private final String value;

    ROLE_ENUM(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
