package co.diegofer.inventoryclean.usecase.command.changeUserRole;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.ChangeUserRoleCommand;
import co.diegofer.inventoryclean.model.events.RoleToUserChanged;
import co.diegofer.inventoryclean.model.events.StockToProductAdded;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.branch.City;
import co.diegofer.inventoryclean.model.values.branch.Country;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.model.values.user.*;
import co.diegofer.inventoryclean.usecase.command.addstocktoproduct.AddStockToProductUseCase;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ChangeUserRoleUseCaseTest {

    @Mock
    private DomainEventRepository repository;

    @Mock
    private EventBus eventBus;

    private ChangeUserRoleUseCase changeUserRoleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Inicializa los mocks
        changeUserRoleUseCase = new ChangeUserRoleUseCase(repository, eventBus);
    }

    @Test
    void apply() {
        ChangeUserRoleCommand changeUserRoleCommand = new ChangeUserRoleCommand("userid","root","ADMIN");

        BranchAggregate savedBranch = new BranchAggregate(BranchId.of("root"), new Name("name"), new Country("Country"), new City("City"));
        savedBranch.addUser(UserId.of("userid"),new Name("name"),new LastName("lastname"),new Email("email"),
                new Password("password"),new Role("SELLER"),"root");
        Flux<DomainEvent> events = Flux.fromIterable(savedBranch.getUncommittedChanges());

        when(repository.findById(Mockito.any())).thenReturn(events);
        when(repository.saveEvent(Mockito.any())).thenReturn(Mono.just(new RoleToUserChanged()));

        // Act
        StepVerifier.create(changeUserRoleUseCase.apply(Mono.just(changeUserRoleCommand)))
                .expectNextCount(1)
                .verifyComplete();

    }
}