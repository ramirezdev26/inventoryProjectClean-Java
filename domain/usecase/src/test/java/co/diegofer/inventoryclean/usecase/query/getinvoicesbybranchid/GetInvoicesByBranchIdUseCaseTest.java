package co.diegofer.inventoryclean.usecase.query.getinvoicesbybranchid;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.usecase.generics.InvoiceData;
import co.diegofer.inventoryclean.usecase.generics.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class GetInvoicesByBranchIdUseCaseTest {
    @Mock
    private InvoiceRepository invoiceRepository;
    private GetInvoicesByBranchIdUseCase getInvoicesByBranchIdUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        getInvoicesByBranchIdUseCase = new GetInvoicesByBranchIdUseCase(invoiceRepository); }

    @Test
    void apply() {

        List<InvoiceData> sampleInvoices = new ArrayList<>();
        List<ProductSale> products = new ArrayList<>();
        sampleInvoices.add(new InvoiceData("invoice1", products, 10, LocalDateTime.now(), "Reseller","root"));
        sampleInvoices.add(new InvoiceData("invoice2", products, 30, LocalDateTime.now(),"Final Customer","root"));
        sampleInvoices.add(new InvoiceData("invoice3", products, 20, LocalDateTime.now(),"Reseller","root"));

        when(invoiceRepository.getInvoicesByBranchId("root")).thenReturn(Flux.fromIterable(sampleInvoices));

        // Act
        Flux<InvoiceData> result = getInvoicesByBranchIdUseCase.apply("root");

        StepVerifier.create(result)
                .expectNextCount(3)
                .verifyComplete();

    }
}