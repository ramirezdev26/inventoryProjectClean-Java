package co.diegofer.inventoryclean.usecase.command.registerSupplier;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.RegisterSupplierCommand;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.model.values.supplier.ContactNumber;
import co.diegofer.inventoryclean.model.values.supplier.PaymentTerm;
import co.diegofer.inventoryclean.model.values.supplier.SupplierId;
import co.diegofer.inventoryclean.model.values.user.Email;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.EventBus;
import co.diegofer.inventoryclean.usecase.generics.UserCaseForCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class RegisterSupplierUseCase extends UserCaseForCommand<RegisterSupplierCommand> {


    private final DomainEventRepository repository;
    private final EventBus eventBus;


    public RegisterSupplierUseCase(DomainEventRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }


    @Override
    public Flux<DomainEvent> apply(Mono<RegisterSupplierCommand> registerSupplierCommandMono) {
        return registerSupplierCommandMono.flatMapMany(command -> repository.findById(command.getBranchId())
                .collectList()
                .flatMapIterable(events -> {

                    Name name = new Name(command.getName());
                    ContactNumber number = new ContactNumber(command.getNumber());
                    Email email = new Email(command.getEmail());
                    PaymentTerm payment_term = new PaymentTerm(command.getPayment_term());

                    BranchAggregate branch = BranchAggregate.from(BranchId.of(command.getBranchId()),events);
                    branch.registerSupplier(
                            SupplierId.of(UUID.randomUUID().toString()),
                            name,
                            number,
                            email,
                            payment_term,
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