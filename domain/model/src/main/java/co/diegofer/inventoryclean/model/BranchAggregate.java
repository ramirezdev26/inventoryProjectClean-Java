package co.diegofer.inventoryclean.model;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
import co.diegofer.inventoryclean.model.events.*;
import co.diegofer.inventoryclean.model.generic.AggregateRoot;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.branch.City;
import co.diegofer.inventoryclean.model.values.branch.Country;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.model.values.product.*;
import co.diegofer.inventoryclean.model.values.supplier.ContactNumber;
import co.diegofer.inventoryclean.model.values.supplier.PaymentTerm;
import co.diegofer.inventoryclean.model.values.supplier.SupplierId;
import co.diegofer.inventoryclean.model.values.user.*;

import java.util.List;
import java.util.Objects;

public class BranchAggregate extends AggregateRoot<BranchId> {

    protected Name name;
    protected Country country;
    protected City city;
    protected List<ProductEntity> products;
    protected List<UserEntity> users;
    protected List<InvoiceEntity> invoices;
    protected List<SupplierEntity> suppliers;

    public BranchAggregate(BranchId id, Name name, Country country, City city) {
        super(id);
        subscribe(new BranchChange(this));
        appendChange(new BranchCreated(name.value(), country.value(), city.value())).apply();
    }

    private BranchAggregate(BranchId id) {
        super(id);
        subscribe(new BranchChange(this));
    }

    public static BranchAggregate from(BranchId id, List<DomainEvent> events) {
        BranchAggregate branchAggregate = new BranchAggregate(id);
        events.forEach(branchAggregate::applyEvent);
        return branchAggregate;
    }

    public void addProduct(ProductId productId, Name name, Category category, Description description, Price price, String branchId) {
        appendChange(new ProductAdded(productId.value(), name.value(), category.value(), description.value(), price.value(), branchId)).apply();
    }

    public void addUser(UserId userId, Name name, LastName lastName, Email email, Password password, Role role, String branchId) {
        appendChange(new UserAdded(userId.value(), name.value(), lastName.value(), email.value(), password.value(), role.value(), branchId)).apply();
    }

    public void registerFinalCustomerSale(String invoiceId, List<ProductSale> products, int total, String type, String branchId){
        appendChange(new FinalCustomerSaleRegistered(invoiceId, products, total, type, branchId)).apply();
    }

    public void registerResellerCustomerSale(String invoiceId, List<ProductSale> products, int total, String type, String branchId){
        appendChange(new ResellerCustomerSaleRegistered(invoiceId, products, total, type, branchId)).apply();
    }

    public void addStockToProduct(List<ProductToAdd> products){
        appendChange(new StockToProductAdded(products)).apply();
    }

    public void changeRoleToUser(UserId userId, Role role){
        appendChange(new RoleToUserChanged(userId.value(), role.value()));
    }

    public void registerSupplier(SupplierId id, Name name, ContactNumber number, Email email, PaymentTerm payment_term, String branchId){
        appendChange(new SupplierAdded(branchId, id.value(), name.value(), number.value(), email.value(), payment_term.value()));
    }

    public int calculateTotal(List<ProductSale> products){
        int total = 0;
        for (ProductSale productRequested: products) {
            for (ProductEntity productInBranch: this.products) {
                if (Objects.equals(productRequested.getId(), productInBranch.identity().value())){
                    total += productInBranch.price().value() * productRequested.getQuantity();
                }
            }
        }
        return total;
    }

}
