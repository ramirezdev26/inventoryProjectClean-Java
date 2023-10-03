package co.diegofer.inventoryclean.model.values.product;

public enum CATEGORY_ENUM {
    Hand_Tools("hand tools"),
    Power_Tools("power tools"),
    Locksmithing("locksmithing"),
    Construction_Hardware("construction hardware"),
    Paint_and_Accessories("paint and accessories"),
    Gardening_and_Outdoors("gardening and outdoors"),
    Safety_and_Protective_Equipment("safety and protection equipment"),
    Plumbing_Supplies("plumbing supplies"),
    Electrical("electrical"),
    Home_Fixtures("home fixtures"),
    Others("others");

    private final String value;

    CATEGORY_ENUM(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
