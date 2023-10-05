package co.diegofer.inventoryclean.usecase;

import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.branch.gateways.BranchRepository;
import co.diegofer.inventoryclean.model.events.BranchCreated;
import co.diegofer.inventoryclean.model.events.ProductAdded;
import co.diegofer.inventoryclean.model.generic.DomainUpdater;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.model.user.gateways.UserRepository;

import java.util.ArrayList;

public class MySqlUpdater extends DomainUpdater {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;



    public MySqlUpdater(BranchRepository branchRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;


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
    }
}
