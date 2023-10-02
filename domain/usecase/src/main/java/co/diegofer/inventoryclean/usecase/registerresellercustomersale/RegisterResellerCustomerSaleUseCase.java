package co.diegofer.inventoryclean.usecase.registerresellercustomersale;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.RegisterResellerCustomerSaleCommand;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.UserCaseForCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RegisterResellerCustomerSaleUseCase extends UserCaseForCommand<RegisterResellerCustomerSaleCommand> {

    private final ProductRepository productRepository;

    private final DomainEventRepository repository;

    public RegisterResellerCustomerSaleUseCase(ProductRepository productRepository, DomainEventRepository repository) {
        this.productRepository = productRepository;
        this.repository = repository;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<RegisterResellerCustomerSaleCommand> registerFinalCustomerSaleCommandMono) {
        return registerFinalCustomerSaleCommandMono.flatMapMany(command -> repository.findById(command.getBranchId())
                .collectList()
                .flatMapMany(events -> productRepository.reduceStock(command.getProducts())
                        .flatMapMany(

                                productsStockReduced ->{
                                    BranchAggregate branch = BranchAggregate.from(BranchId.of(command.getBranchId()), events);
                                    branch.registerFinalCustomerSale(
                                            command.getProducts(),
                                            branch.calculateTotal(command.getProducts())
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
