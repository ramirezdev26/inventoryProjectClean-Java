package co.diegofer.inventoryclean.model;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
import co.diegofer.inventoryclean.model.events.*;
import co.diegofer.inventoryclean.model.generic.EventChange;
import co.diegofer.inventoryclean.model.values.branch.City;
import co.diegofer.inventoryclean.model.values.branch.Country;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.model.values.invoice.InvoiceId;
import co.diegofer.inventoryclean.model.values.product.*;
import co.diegofer.inventoryclean.model.values.user.Email;
import co.diegofer.inventoryclean.model.values.user.Password;
import co.diegofer.inventoryclean.model.values.user.Role;
import co.diegofer.inventoryclean.model.values.user.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class BranchChange extends EventChange {

    public BranchChange(BranchAggregate branchAggregate) {

        apply((BranchCreated event) -> {
            branchAggregate.name = new Name(event.getName());
            branchAggregate.country = new Country(event.getCountry());
            branchAggregate.city = new City(event.getCity());
            branchAggregate.products = new ArrayList<>();
            branchAggregate.users = new ArrayList<>();
            branchAggregate.invoices = new ArrayList<>();
        });

        apply((ProductAdded event) -> {
            branchAggregate.products.add(new ProductEntity(
                    ProductId.of(event.getProductId()),
                    new Name(event.getName()),
                    new Category(event.getCategory()),
                    new Description(event.getDescription()),
                    new InventoryStock(0),
                    new Price(event.getPrice())
            ));
        });

        apply((UserAdded event) -> {
            branchAggregate.users.add(new UserEntity(
                    UserId.of(event.getUserId()),
                    new Name(event.getName()),
                    new Email(event.getEmail()),
                    new Password(event.getPassword()),
                    new Role(event.getRole())
            ));
        });

        apply((StockToProductAdded event) -> {
            for (ProductToAdd productRequested: event.getProducts()) {
                for (ProductEntity product : branchAggregate.products) {
                    if (product.identity().value().equals(productRequested.getProductId())) {
                        product.addStock(new InventoryStock(productRequested.getQuantity()));
                    }
            }
        }});

        apply((RoleToUserChanged event) -> {
            for (UserEntity user: branchAggregate.users) {
                if (user.identity().value().equals(event.getUserId())){
                    user.changeRole(new Role(event.getRole()));
                }
            }
        });

        apply((FinalCustomerSaleRegistered event) -> {

            branchAggregate.invoices.add(new InvoiceEntity(
                    InvoiceId.of(event.getInvoiceId()),
                    event.getProducts(),
                    event.getTotal(),
                    "Final customer sale",
                    branchAggregate.identity().value()
                    ));

            for (ProductSale productRequested: event.getProducts()) {
                for (ProductEntity productInBranch: branchAggregate.products) {
                    if (Objects.equals(productRequested.getId(), productInBranch.identity().value())){
                        productInBranch.setInventoryStock(
                                        new InventoryStock(
                                                productInBranch.inventoryStock().value() - productRequested.getQuantity()
                                        ));
                    }
                }
            }
        });

        apply((ResellerCustomerSaleRegistered event) -> {

            branchAggregate.invoices.add(new InvoiceEntity(
                    InvoiceId.of(event.getInvoiceId()),
                    event.getProducts(),
                    event.getTotal(),
                    "Reseller sale",
                    branchAggregate.identity().value()
            ));

            for (ProductSale productRequested: event.getProducts()) {
                for (ProductEntity productInBranch: branchAggregate.products) {
                    if (Objects.equals(productRequested.getId(), productInBranch.identity().value())){
                        productInBranch.setInventoryStock(
                                new InventoryStock(
                                        productInBranch.inventoryStock().value() - productRequested.getQuantity()
                                ));
                    }
                }
            }
        });

    }


}
