package co.diegofer.inventoryclean.usecase;

import co.diegofer.inventoryclean.model.InvoiceEntity;
import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.branch.gateways.BranchRepository;
import co.diegofer.inventoryclean.model.events.*;
import co.diegofer.inventoryclean.model.generic.DomainUpdater;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.model.supplier.Supplier;
import co.diegofer.inventoryclean.model.supplier.gateway.SupplierRepository;
import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.model.user.gateways.UserRepository;
import co.diegofer.inventoryclean.model.values.invoice.InvoiceId;
import co.diegofer.inventoryclean.usecase.generics.InvoiceRepository;

public class MySqlUpdater extends DomainUpdater {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final SupplierRepository supplierRepository;



    public MySqlUpdater(BranchRepository branchRepository, ProductRepository productRepository, UserRepository userRepository, InvoiceRepository invoiceRepository, SupplierRepository supplierRepository) {
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.supplierRepository = supplierRepository;


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

        listen((SupplierAdded event) -> {
            Supplier supplier = new Supplier(event.getSupplierId(), event.getName(),
                    event.getNumber(), event.getEmail(), event.getPayment_term(),
                    event.getBranchId());
            supplierRepository.saveASupplier(supplier).subscribe();
        });

        listen((StockToProductAdded event) -> {
            productRepository.addStock(event.getProducts()).subscribe();
        });

        listen((RoleToUserChanged event) -> {
            userRepository.changeRole(event.getUserId(), event.getRoleToChange()).subscribe();
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
