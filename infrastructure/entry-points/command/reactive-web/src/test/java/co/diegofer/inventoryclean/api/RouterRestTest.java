package co.diegofer.inventoryclean.api;

import co.diegofer.inventoryclean.model.commands.*;
import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.RegisterFinalCustomerSaleCommand;
import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
import co.diegofer.inventoryclean.model.events.*;
import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.usecase.command.addstocktoproduct.AddStockToProductUseCase;
import co.diegofer.inventoryclean.usecase.command.changeUserRole.ChangeUserRoleUseCase;
import co.diegofer.inventoryclean.usecase.command.registerSupplier.RegisterSupplierUseCase;
import co.diegofer.inventoryclean.usecase.command.registerbranch.RegisterBranchUseCase;
import co.diegofer.inventoryclean.usecase.command.registerfinalcustomersale.RegisterFinalCustomerSaleUseCase;
import co.diegofer.inventoryclean.usecase.command.registerproduct.RegisterProductUseCase;
import co.diegofer.inventoryclean.usecase.command.registerresellercustomersale.RegisterResellerCustomerSaleUseCase;
import co.diegofer.inventoryclean.usecase.command.registeruser.RegisterUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RouterRestTest {

    private WebTestClient webTestClient;

    @Mock
    private RegisterBranchUseCase registerBranchUseCase;
    @Mock
    private RegisterProductUseCase registerProductUseCase;

    @Mock
    private RegisterUserUseCase registerUserUseCase;
    @Mock
    private RegisterSupplierUseCase registerSupplierUseCase;
    @Mock
    private AddStockToProductUseCase addStockToProductUseCase;
    @Mock
    private RegisterFinalCustomerSaleUseCase registerFinalCustomerSaleUseCase;
    @Mock
    private RegisterResellerCustomerSaleUseCase registerResellerCustomerSaleUseCaseCommand;
    @Mock
    private ChangeUserRoleUseCase changeUserRoleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Handler handler = new Handler(registerBranchUseCase, registerProductUseCase, registerUserUseCase,
                registerSupplierUseCase, addStockToProductUseCase, registerFinalCustomerSaleUseCase, registerResellerCustomerSaleUseCaseCommand,
                changeUserRoleUseCase);
        webTestClient = WebTestClient
                .bindToRouterFunction(new RouterRest().saveBranch(handler)
                        .andOther(new RouterRest().saveProduct(handler))
                        .andOther(new RouterRest().saveUser(handler))
                        .andOther(new RouterRest().saveSupplier(handler))
                        .andOther(new RouterRest().patchAddProductStock(handler))
                        .andOther(new RouterRest().patchChangeRoleUser(handler))
                        .andOther(new RouterRest().patchRegisterFinalCustomerSale(handler))
                        .andOther(new RouterRest().patchRegisterResellerCustomerSale(handler)))
                .configureClient()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Test
    void testRegisterBranch() {

        String name = "name";

        RegisterBranchCommand branchCommand = new RegisterBranchCommand("name", "country", "city");
        Flux<DomainEvent> event = Flux.just(new BranchCreated("name", "country", "city"));


        when(registerBranchUseCase.apply(any(Mono.class))).thenReturn(event);

        webTestClient.post()
                .uri("/branches")
                .body(Mono.just(branchCommand), RegisterBranchCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchCreated.class)
                .value(response -> {
                    assertEquals(name, response.getName());
                });

    }

    @Test
    void testRegisterProduct() {

        String name = "name";

        AddProductCommand addProductCommand = new AddProductCommand("branchid", "productid", "name",
                "electrical","description",10);
        Flux<DomainEvent> event = Flux.just(new ProductAdded("productid", "name",
                "electrical","description",10, "branchid"));


        when(registerProductUseCase.apply(any(Mono.class))).thenReturn(event);

        webTestClient.post()
                .uri("/product")
                .body(Mono.just(addProductCommand), AddProductCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductAdded.class)
                .value(response -> {
                    assertEquals(name, response.getName());
                });

    }

    @Test
    void testRegisterUser() {

        String name = "name";

        RegisterUserCommand registerUserCommand = new RegisterUserCommand("branchid", "userid", "name",
                "last_name","email","password", "SELLER");
        Flux<DomainEvent> event = Flux.just(new UserAdded("userid", "name",
                "last_name","email","password", "SELLER", "branchid"));


        when(registerUserUseCase.apply(any(Mono.class))).thenReturn(event);

        webTestClient.post()
                .uri("/user")
                .body(Mono.just(registerUserCommand), RegisterUserCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserAdded.class)
                .value(response -> {
                    assertEquals(name, response.getName());
                });

    }

    @Test
    void testRegisterSupplier() {

        String name = "name";

        RegisterSupplierCommand registerSupplierCommand = new RegisterSupplierCommand("branchid", "supplierid", "name",
                32145,"email","payment_term");
        Flux<DomainEvent> event = Flux.just(new SupplierAdded("branchid", "supplierid", "name",
                32145,"email","payment_term"));


        when(registerSupplierUseCase.apply(any(Mono.class))).thenReturn(event);

        webTestClient.post()
                .uri("/supplier")
                .body(Mono.just(registerSupplierCommand), RegisterSupplierCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SupplierAdded.class)
                .value(response -> {
                    assertEquals(name, response.getName());
                });
    }

    @Test
    void testAddStock() {

        List<ProductToAdd> products = new ArrayList<>();
        AddStockToProductCommand addStockToProductCommand = new AddStockToProductCommand(products, "branchid");
        Flux<DomainEvent> event = Flux.just(new StockToProductAdded(products));


        when(addStockToProductUseCase.apply(any(Mono.class))).thenReturn(event);

        webTestClient.patch()
                .uri("/product/purchase")
                .body(Mono.just(addStockToProductCommand), AddStockToProductCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StockToProductAdded.class)
                .value(response -> {
                    assertEquals(products.size(), response.getProducts().size());
                });
    }

    @Test
    void testPatchChangeRoleUser(){
        ChangeUserRoleCommand changeUserRoleCommand = new ChangeUserRoleCommand("userid","branchid","ADMIN");
        Flux<DomainEvent> event = Flux.just(new RoleToUserChanged("userid","ADMIN"));

        when(changeUserRoleUseCase.apply(any(Mono.class))).thenReturn(event);

        webTestClient.patch()
                .uri("/user/role")
                .body(Mono.just(changeUserRoleCommand), ChangeUserRoleCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RoleToUserChanged.class)
                .value(response -> {
                    assertEquals(changeUserRoleCommand.getUserId(), response.getUserId());
                });
    }

    @Test
    void testPatchRegisterFinalCustomerSale(){
        List<ProductSale> products = new ArrayList<>();
        RegisterFinalCustomerSaleCommand registerFinalCustomerSaleCommand = new RegisterFinalCustomerSaleCommand(products, "branchid");
        Flux<DomainEvent> event = Flux.just(new FinalCustomerSaleRegistered("invoiceid",products,20,"Final Customer Sale","branchid"));

        when(registerFinalCustomerSaleUseCase.apply(any(Mono.class))).thenReturn(event);

        webTestClient.patch()
                .uri("/product/customer-sale")
                .body(Mono.just(registerFinalCustomerSaleCommand), RegisterFinalCustomerSaleCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FinalCustomerSaleRegistered.class)
                .value(response -> {
                    assertEquals("invoiceid", response.getInvoiceId());
                });
    }

    @Test
    void testPatchRegisterResellerCustomerSale(){
        List<ProductSale> products = new ArrayList<>();
        RegisterResellerCustomerSaleCommand registerResellerCustomerSaleCommand = new RegisterResellerCustomerSaleCommand(products, "branchid");
        Flux<DomainEvent> event = Flux.just(new ResellerCustomerSaleRegistered("invoiceid",products,20,"Final Customer Sale","branchid"));

        when(registerResellerCustomerSaleUseCaseCommand.apply(any(Mono.class))).thenReturn(event);

        webTestClient.patch()
                .uri("/product/reseller-sale")
                .body(Mono.just(registerResellerCustomerSaleCommand), RegisterResellerCustomerSaleCommand.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResellerCustomerSaleRegistered.class)
                .value(response -> {
                    assertEquals("invoiceid", response.getInvoiceId());
                });
    }



}