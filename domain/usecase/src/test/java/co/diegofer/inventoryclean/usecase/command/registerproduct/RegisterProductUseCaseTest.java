package co.diegofer.inventoryclean.usecase.command.registerproduct;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.AddProductCommand;
import co.diegofer.inventoryclean.model.events.ProductAdded;
import co.diegofer.inventoryclean.model.events.StockToProductAdded;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.branch.City;
import co.diegofer.inventoryclean.model.values.branch.Country;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.usecase.command.registerfinalcustomersale.RegisterFinalCustomerSaleUseCase;
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

class RegisterProductUseCaseTest {

    @Mock
    private DomainEventRepository repository;

    @Mock
    private EventBus eventBus;

    private RegisterProductUseCase registerProductUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Inicializa los mocks
        registerProductUseCase = new RegisterProductUseCase(repository, eventBus);
    }

    @Test
    void apply() {
        AddProductCommand addProductCommand = new AddProductCommand("root", "productid", "name",
                "electrical", "description", 5);

        BranchAggregate savedBranch = new BranchAggregate(BranchId.of("root"), new Name("name"), new Country("Country"), new City("City"));
        Flux<DomainEvent> events = Flux.fromIterable(savedBranch.getUncommittedChanges());

        when(repository.findById(Mockito.any())).thenReturn(events);
        when(repository.saveEvent(Mockito.any())).thenReturn(Mono.just(new ProductAdded()));

        // Act
        StepVerifier.create(registerProductUseCase.apply(Mono.just(addProductCommand)))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void invalidCategoryScenario(){
        AddProductCommand addProductCommand = new AddProductCommand("root", "productid", "name",
                "anotherCategory", "description", 5);

        BranchAggregate savedBranch = new BranchAggregate(BranchId.of("root"), new Name("name"), new Country("Country"), new City("City"));
        Flux<DomainEvent> events = Flux.fromIterable(savedBranch.getUncommittedChanges());

        when(repository.findById(Mockito.any())).thenReturn(events);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            registerProductUseCase.apply(Mono.just(addProductCommand))
                    .blockLast();
        });
    }
}