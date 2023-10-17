package co.diegofer.inventoryclean.usecase.command.registeruser;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.AddProductCommand;
import co.diegofer.inventoryclean.model.commands.RegisterUserCommand;
import co.diegofer.inventoryclean.model.events.ProductAdded;
import co.diegofer.inventoryclean.model.events.UserAdded;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.branch.City;
import co.diegofer.inventoryclean.model.values.branch.Country;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.usecase.command.registerproduct.RegisterProductUseCase;
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

class RegisterUserUseCaseTest {

    @Mock
    private DomainEventRepository repository;

    @Mock
    private EventBus eventBus;

    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Inicializa los mocks
        registerUserUseCase = new RegisterUserUseCase(repository, eventBus);
    }

    @Test
    void apply() {
        RegisterUserCommand registerUserCommand = new RegisterUserCommand("root", "userid", "name",
                "lastname", "email", "password", "ADMIN");

        BranchAggregate savedBranch = new BranchAggregate(BranchId.of("root"), new Name("name"), new Country("Country"), new City("City"));
        Flux<DomainEvent> events = Flux.fromIterable(savedBranch.getUncommittedChanges());

        when(repository.findById(Mockito.any())).thenReturn(events);
        when(repository.saveEvent(Mockito.any())).thenReturn(Mono.just(new UserAdded()));

        // Act
        StepVerifier.create(registerUserUseCase.apply(Mono.just(registerUserCommand)))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void invalidRoleEscenario() {
        RegisterUserCommand registerUserCommand = new RegisterUserCommand("root", "userid", "name",
                "lastname", "email", "password", "NoValid");

        BranchAggregate savedBranch = new BranchAggregate(BranchId.of("root"), new Name("name"), new Country("Country"), new City("City"));
        Flux<DomainEvent> events = Flux.fromIterable(savedBranch.getUncommittedChanges());

        when(repository.findById(Mockito.any())).thenReturn(events);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            registerUserUseCase.apply(Mono.just(registerUserCommand))
                    .blockLast();
        });
    }
}