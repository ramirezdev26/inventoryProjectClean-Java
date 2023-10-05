package co.diegofer.inventoryclean.usecase.query.getinvoicesbybranchid;

import co.diegofer.inventoryclean.model.InvoiceEntity;
import co.diegofer.inventoryclean.usecase.generics.InvoiceData;
import co.diegofer.inventoryclean.usecase.generics.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetInvoicesByBranchIdUseCase  {

    private final InvoiceRepository invoiceRepository;

    public Flux<InvoiceData> apply(String branchId) {return invoiceRepository.getInvoicesByBranchId(branchId);}

}
