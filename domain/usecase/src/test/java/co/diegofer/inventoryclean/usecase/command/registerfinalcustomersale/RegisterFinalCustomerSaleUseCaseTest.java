package co.diegofer.inventoryclean.usecase.command.registerfinalcustomersale;

import co.diegofer.inventoryclean.model.BranchAggregate;
import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.RegisterFinalCustomerSaleCommand;
import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
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
import co.diegofer.inventoryclean.usecase.command.changeUserRole.ChangeUserRoleUseCase;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RegisterFinalCustomerSaleUseCaseTest {

    @Mock
    private DomainEventRepository repository;

    @Mock
    private EventBus eventBus;

    private RegisterFinalCustomerSaleUseCase registerFinalCustomerSaleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Inicializa los mocks
        registerFinalCustomerSaleUseCase = new RegisterFinalCustomerSaleUseCase(repository, eventBus);
    }

    @Test
    void apply() {
        List<ProductSale> productSaleList = new ArrayList<>();
        productSaleList.add(new ProductSale("productID", "proName", 2));
        RegisterFinalCustomerSaleCommand registerFinalCustomerSaleCommand = new RegisterFinalCustomerSaleCommand(productSaleList, "root");

        List<ProductToAdd> productsToAddStock = new ArrayList<>();
        productsToAddStock.add(new ProductToAdd("productID", 5));
        BranchAggregate savedBranch = new BranchAggregate(BranchId.of("root"), new Name("name"), new Country("Country"), new City("City"));
        savedBranch.addProduct(ProductId.of("productID"), new Name("proName"), new Category("electrical"),
                new Description("Descripcion"), new Price(10), "root");
        savedBranch.addStockToProduct(productsToAddStock);
        Flux<DomainEvent> events = Flux.fromIterable(savedBranch.getUncommittedChanges());

        when(repository.findById(Mockito.any())).thenReturn(events);
        when(repository.saveEvent(Mockito.any())).thenReturn(Mono.just(new StockToProductAdded()));

        // Act
        StepVerifier.create(registerFinalCustomerSaleUseCase.apply(Mono.just(registerFinalCustomerSaleCommand)))
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    void quantityBiggerThanCurrentStock(){
        List<ProductSale> productSaleList = new ArrayList<>();
        productSaleList.add(new ProductSale("productID", "proName", 6));
        RegisterFinalCustomerSaleCommand registerFinalCustomerSaleCommand = new RegisterFinalCustomerSaleCommand(productSaleList, "root");

        List<ProductToAdd> productsToAddStock = new ArrayList<>();
        productsToAddStock.add(new ProductToAdd("productID", 5));
        BranchAggregate savedBranch = new BranchAggregate(BranchId.of("root"), new Name("name"), new Country("Country"), new City("City"));
        savedBranch.addProduct(ProductId.of("productID"), new Name("proName"), new Category("electrical"),
                new Description("Descripcion"), new Price(10), "root");
        savedBranch.addStockToProduct(productsToAddStock);
        Flux<DomainEvent> events = Flux.fromIterable(savedBranch.getUncommittedChanges());

        when(repository.findById(Mockito.any())).thenReturn(events);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            registerFinalCustomerSaleUseCase.apply(Mono.just(registerFinalCustomerSaleCommand))
                    .blockLast();
        });

    }

}