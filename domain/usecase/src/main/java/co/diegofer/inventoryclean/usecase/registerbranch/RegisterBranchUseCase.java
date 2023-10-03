package co.diegofer.inventoryclean.usecase.registerbranch;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.branch.gateways.BranchRepository;
import co.diegofer.inventoryclean.model.commands.RegisterBranchCommand;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.branch.City;
import co.diegofer.inventoryclean.model.values.branch.Country;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.EventBus;
import co.diegofer.inventoryclean.usecase.generics.UserCaseForCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RegisterBranchUseCase extends UserCaseForCommand<RegisterBranchCommand> {

    private final BranchRepository branchRepository;

    private final DomainEventRepository repository;
    private final EventBus eventBus;

    public RegisterBranchUseCase(BranchRepository branchRepository, DomainEventRepository repository, EventBus eventBus) {
        this.branchRepository = branchRepository;
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<RegisterBranchCommand> registerBranchCommandMono) {

        Mono<Branch> branchSaved =  registerBranchCommandMono.flatMap(command -> branchRepository.saveABranch(new Branch(command.getName(), command.getCountry(), command.getCity())));

        return branchSaved.flatMapIterable(branch -> {
            System.out.println(branch.getName());
            BranchAggregate branchAggregate = new BranchAggregate(
                    BranchId.of(branch.getId()),
                    new Name(branch.getName()),
                    new Country(branch.getCountry()),
                    new City(branch.getCity())
            );
            return branchAggregate.getUncommittedChanges();
        }).map(event -> {
            eventBus.publish(event);
            return event;
        }).flatMap(repository::saveEvent);
    }


}
