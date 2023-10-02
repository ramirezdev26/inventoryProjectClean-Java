package co.diegofer.inventoryclean.usecase.addstocktoproduct;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.AddStockToProductCommand;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.product.InventoryStock;
import co.diegofer.inventoryclean.model.values.product.ProductId;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.UserCaseForCommand;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class AddStockToProductUseCase extends UserCaseForCommand<AddStockToProductCommand> {

    private final ProductRepository productRepository;

    private final DomainEventRepository repository;

    public AddStockToProductUseCase(ProductRepository productRepository, DomainEventRepository repository) {
        this.productRepository = productRepository;
        this.repository = repository;
    }


    @Override
    public Flux<DomainEvent> apply(Mono<AddStockToProductCommand> buyProductCommandMono) {
        return buyProductCommandMono.flatMapMany(command -> repository.findById(command.getBranchId())
                .collectList()
                .flatMapMany(events -> productRepository.addStock(command.getProductId(), command.getQuantity())
                        .flatMapMany(

                                productsStockAdded ->{
                                    BranchAggregate branch = BranchAggregate.from(BranchId.of(command.getBranchId()), events);
                                    branch.addStockToProduct(
                                            ProductId.of(command.getProductId()),
                                            new InventoryStock(command.getQuantity())
                                    );

                                    return Flux.fromIterable(branch.getUncommittedChanges());
                                }
                        ))
                .map(event -> {
                    repository.saveEvent(event);
                    return event;
                }).flatMap(repository::saveEvent)
        );
    }
}
