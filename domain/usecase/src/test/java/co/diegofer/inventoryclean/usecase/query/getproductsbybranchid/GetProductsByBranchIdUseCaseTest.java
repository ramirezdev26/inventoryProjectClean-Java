package co.diegofer.inventoryclean.usecase.query.getproductsbybranchid;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.usecase.generics.InvoiceData;
import co.diegofer.inventoryclean.usecase.generics.InvoiceRepository;
import co.diegofer.inventoryclean.usecase.query.getinvoicesbybranchid.GetInvoicesByBranchIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetProductsByBranchIdUseCaseTest {

    @Mock
    private ProductRepository productRepository;
    private GetProductsByBranchIdUseCase getProductsByBranchIdUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        getProductsByBranchIdUseCase = new GetProductsByBranchIdUseCase(productRepository); }

    @Test
    void apply() {

        List<Product> sampleProducts = new ArrayList<>();
        sampleProducts.add(new Product("product1", "descripcion", 10, 10, "electrical","root"));
        sampleProducts.add(new Product("product2", "descripcion", 10, 10, "electrical","root"));
        sampleProducts.add(new Product("product3", "descripcion", 10, 10, "electrical","root"));

        when(productRepository.findProductsByBranch("root")).thenReturn(Flux.fromIterable(sampleProducts));

        // Act
        Flux<Product> result = getProductsByBranchIdUseCase.apply("root");

        StepVerifier.create(result)
                .expectNextCount(3)
                .verifyComplete();

    }
}