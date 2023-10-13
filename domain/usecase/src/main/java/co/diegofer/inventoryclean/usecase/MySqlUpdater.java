package co.diegofer.inventoryclean.usecase;

import co.diegofer.inventoryclean.model.InvoiceEntity;
import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.branch.gateways.BranchRepository;
import co.diegofer.inventoryclean.model.events.*;
import co.diegofer.inventoryclean.model.generic.DomainUpdater;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.model.user.gateways.UserRepository;
import co.diegofer.inventoryclean.model.values.invoice.InvoiceId;
import co.diegofer.inventoryclean.usecase.generics.InvoiceRepository;

import java.time.LocalDateTime;

public class MySqlUpdater extends DomainUpdater {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;



    public MySqlUpdater(BranchRepository branchRepository, ProductRepository productRepository, UserRepository userRepository, InvoiceRepository invoiceRepository) {
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;


        listen((BranchCreated event) -> {
            Branch branch = new Branch(event.aggregateRootId(), event.getName(), event.getCountry(), event.getCity());
            branchRepository.saveABranch(branch).subscribe();
        });

        listen((ProductAdded event) -> {
            Product product = new Product(event.getProductId(), event.getName(),
                                          event.getDescription(), 0, event.getPrice(),
                                          event.getCategory(), event.getBranchId());
            productRepository.saveAProduct(product).subscribe();
        });

        listen((UserAdded event) -> {
            User user = new User(event.getUserId(), event.getName(),
                    event.getLastName(), event.getEmail(), event.getPassword(),
                    event.getRole(), event.getBranchId());
            userRepository.saveAUser(user).subscribe();
        });

        listen((StockToProductAdded event) -> {
            productRepository.addStock(event.getProducts()).subscribe();
        });

        listen((RoleToUserChanged event) -> {
            System.out.println("Paso por aqui");
            System.out.println(event.getRoleToChange());
            userRepository.changeRole(event.getUserId(), event.getRoleToChange());
        });

        listen((FinalCustomerSaleRegistered event) -> {
            InvoiceEntity invoiceEntity = new InvoiceEntity(
                    InvoiceId.of(event.getInvoiceId()),
                    event.getProducts(),
                    event.getTotal(),
                    event.getType(),
                    event.aggregateRootId()
            );
                productRepository.reduceStock(event.getProducts()).subscribe();
                invoiceRepository.saveInvoice(invoiceEntity).subscribe();
        });

        listen((ResellerCustomerSaleRegistered event) -> {
            InvoiceEntity invoiceEntity = new InvoiceEntity(
                    InvoiceId.of(event.getInvoiceId()),
                    event.getProducts(),
                    event.getTotal(),
                    event.getType(),
                    event.aggregateRootId()
            );
            productRepository.reduceStock(event.getProducts()).subscribe();
            invoiceRepository.saveInvoice(invoiceEntity).subscribe();
        });
    }
}
