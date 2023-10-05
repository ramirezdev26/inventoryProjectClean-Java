package co.diegofer.inventoryclean.usecase.command.registerproduct;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.AddProductCommand;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.model.values.product.*;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.EventBus;
import co.diegofer.inventoryclean.usecase.generics.UserCaseForCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class RegisterProductUseCase extends UserCaseForCommand<AddProductCommand> {


    private final DomainEventRepository repository;
    private final EventBus eventBus;


    public RegisterProductUseCase(DomainEventRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }


    @Override
    public Flux<DomainEvent> apply(Mono<AddProductCommand> addProductCommandMono) {
        return addProductCommandMono.flatMapMany(command -> repository.findById(command.getBranchId())
                .collectList()
                .flatMapIterable(events -> {

                    Name name = new Name(command.getName());
                    Category category = new Category(command.getCategory());
                    Description description = new Description(command.getDescription());
                    Price price = new Price(command.getPrice());

                    BranchAggregate branch = BranchAggregate.from(BranchId.of(command.getBranchId()),events);
                    branch.addProduct(
                            ProductId.of(UUID.randomUUID().toString()),
                            name,
                            category,
                            description,
                            price,
                            command.getBranchId()
                    );
                    return branch.getUncommittedChanges();
                }).map(event -> {
                    eventBus.publish(event);
                    return event;
                }).flatMap(repository::saveEvent)
        );
    }

}
