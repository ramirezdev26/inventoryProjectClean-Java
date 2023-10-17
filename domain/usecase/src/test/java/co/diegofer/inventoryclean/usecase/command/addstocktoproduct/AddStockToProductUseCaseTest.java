package co.diegofer.inventoryclean.usecase.command.addstocktoproduct;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.model.commands.AddStockToProductCommand;
import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
import co.diegofer.inventoryclean.model.events.BranchCreated;
import co.diegofer.inventoryclean.model.events.StockToProductAdded;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.model.values.branch.BranchId;
import co.diegofer.inventoryclean.model.values.branch.City;
import co.diegofer.inventoryclean.model.values.branch.Country;
import co.diegofer.inventoryclean.model.values.common.Name;
import co.diegofer.inventoryclean.model.values.product.Category;
import co.diegofer.inventoryclean.model.values.product.Description;
import co.diegofer.inventoryclean.model.values.product.Price;
import co.diegofer.inventoryclean.model.values.product.ProductId;
import co.diegofer.inventoryclean.usecase.command.registerbranch.RegisterBranchUseCase;
import co.diegofer.inventoryclean.usecase.generics.DomainEventRepository;
import co.diegofer.inventoryclean.usecase.generics.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddStockToProductUseCaseTest {

    @Mock
    private DomainEventRepository repository;

    @Mock
    private EventBus eventBus;

    private AddStockToProductUseCase addStockToProductUseCase;

    @BeforeEach
    void setUp() {
        addStockToProductUseCase = new AddStockToProductUseCase(repository, eventBus);
    }

    @Test
    void apply() {
        List<ProductToAdd> products = new ArrayList<>();
        products.add(new ProductToAdd("productID", 5));
        AddStockToProductCommand addStockToProductCommand = new AddStockToProductCommand(products, "root");

        BranchAggregate savedBranch = new BranchAggregate(BranchId.of("root"), new Name("name"), new Country("Country"), new City("City"));
        savedBranch.addProduct(ProductId.of("productID"), new Name("proName"), new Category("electrical"),
                new Description("Descripcion"), new Price(10), "root");
        Flux<DomainEvent> events = Flux.fromIterable(savedBranch.getUncommittedChanges());


        when(repository.findById(Mockito.any())).thenReturn(events);
        when(repository.saveEvent(Mockito.any())).thenReturn(Mono.just(new StockToProductAdded()));

        // Act
        StepVerifier.create(addStockToProductUseCase.apply(Mono.just(addStockToProductCommand)))
                .expectNextCount(1)
                .verifyComplete();
    }
}