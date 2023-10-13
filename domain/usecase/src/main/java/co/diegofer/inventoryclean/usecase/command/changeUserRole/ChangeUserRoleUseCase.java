package co.diegofer.inventoryclean.usecase.command.changeUserRole;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.ChangeUserRoleCommand;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.user.Role;
import co.diegofer.inventoryclean.model.values.user.UserId;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.EventBus;
import co.diegofer.inventoryclean.usecase.generics.UserCaseForCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ChangeUserRoleUseCase extends UserCaseForCommand<ChangeUserRoleCommand> {


    private final DomainEventRepository repository;
    private final EventBus eventBus;


    public ChangeUserRoleUseCase(DomainEventRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }


    @Override
    public Flux<DomainEvent> apply(Mono<ChangeUserRoleCommand> changeUserRoleCommandMono) {
        return changeUserRoleCommandMono.flatMapMany(command -> repository.findById(command.getBranchId())
                        .collectList()
                        .flatMapMany(
                                events ->{
                                    BranchAggregate branch = BranchAggregate.from(BranchId.of(command.getBranchId()), events);
                                    branch.changeRoleToUser(
                                            UserId.of(command.getUserId()),
                                            new Role(command.getRole())
                                    );

                                    return Flux.fromIterable(branch.getUncommittedChanges());
                                }
                        ))
                .map(event -> {
                    eventBus.publish(event);
                    return event;
                }).flatMap(repository::saveEvent);
    }
}
