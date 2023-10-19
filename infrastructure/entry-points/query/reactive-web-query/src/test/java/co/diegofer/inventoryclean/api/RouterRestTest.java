package co.diegofer.inventoryclean.api;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.supplier.Supplier;
import co.diegofer.inventoryclean.model.user.User;
import co.diegofer.inventoryclean.usecase.generics.InvoiceData;
import co.diegofer.inventoryclean.usecase.query.getallbranches.GetAllBranchesUseCase;
import co.diegofer.inventoryclean.model.branch.Branch;
import co.diegofer.inventoryclean.usecase.query.getinvoicesbybranchid.GetInvoicesByBranchIdUseCase;
import co.diegofer.inventoryclean.usecase.query.getproductsbybranchid.GetProductsByBranchIdUseCase;
import co.diegofer.inventoryclean.usecase.query.getsuppliersbybranchid.GetSuppliersByBranchIdUseCase;
import co.diegofer.inventoryclean.usecase.query.getusersbybranchid.GetUsersByBranchIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


class RouterRestTest {
    private WebTestClient webTestClient;

    @Mock
    private GetAllBranchesUseCase getAllBranchesUseCase;
    @Mock
    private GetProductsByBranchIdUseCase getProductsByBranchIdUseCase;
    @Mock
    private GetUsersByBranchIdUseCase getUsersByBranchIdUseCase;
    @Mock
    private GetSuppliersByBranchIdUseCase getSuppliersByBranchIdUseCase;
    @Mock
    private GetInvoicesByBranchIdUseCase getInvoicesByBranchIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        webTestClient = WebTestClient
                .bindToRouterFunction(new RouterRest().viewAllBranches(getAllBranchesUseCase)
                        .andOther(new RouterRest().getProductsByBranch(getProductsByBranchIdUseCase))
                        .andOther(new RouterRest().getUsersByBranch(getUsersByBranchIdUseCase))
                        .andOther(new RouterRest().getSuppliersByBranch(getSuppliersByBranchIdUseCase))
                        .andOther(new RouterRest().getInvoicesByBranch(getInvoicesByBranchIdUseCase)))
                .configureClient()
                .baseUrl("http://localhost:8081")
                .build();
    }

    @Test
    void testGetAllBranches() {
        Branch branch1 = new Branch("branch1", "country", "city");
        Branch branch2 = new Branch("branch2", "country", "city");
        Flux<Branch> branches = Flux.just(branch1, branch2);

        when(getAllBranchesUseCase.apply()).thenReturn(branches);

        webTestClient
                .get()
                .uri("/branches")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBodyList(Branch.class)
                .hasSize(2);
    }

    @Test
    void getProductsByBranch() {
        Product product1 = new Product("product1","descripcion",2,10,"electrical","branch_id");
        Product product2 = new Product("product2","descripcion",2,10,"electrical","branch_id");

        Flux<Product> products = Flux.just(product1, product2);

        when(getProductsByBranchIdUseCase.apply("branch_id")).thenReturn(products);

        webTestClient
                .get()
                .uri("/products/branch_id")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBodyList(Product.class)
                .hasSize(2);

    }

    @Test
    void testGetProductsByBranchIllegalArgumentException() {
        when(getProductsByBranchIdUseCase.apply("branch_id")).thenReturn(Flux.error(new IllegalArgumentException("Branch not found")));

        webTestClient
                .get()
                .uri("/products/branch_id")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Branch not found");;
    }

    @Test
    void getUsersByBranch() {
        User user1 = new User("user1", "lastName", "email","password","ADMIN","branch_id");
        User user2 = new User("user2","lastName", "email","password","ADMIN","branch_id");
        Flux<User> users = Flux.just(user1, user2);

        when(getUsersByBranchIdUseCase.apply("branch_id")).thenReturn(users);

        webTestClient
                .get()
                .uri("/users/branch_id")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBodyList(User.class)
                .hasSize(2);
    }

    @Test
    void testGetUsersByBranchNotFound() {
        when(getUsersByBranchIdUseCase.apply("branch_id")).thenReturn(Flux.error(new IllegalArgumentException("Branch not found")));

        webTestClient
                .get()
                .uri("/users/branch_id")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Branch not found");
    }

    @Test
    void getSuppliersByBranch() {
        Supplier supplier1 = new Supplier("user1", 12345, "email","payment_term","branch_id");
        Supplier supplier2 = new Supplier("user2",12345, "email","payment_term","branch_id");
        Flux<Supplier> suppliers = Flux.just(supplier1, supplier2);

        when(getSuppliersByBranchIdUseCase.apply("branch_id")).thenReturn(suppliers);

        webTestClient
                .get()
                .uri("/suppliers/branch_id")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBodyList(Supplier.class)
                .hasSize(2);
    }

    @Test
    void testGetSuppliersByBranchNotFound() {
        when(getSuppliersByBranchIdUseCase.apply("branch_id")).thenReturn(Flux.error(new IllegalArgumentException("Branch not found")));

        webTestClient
                .get()
                .uri("/suppliers/branch_id")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Branch not found");
    }

    @Test
    void getInvoicesByBranch() {
        List<ProductSale> products = new ArrayList<>();
        InvoiceData invoiceData1 = new InvoiceData("userId1",products, 123, LocalDateTime.now(),"Reseller","branch_id");
        InvoiceData invoiceData2 = new InvoiceData("userId2",products, 123, LocalDateTime.now(),"Final Customer","branch_id");
        Flux<InvoiceData> invoices = Flux.just(invoiceData1, invoiceData2);

        when(getInvoicesByBranchIdUseCase.apply("branch_id")).thenReturn(invoices);

        webTestClient
                .get()
                .uri("/invoices/branch_id")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBodyList(InvoiceData.class)
                .hasSize(2);
    }

    @Test
    void testGetInvoicesByBranchNotFound() {
        when(getInvoicesByBranchIdUseCase.apply("branch_id")).thenReturn(Flux.error(new IllegalArgumentException("Branch not found")));

        webTestClient
                .get()
                .uri("/invoices/branch_id")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Branch not found");
    }

}