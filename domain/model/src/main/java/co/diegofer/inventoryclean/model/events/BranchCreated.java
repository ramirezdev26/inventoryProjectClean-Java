package co.diegofer.inventoryclean.model.events;

import co.diegofer.inventoryclean.model.generic.DomainEvent;

public class BranchCreated extends DomainEvent {

    private String name;
    private String country;
    private String city;
    public BranchCreated() {
        super("co.diegofer.inventoryclean.model.events.BranchCreated");
    }

    public BranchCreated(String name,String country, String city){
        super("co.diegofer.inventoryclean.model.events.BranchCreated");
        this.name = name;
        this.country = country;
        this.city = city;
    }

    public String getName(){
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
